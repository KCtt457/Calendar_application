package CalendarSystem;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a calendar.
 * A calendar manages events, memos, notifications and alerts. A calendar is user-specific.
 *
 * @see AlertSystem
 * @see Event
 * @see MemoSystem
 * @see SeriesSystem
 */
public class Calendar implements Serializable {

    /**
     * A list of events stored in this calendar.
     */
    private ArrayList<Event> myEvents = new ArrayList<>();

    /**
     * The system for handling memos associated with the events stored in this calendar.
     */
    private MemoSystem myMemos = new MemoSystem();

    /**
     * The system for handling series of events.
     */
    private SeriesSystem mySeries = new SeriesSystem();

    /**
     * The system for handling alerts.
     */
    private AlertSystem myAlerts = new AlertSystem();

    /**
     * A list of events shared to this calendar.
     */
    private ArrayList<Event> notifications = new ArrayList<>();

    /**
     * The current time in this calendar.
     */
    public LocalDateTime time = LocalDateTime.now();

    // Methods for creating events, alerts, memos

    /**
     * Adds the specified event to this calendar's stored events.
     *
     * @param e the event to be added to this calendar.
     */
    public void addEvent(Event e) {
        myEvents.add(e);
    }

    /**
     * Adds an Individual alert to this calendar's alert system.
     *
     * @param e    the event associated with the alert.
     * @param msg  the alert's message.
     * @param date the time and date of the alert.
     */
    public void addIndividualAlert(Event e, String msg, LocalDateTime date) {
        myAlerts.addIndividualAlert(e, msg, date);
    }

    /**
     * Adds a Frequent alert to this calendar's alert system.
     *
     * @param e   the event associated with the alert.
     * @param msg the alert's message.
     * @param d   the frequency of the alert.
     */
    public void addFrequentAlert(Event e, String msg, Duration d) {
        myAlerts.addFrequentAlert(e, msg, d);
    }

    /**
     * Adds a new Memo object with the specified note and associates it with the specified events.
     *
     * @param events a list of events associated with the memo.
     * @param note   the note of the memo.
     */
    public void createMemo(List<Event> events, String note) {
        myMemos.createMemo(events, note);
    }

    // Methods for deleting events, alerts, memos

    /**
     * Removes the specified event from this calendar's stored events.
     *
     * @param e an event in this calendar.
     */
    public void deleteEvent(Event e) {
        this.myEvents.remove(e); // removes from this calendar's list of events
        if (!(e.getMemos() == null) && !(e.getMemos().isEmpty())) {
            deleteAllMemosforEvent(e); // removes memos for this event from memo system if not associated with any other events}
        }
        deleteAllAlertsforEvent(e); // removes all alerts for this event
    }

    /**
     * Removes the specified alert from this calendar's alert system.
     *
     * @param a an alert in this calendar's alert system.
     */
    public void deleteAlert(Alert a) {
        this.myAlerts.deleteAlert(a);
    }

    /**
     * Removes all the alerts for the specified event.
     *
     * @param e an event in this calendar.
     */
    public void deleteAllAlertsforEvent(Event e) {
        this.myAlerts.deleteAllAlertsforEvent(e);
    }

    /**
     * Removes the specified memo from this calendar's memo system.
     *
     * @param m a memo in this calendar's memo system.
     */
    public void deleteMemo(Memo m) {
        this.myMemos.deleteMemo(m); // deletes memo from memo system
        for (Event e : this.myEvents) {
            e.deleteMemo(m); // deletes memo from events, if present
        }
    }

    /**
     * Removes all the memos for the specified event.
     *
     * @param e an event in this calendar.
     */
    public void deleteAllMemosforEvent(Event e) {
        for (Memo m : e.getMemos()) {
            if (findEvent(m).size() == 1 && !this.myMemos.isEmpty() && !(this.myMemos == null)) {
                this.myMemos.deleteMemo(m); // removes memo from memo system if not associated with any other event
            }
        }
        e.deleteAllMemos(); // removes all memos from this event's stored memos
    }

    /**
     * Gets the memo system for this calendar.
     *
     * @return this calendar's memo system.
     */
    public MemoSystem getMyMemos() {
        return myMemos;
    }

    // Methods for editing events

