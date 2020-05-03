package CalendarSystem;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * A Frequent Alert is a type of alert with appear times defined by frequency.
 * It is a subclass of Alert.
 *
 * @see Alert
 */
public class FrequentAlert extends Alert {

    /**
     * The frequency of the alert (ex. every 1 day).
     */
    private Duration frequency;

    /**
     * Initializes a new FrequentAlert object using the specified parameters.
     *
     * @param evT     datetime of the associated event.
     * @param name    name of the associated event.
     * @param message message content of the alert.
     * @param d       frequency of the alert (ex. every 1 day).
     */
    public FrequentAlert(LocalDateTime evT, String name, String message, Duration d) {
        super(evT, message, name);
        frequency = d;
        setTimesHelper();
        setData("Frequent Alert: " + message + " every " + durationToString(d));
    }

    /**
     * Checks if a frequent alert can be created.
     *
     * @param dateTime  the start time of the associated event.
     * @param now       the time currently.
     * @param frequency the frequency of the event.
     * @return true if the frequent alert can be created, otherwise false.
     */
    public static boolean isValidFrequentAlert(LocalDateTime dateTime, LocalDateTime now, Duration frequency) {
        boolean beforeEventStartTime = now.isBefore(dateTime);
        boolean hasValidAlertTime = dateTime.minus(frequency).isAfter(now);
        return beforeEventStartTime && hasValidAlertTime;
    }
    // methods for editing FrequentAlert

    /**
     * Changes the frequency of the frequent alert.
     *
     * @param newDur the new frequency.
     */
    public void changeFrequency(Duration newDur) {
        this.resetTimes();
        this.frequency = newDur;
        setTimesHelper();
    }

    /**
     * Adds every appear datetime to attribute times based on frequency.
     */
    private void setTimesHelper() {
        LocalDateTime t = getEventTime();
        do {
            t = t.minus(this.frequency);
        }

        //use system time now - needs to be changed!!
        while (t.isAfter(LocalDateTime.now()));

        //then t is the first alarm time
        while (t.isBefore(this.getEventTime())) {
            addtoTimes(t);
            t = t.plus(this.frequency);
        }
    }

    // implement abstract methods

    /**
     * Returns the type of alert.
     *
     * @return 'f' -> denoting frequent alert.
     */
    @Override
    public String getAlertType() {
        return "f";
    }


    // methods for displaying alert

    /**
     * Returns the duration in a readable format: ex. 1.5 days -> 1.5D.
     *
     * @param duration: duration to be formatted.
     * @return formatted string of the frequency.
     */
    public String durationToString(Duration duration) {
        if (duration.toDays() != 0)
            return duration.toDays() + "D";
        return duration.toHours() + "H";
    }

    @Override
    public String toString() {
        return "Frequent Alert: " + getMessage() + " every " + durationToString(frequency);
    }


    //getters

    /**
     * Gets the frequency of this alert.
     *
     * @return the frequency of this alert.
     */
    public Duration getFrequency() {
        return frequency;
    }

}
