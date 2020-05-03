package CalendarSystem;

import java.time.LocalDateTime;

/**
 * An IndividualAlert is a type of alert with appear times defined by scheduled datetime.
 * It is a subclass of the Alert class.
 *
 * @see Alert
 */
public class IndividualAlert extends Alert {
    /**
     * The scheduled time for the alert.
     */
    private LocalDateTime time;

    /**
     * Initializes a new IndividualAlert object using the specified parameters.
     *
     * @param evT     event datetime of the associated event.
     * @param name    event name of the associated event.
     * @param message message content of alert.
     * @param t       datetime of the individual alert to appear at.
     */
    public IndividualAlert(LocalDateTime evT, String name, String message, LocalDateTime t) {
        super(evT, message, name);
        time = t;
        addtoTimes(time);
        setData("Individual Alert: " + message + " at " + t);
    }

    // Methods for editing individual alert

    /**
     * Changes the datetime of the individual alert.
     *
     * @param newTime the new datetime of the alert.
     */
    public void changeTime(LocalDateTime newTime) {
        this.removefromTimes(this.time);
        this.time = newTime;
        addtoTimes(this.time);
    }

    // Methods for displaying alert

    /**
     * Returns string 'i' representing individual alert for display.
     *
     * @return type of alert ('i').
     */
    @Override
    public String getAlertType() {
        return "i";
    }


    @Override
    public String toString() {
        return "Individual Alert: " + getMessage() + " at " + time;
    }

    //getters

    /**
     * Gets the time of the alert.
     *
     * @return the time of the alert.
     */
    public LocalDateTime getTime() {
        return time;
    }
}
