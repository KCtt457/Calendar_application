package CalendarSystem;

import java.time.LocalDateTime;

/**
 * Packages the data from alert system in a displayable format for the AlertMenu GUI.
 * Ex. A frequent alert may have multiple AlertSystemData (one for each appear date).
 */
public class AlertSystemData {
    /**
     * The appear time of the alert (single date).
     */
    private LocalDateTime time;

    /**
     * The name of the associated event with the alert.
     */
    private String eventName;

    /**
     * The message content of the alert.
     */
    private String message;

    /**
     * The type of the alert, 'f' for frequent, 'i' for individual.
     */
    private String type;

    /**
     * Initializes a new AlertSystemData object for GUI purposes.
     *
     * @param d datetime of the alert appear time.
     * @param e name of the associated event.
     * @param m message content of alert.
     * @param t type of the alert, 'f' for frequent, 'i' for individual.
     */
    public AlertSystemData(LocalDateTime d, String e, String m, String t) {
        time = d;
        eventName = e;
        message = m;
        type = t;
    }

    //getters and setters for display purposes

    /**
     * Gets the appear time of the alert.
     *
     * @return the appear time of the alert.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Sets the appear time of the alert.
     *
     * @param time the appear time of the alert.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Gets the name of the associated event to the alert.
     *
     * @return the name of the associated event to the alert.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the associate event to the alert.
     *
     * @param eventName the name of the associate event to the alert.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the message of the alert.
     *
     * @return the message of the alert.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the alert.
     *
     * @param message the message content of the alert.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the type of the alert.
     *
     * @return the type of the alert, 'f' for frequent, 'i' for individual.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the alert.
     *
     * @param type the type of the alert, 'f' for frequent, 'i' for individual.
     */
    public void setType(String type) {
        this.type = type;
    }
}