    /**
     * Changes date and time of the specified event to the new specified start and end time and date.
     * Note: This method also deletes all the alerts for the specified event.
     *
     * @param event an event in this calendar.
     * @param start the new start time for the event.
     * @param end   the new end time for the event.
     */
    public void changeEventTime(Event event, LocalDateTime start, LocalDateTime end) {
        event.setStartTime(start);
        event.setEndTime(end);
        updateEventStatus(event);
        deleteAllAlertsforEvent(event); // deletes the alerts for the event
    }

    /**
     * Changes the name of the specified event to the specified new name.
     *
     * @param event    an event in this calendar.
     * @param new_name the new name for the event.
     */
    public void changeEventName(Event event, String new_name) {
        event.setEventName(new_name);
    }

    /**
     * Changes the tag for the specified event.
     *
     * @param tag   the new tag for the event.
     * @param event an event in this calendar.
     */
    public void changeEventTag(String tag, Event event) {
        event.setTag(tag);
    }


    /**
     * Recreate an event at additional time/day.
     * If the event is in a series of event, add the new event to the series.
     */
    public Event duplicateEvent(Event event, LocalDateTime start, LocalDateTime end) {
        String name = event.getEventName();
        Event duplicate = new Event(name, start, end);
        addEvent(duplicate);
        duplicate.getMemos().addAll(event.getMemos());

        ArrayList<Series> associateSeries = event.getSeries();
        if (!associateSeries.isEmpty()) {
            for (Series s : associateSeries) {
                mySeries.addEvent(s, duplicate);
            }
        }
        return duplicate;
    }

    //methods for finding list of events: by tag, memo or date

