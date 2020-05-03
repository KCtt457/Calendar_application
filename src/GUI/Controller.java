package GUI;

import CalendarSystem.CalendarManager;
import CalendarSystem.Calendar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Controller {

    private CalendarManager calendarManager;

    private String theme = "GUI/Light.css";

    protected void setTheme(String theme) {this.theme = theme;}

    protected String getTheme() {return theme;}

    protected void setCalendarManager(CalendarManager calManager) {
        calendarManager = calManager;
    }

    protected Calendar getCalendar() {
        return calendarManager.getCalendar();
    }

    protected CalendarManager getCalendarManager() {
        return calendarManager;
    }

    /*
     * Changes scene to display on the stage <item> is on to a new scene
     * specified by <fxmlFileName>. Then initializes the new scene controller
     * by passing in this controller's calendarManager and any additional
     * initialization the screen needs.
     */
    protected void setScreen(String fxmlFileName, Node item) throws IOException {
        setNewWindowAndGetLoader(fxmlFileName, (Stage) item.getScene().getWindow(), 900, 600);
    }

    /*
     * Additional steps the controller must perform
     * in order to fully create scene
     */
    protected void initScreen() {}

    /*
     * Sets the theme of currScene to this controller's theme
     */
    protected void setSceneTheme(Scene currScene) {
        if (theme.equals("GUI/Dark.css")) {
            currScene.getStylesheets().remove("GUI/Light.css");
            if (!currScene.getStylesheets().contains("GUI/Dark.css")) {
                currScene.getStylesheets().add("GUI/Dark.css");
            }
        } else if (theme.equals("GUI/Light.css")) {
            currScene.getStylesheets().remove("GUI/Dark.css");
            if (!currScene.getStylesheets().contains("GUI/Light.css")) {
                currScene.getStylesheets().add("GUI/Light.css");
            }
        }
    }

    /**
     * set new window and return the controller of the new window
     * @param fxmlFileName name of the fxml file
     * @param window stage to initialize
     * @param width width of window
     * @param height height of window
     * @return FXML loader of the fxml file
     * @throws IOException
     */
    protected FXMLLoader setNewWindowAndGetLoader(String fxmlFileName, Stage window,
                                                  int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFileName));
        Parent newRoot = loader.load();
        Scene newScreen = new Scene(newRoot, width, height);
        setSceneTheme(newScreen);
        window.setScene(newScreen);
        Controller controller = loader.getController();
        controller.setCalendarManager(calendarManager);
        controller.setTheme(theme);
        controller.initScreen();
        return loader;
    }

    protected StringConverter<LocalDate> getDateConverter(){
        String pattern = "dd/MM/yyyy";
        return new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
    }


    /**
     * open the pop up window and return the loader
     * @param title title of the new window
     * @param file filename of the fxml file to open
     * @throws IOException
     */
    protected void openWindowAndGetLoader(String title, String file) throws IOException {
        //Make New pop up window
        Stage window = new Stage();
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);

        //Create new scene to display
        FXMLLoader loader = setNewWindowAndGetLoader(file, window, 600, 350);

        window.showAndWait();
    }
}
