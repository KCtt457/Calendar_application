package CalendarSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CalendarManager is a class that is user-specific and handles the creation and storage of the user's calendar(s).
 * It is also responsible for saving the user's calendar information to a .ser file.
 */
public class CalendarManager {

    /**
     * The user's selected calendar.
     */
    private Calendar calendar;

    /**
     * The name of the file containing the user's selected calendar.
     */
    private String filePath;

    /**
     * The name of the user.
     */
    private String userPath;

    private static final Logger logger = Logger.getLogger(CalendarManager.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();

    /**
     * Creates a new empty initialBuild.CalendarManager with the specified filePath as the username.
     *
     * @param filePath the name of the user.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public CalendarManager(String filePath) throws ClassNotFoundException, IOException {
        this.calendar = new Calendar();
        this.userPath = filePath;
        this.filePath = filePath + "default.ser"; // default name of calendar

        // Associate the handler with the logger.
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        // Reads serializable objects from file.
        // Populates the record list using stored data, if it exists.
        File file = new File(this.filePath);
        if (file.exists()) {
            readFromFile();
        } else {
            file.createNewFile();
        }
    }

    /**
     * Creates another calendar for the user with the specified name.
     * Note: This does not replace the user's default calendar, but is
     * another calendar added to the user's set of calendars.
     *
     * @param name the name of the new calendar.
     * @throws IOException
     */
    public void createCalendar(String name) throws IOException {
        saveToFile();
        this.filePath = this.userPath + name + ".ser";
        this.calendar = new Calendar();
        File file = new File(filePath);
        file.createNewFile();
        saveToFile();
    }

    /**
     * Changes the user's selected calendar to that with the specified name and reads from its file.
     *
     * @param name the name of one of the user's calendars
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void selectCalendar(String name) throws ClassNotFoundException, IOException {
        saveToFile();
        this.filePath = this.userPath + name + ".ser";
        File file = new File(filePath);
        if (file.exists()) {
            readFromFile();
        } else {
            file.createNewFile();
        }
    }

    /**
     * Reads from the file storing the user's calendar information.
     *
     * @throws ClassNotFoundException
     */
    public void readFromFile() throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(filePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize
            calendar = (Calendar) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    /**
     * Writes the calendar to file at filePath.
     *
     * @throws IOException
     */
    public void saveToFile() throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(calendar);
        output.close();
    }

    /**
     * Gets this user's selected calendar.
     *
     * @return a calendar belonging to the user.
     */
    public Calendar getCalendar() {
        return this.calendar;
    }

    /**
     * Gets a list of names of this user's calendars.
     *
     * @return the list of names of this user's calendars.
     */
    public ArrayList<String> getUserCalendars() {
        File userData = new File("user_data");
        String username = userPath.substring(12);
        ArrayList<String> userCalendars = new ArrayList<>();
        if (userData.isDirectory()) {
            File[] sample = userData.listFiles();
            if (sample != null) {
                for (File f : sample) {
                    if (f.getName().contains(username)) {
                        userCalendars.add(f.getName().replace(username, "").
                                replace(".ser", ""));
                    }
                }
            }
        }
        return userCalendars;
    }

    /**
     * Shares the specified event with another user's calendar.
     *
     * @param name  the name of the other user.
     * @param event the event to be shared.
     * @return true if event was shared, otherwise false.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean shareEvent(String name, Event event) throws IOException, ClassNotFoundException {
        String otherUserPath = "./user_data/" + name + "_";
        File friendFile = new File(otherUserPath + "default.ser");
        if (friendFile.exists()) {
            CalendarManager friendCalManager = new CalendarManager(otherUserPath);
            friendCalManager.getCalendar().addEventNotification(event);
            friendCalManager.saveToFile();
            return true;
        } else {
            return false;
        }
    }
}
