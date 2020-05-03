package CalendarSystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents an event.
 * Events are stored in a calendar.
 *
 * @see Calendar
 */
public class Event implements Serializable {

    /**
     * The name of this event.
     */
    private String eventName;

    /**
     * The start time and date of this event.
     */
    private LocalDateTime startTime;

    /**
     * The end time and date of this event.
     */
    private LocalDateTime endTime;

    /**
     * A label for this event.
     */
    private String tag;

    /**
     * Whether this Event is "past", "ongoing" or "future"
     */
    private String status;

    /**
     * A list of memos associated with this Event.
     */
    private ArrayList<Memo> memos;

    /** A list of series associated with this Event */
    public ArrayList<Series> series;

    /**
     * Constructs an indefinite event.
     * @param name: name of event
     */
    public Event(String name){
        eventName = name;
        startTime = LocalDateTime.MIN;
        endTime = LocalDateTime.MIN;
        tag = "";
        status = "future";
        memos = new ArrayList<>();
        series = new ArrayList<>();
    }

    /**
     * Initializes a new Event object with the specified name, start time and end time.
     * A newly created event by default has no memos, no tag and its status is automatically set
     * to "future".
     *
     * @param name  the name of this event.
     * @param start the start time and date for this event.
     * @param end   the end time and date for this event.
     */
    public Event(String name, LocalDateTime start, LocalDateTime end) {
        eventName = name;
        startTime = start;
        endTime = end;
        tag = "";  // default tag is an empty string
        status = "future"; // default status is "future"
        memos = new ArrayList<>(); // has no memos
        series = new ArrayList<>();
    }

    /**
     * Gets the name of this event.
     *
     * @return this event's name.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Gets the start time and date of this event.
     *
     * @return the start time and date of this event.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time and date of this event.
     *
     * @return the end time and date of this event.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the list of memos associated with this event.
     *
     * @return the list of memos associated with this event.
     */
    public ArrayList<Memo> getMemos() {
        return memos;
    }

    /**
     * Gets the tag for this event.
     *
     * @return this event's tag.
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * Returns whether this event is "past", "ongoing" or "future".
     *
     * @return "past" || "ongoing" || "future"
     */
    public String getStatus() {
        return this.status;
    }

    // Methods for editing this event

    /**
     * Changes the status of this event to "past", "ongoing" or "future".
     *
     * @param newStatus "past" || "ongoing" || "future"
     */
    public void changeStatus(String newStatus) {
        this.status = newStatus;
    }

    /**
     * Changes this event's name to the specified new name.
     *
     * @param newName the new name for this event.
     */
    public void setEventName(String newName) {
        this.eventName = newName;
    }

    /**
     * Changes the start time and date for this event to the specified new start time
     * and date.
     *
     * @param newStart the new start time and date for this event.
     */
    public void setStartTime(LocalDateTime newStart) {
        this.startTime = newStart;
    }

    /**
     * Changes the end time and date for this event to the specified new end time
     * and date.
     *
     * @param newEnd the new end time and date for this event.
     */
    public void setEndTime(LocalDateTime newEnd) {
        this.endTime = newEnd;
    }

    /**
     * Removes the specified memo from this event's list of memos, if it is present.
     *
     * @param memo a memo associated with this event.
     */
    public void deleteMemo(Memo memo) {
        memos.remove(memo);
    }

    /**
     * Removes all the memos from this event's list of memos.
     */
    public void deleteAllMemos() {
        memos = new ArrayList<>();
    }

    /**
     * Sets the tag for this event.
     *
     * @param tag the label for this event.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.eventName + "\t" + this.startTime.toString() + " to " + this.endTime.toString();
    }

    /**
     * Update the list of associated series.
     * @param s new series associated.
     */
    public void associateSeries(Series s) {
        series.add(s);
    }

    /**
     * Get the list of associated series to this event.
     * @return list of associated series
     */
    public ArrayList<Series> getSeries() {
        return series;
    }
}