    /**
     * find events by their tag
     *
     * @param tag: the tag associated with an Event or multiple Events
     * @return A list of events with the input tag
     */
    public ArrayList<Event> findEvent(String tag) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : myEvents) {
            if (event.getTag().equals(tag)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * find events by date
     *
     * @param date: date of event
     * @return A list of events that are happening during the input date
     */
    public ArrayList<Event> findEvent(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : myEvents) {
            LocalDate start = event.getStartTime().toLocalDate();
            LocalDate end = event.getEndTime().toLocalDate();
            if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * find events by their memo
     *
     * @param memo: a memo associated with an event or multiple events
     * @return A list of events which have the input memo
     */
    public ArrayList<Event> findEvent(Memo memo) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : myEvents) {
            for (Memo memo1 : event.getMemos()) {
                if (memo1 == memo) {
                    events.add(event);
                }
            }
        }
        return events;
    }

    /**
     * find events by their memo note
     *
     * @param note: the note associated with a Memo
     * @return A list of events with the input note
     */
    public ArrayList<Event> findEventByMemoNote(String note) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : myEvents) {
            for (Memo m : event.getMemos()) {
                if (m.getNote().equals(note) && !events.contains(event)) {
                    events.add(event);
                }
            }
        }
        return events;
    }

    //methods for getting list of events: past, current or future

    /**
     * Gets the list of events stored in this calendar.
     *
     * @return the list of events stored in this calendar.
     */
    public ArrayList<Event> getMyEvents() {
        return myEvents;
    }

    /**
     * Gets the names of all the events in this Calendar.
     *
     * @return an array list of the names of events in this calendar.
     */
    public ArrayList<String> getEventNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Event e : myEvents) {
            names.add(e.getEventName());
        }
        return names;
    }

    /**
     * Gets a list of events that occurred in the past by the time of calendar.
     *
     * @return a list of past events.
     */
    public ArrayList<Event> getPastEvents() {
        ArrayList<Event> events = new ArrayList<>();
        if (myEvents == null || myEvents.isEmpty()) {
            return null;
        } else {
            for (Event event : myEvents) {
                if (event.getEndTime().isBefore(time)) {
                    events.add(event);
                }
            }
            return events;
        }
    }

    /**
     * Gets a list of events currently occurring by the time of calendar.
     *
     * @return a list of current events.
     */
    public ArrayList<Event> getCurrentEvents() {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : myEvents) {
            if (event.getEndTime().isAfter(time) && event.getStartTime().isBefore(time)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Gets a list of events occurring in the future by the time of calendar.
     *
     * @return a list of future events.
     */
    public ArrayList<Event> getFutureEvents() {
        ArrayList<Event> events = new ArrayList<>();
        if (myEvents == null || myEvents.isEmpty()) {
            return null;
        } else {
            for (Event event : myEvents) {
                if (event.getStartTime().isAfter(time)) {
                    events.add(event);
                }
            }
            return events;
        }
    }

    /**
     * Gets an event that is stored in this calendar by specifying its name.
     *
     * @param name: the name of an event; must be the name of an event in this calendar.
     * @return the event with the specified name.
     */
    public Event getEvent(String name) {
        for (Event event : myEvents) {
            if (event.getEventName().equals(name))
                return event;
        }
        return null;
    }

    /**
     * Updates the status of an event as time goes on.
     *
     * @param event an event in this calendar.
     */
    private void updateEventStatus(Event event) {

        LocalDate date1 = time.toLocalDate();

        LocalDate start = event.getStartTime().toLocalDate();
        LocalDate end = event.getEndTime().toLocalDate();
        if (date1.compareTo(start) >= 0 && date1.compareTo(end) <= 0 && !event.getStatus().equals("ongoing")) {
            event.changeStatus("ongoing");
        } else if (date1.compareTo(end) > 0 && !event.getStatus().equals("past")) {
            event.changeStatus("past");
        }
    }

    /**
     * Gets the current time of this calendar.
     *
     * @return a string representation of the current time of this calendar.
     */
    public String getTime() {
        return time.toString();
    }

    /**
     * Gets all the Series associated with an event.
     *
     * @param event an event in this Calendar
     * @return An ArrayList of all the Series <event> is in.
     */
    public ArrayList<Series> getAssociatedSeries(Event event) {
        ArrayList<Series> assocSeries = new ArrayList<>();
        for (Series s : mySeries.getSeries()) {
            if (s.getEvents().contains(event)) {
                assocSeries.add(s);
            }
        }
        return assocSeries;
    }

    /**
     * Adds a new series using the given parameters to this calendar's series system.
     *
     * @param name  the name of the series.
     * @param d     the duration of each event in the series.
     * @param freq  the duration between events in the series.
     * @param num   the number of events in this series.
     * @param first the time and date of the first event in this series.
     */
    public void addSeries(String name, Duration d, Period freq, int num, LocalDateTime first) {
        Collection<Event> newEvents = mySeries.buildSeries(name, d, freq, num, first);
        this.myEvents.addAll(newEvents);
    }

    /**
     * Adds a new series including the specified events.
     *
     * @param name   the name of the series.
     * @param events a list of events in this calendar to be included in the series.
     */
    public void addSeries(String name, ArrayList<Event> events) {
        mySeries.createSeries(name, events);
    }

    /**
     * Gets a collection of the events in the series with the given name.
     *
     * @param name the name of the series.
     * @return a collection of the events in the series with the given name.
     */
    public Collection<Event> findEventsBySeries(String name) {
        return mySeries.findEventsBySeries(name);
    }

    // Methods for getting alerts: all, based on events or current ones

    /**
     * Gets a list of all the alerts in this calendar's alert system.
     *
     * @return a list of all the alerts in this calendar's alert system.
     */
    public ArrayList<Alert> getAllAlerts() {
        return new ArrayList<>(myAlerts.getAllAlerts());
    }

    @Override
    public String toString() {
        String s = "";
        for (Event e : myEvents) {
            s += e.toString() + "\n"; // displays events in this calendar
        }
        if (s.length() != 0) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * Gets the alert system for this calendar.
     *
     * @return the alert system for this calendar.
     */
    public AlertSystem getMyAlerts() {
        return myAlerts;
    }

    /**
     * Adds a notification of an event to this calendar's notification system.
     *
     * @param event an event that was shared with this calendar.
     */
    public void addEventNotification(Event event) {
        this.notifications.add(event);
    }

    /**
     * Gets a list of the event invites that this calendar has received.
     *
     * @return a list of the event invites that this calendar has received.
     */
    public ArrayList<Event> getEventInvites() { return this.notifications; }

    /**
     * Returns the time of the calendar.
     */
    public LocalDateTime getLocalDateTime() {
        return time;
    }
}

