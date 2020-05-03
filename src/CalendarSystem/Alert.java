package CalendarSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Represents an alert. An alert stores the name and time of the associated event, along with all the times the
 * alert should appear, the content of the message, and data for display.
 */
public abstract class Alert implements Serializable {
    /**
     * The name of the associated event.
     */
    private String name;

    /**
     * The datetime of the associated event.
     */
    private LocalDateTime eventTime;

    /**
     * A list of all the times the alert should appear.
     */
    private ArrayList<LocalDateTime> times;

    /**
     * The message content of the alert.
     */
    private String message;

    /**
     * A string representing the data alert contains (for GUI display purposes).
     */
    private String data;

    /**
     * Initializes a new Alert object with the given event date and name, and message for alert.
     *
     * @param date    date of the associated event
     * @param message message of the alert
     * @param name    name of the associated event
     */
    public Alert(LocalDateTime date, String message, String name) {
        this.name = name;
        this.eventTime = date;
        this.message = message;
        this.times = new ArrayList<>();
    }


    /**
     * Adds the date to the list of all alert times.
     *
     * @param date date to be added.
     */
    protected void addtoTimes(LocalDateTime date) {
        this.times.add(date);
    }

    /**
     * Removes the date from the list of all alert times.
     *
     * @param date date to be removed.
     */
    protected void removefromTimes(LocalDateTime date) {
        this.times.remove(date);
    }

    /**
     * Removes all of the alert times.
     */
    protected void resetTimes() {
        this.times = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Alert for Event" + name + "occurring at" + eventTime.toString() + ": " + message;
    }

    //abstract methods

    /**
     * Gets the type of the alert.
     *
     * @return the type of the alert, 'f' for frequent, 'i' for individual.
     */
    public abstract String getAlertType();

    //getters and setters

    /**
     * Gets the times at which the alert should appear.
     *
     * @return a list of the times at which the alert should appear.
     */
    public ArrayList<LocalDateTime> getTimes() {
        return times;
    }

    /**
     * Gets the dateTime of the associated event.
     *
     * @return the dateTime of the associated event.
     */
    public LocalDateTime getEventTime() {
        return this.eventTime;
    }

    /**
     * Sets the name of the alert.
     *
     * @param newName the name of the alert.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets the message content of the alert.
     *
     * @param newMessage the message content of the alert.
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    /**
     * Gets the name of the alert.
     *
     * @return the name of the alert.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the message content of the alert.
     *
     * @return the message content of the alert.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the data of the alert to be displayed.
     *
     * @return the data of the alert to be displayed
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the data of the alert to be displayed.
     *
     * @param data the data of the alert to be displayed.
     */
    public void setData(String data) {
        this.data = data;
    }
}
