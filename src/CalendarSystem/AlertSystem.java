package CalendarSystem;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.NavigableSet;

/**
 * Represents a system for managing alerts.
 * The AlertSystem class handles creating, editing, deleting alerts, along with other alert functions.
 *
 * @see Alert
 */
public class AlertSystem implements Serializable {
    /**
     * A set containing all the times when alerts should appear.
     */
    private TreeSet<LocalDateTime> allAlertTimes = new TreeSet<>();

    /**
     * A map mapping each appear time to a list of alerts that should show up at the appear time.
     */
    private Map<LocalDateTime, List<Alert>> dateAlertsMap = new HashMap<>();

    /**
     * A map mapping events to their associated alerts.
     */
    private Map<Event, List<Alert>> eventAlertsMap = new HashMap<>();

    //Add alerts to the system

    /**
     * Creates and adds a new IndividualAlert object to this alert system using the specified parameters.
     *
     * @param event   the event which the alert is associated with.
     * @param time    time of the alert.
     * @param message message content of the alert.
     */
    public void addIndividualAlert(Event event, String message, LocalDateTime time) {
        Alert newAlert = new IndividualAlert(event.getStartTime(), event.getEventName(), message, time);
        eventAlertsMap.putIfAbsent(event, new ArrayList<>());
        eventAlertsMap.get(event).add(newAlert);
        addtoTimesSet(newAlert);
    }

    /**
     * Adds a new FrequentAlert object to the system; does nothing if the frequent alert date is not valid
     * (ex. no valid alert time with the given frequency).
     *
     * @param event    the event which the alert is associated with.
     * @param duration frequency of the alert.
     * @param message  message content of the alert.
     */
    public void addFrequentAlert(Event event, String message, Duration duration) {
        if (FrequentAlert.isValidFrequentAlert(event.getStartTime(), LocalDateTime.now(), duration)) {
            Alert newAlert = new FrequentAlert(event.getStartTime(), event.getEventName(), message, duration);
            eventAlertsMap.putIfAbsent(event, new ArrayList<>());
            eventAlertsMap.get(event).add(newAlert);
            addtoTimesSet(newAlert);
        } else {
            System.out.println("Illegal Frequent Alert Time");
        }
    }


    //Return sets of alerts -> that should appear currently, all alerts, or according to event

    /**
     * Gets all alerts associated with the specified event.
     *
     * @param e events
     */
    public Set<Alert> getAlerts(Event e) {
        return new HashSet<>(eventAlertsMap.get(e));
    }

    /**
     * Gets a list of upcoming alerts.
     *
     * @return upcoming alerts
     */
    public Set<Alert> getCurrAlerts() {
        Set<Alert> CurrAlerts = new HashSet<>(); //the set of Alerts to Show

        // subset of valid times, reverse order
        NavigableSet<LocalDateTime> subset = this.allAlertTimes.tailSet(LocalDateTime.now(), true);
        NavigableSet<LocalDateTime> validAlertTimes = subset.descendingSet();

        //check if alert is valid (i.e. event hasn't occurred), then add to set CurrAlerts
        for (LocalDateTime date : validAlertTimes) {
            for (Alert alert : dateAlertsMap.get(date)) {
                if (alert.getEventTime().isAfter(LocalDateTime.now()))
                    CurrAlerts.add(alert);
            }
        }

        return CurrAlerts;
    }

    /**
     * Gets a list of alert times for upcoming alerts.
     *
     * @return list of the alert times.
     */
    public Set<LocalDateTime> getCurrAlertTime() {
        // subset of valid times, reverse order
        NavigableSet<LocalDateTime> subset = this.allAlertTimes.tailSet(LocalDateTime.now(), true);
        return subset.descendingSet();
    }

    /**
     * Gets a list of all alerts in the system - past, current and future.
     *
     * @return list of all alerts.
     */
    public Set<Alert> getAllAlerts() {
        Set<Alert> result = new HashSet<>();
        Set<Event> events = eventAlertsMap.keySet();
        for (Event e : events) {
            result.addAll(eventAlertsMap.get(e));
        }
        return result;
    }


    //methods to edit or delete alerts

    /**
     * Removes the specified event from this eventAlertsMap.
     */
    public void removeEvent(Event e) {
        this.eventAlertsMap.remove(e);
    }

    /**
     * Deletes the specified alert from this alert system.
     */
    public void deleteAlert(Alert alert) {
        removeFromDateAlertsMap(alert);

        // removes alert from eventAlertsMap
        for (Event e : eventAlertsMap.keySet()) {
            eventAlertsMap.get(e).remove(alert);
        }
    }

    /**
     * Removes all the alerts for the specified event.
     *
     * @param e an event which is associated with alerts in this alert system.
     */
    public void deleteAllAlertsforEvent(Event e) {
        if (!this.eventAlertsMap.isEmpty()) {
            List<Alert> copy = new ArrayList<>(eventAlertsMap.get(e));
            for (Alert a : copy) {
                deleteAlert(a);
            }
            removeEvent(e);
        }
    }

    /**
     * Edits the message and frequency of the specified frequent alert.
     *
     * @param alert     a frequent alert in this system.
     * @param message   message content of the alert.
     * @param frequency frequency of the alert.
     */
    public void editFrequentAlert(FrequentAlert alert, String message, Duration frequency) {
        removeFromDateAlertsMap(alert);
        alert.changeFrequency(frequency);
        alert.setMessage(message);
        addtoTimesSet(alert);
        alert.setData(alert.toString());
    }

    /**
     * Edits the message and time of the specified individual alert.
     *
     * @param alert    an individual alert in this alert system.
     * @param message  the message content of the alert.
     * @param dateTime the time of the alert.
     */
    public void editIndividualAlert(IndividualAlert alert, String message, LocalDateTime dateTime) {
        removeFromDateAlertsMap(alert);
        alert.setMessage(message);
        alert.changeTime(dateTime);
        addtoTimesSet(alert);
        alert.setData(alert.toString());
    }

    // helper methods

    /**
     * A helper method for adding the times of the alert to dateAlertsMap and allAlertTimes.
     *
     * @param alert: the alert to add.
     */
    private void addtoTimesSet(Alert alert) {
        for (LocalDateTime alertTime : alert.getTimes()) {
            dateAlertsMap.putIfAbsent(alertTime, new ArrayList<>());
            dateAlertsMap.get(alertTime).add(alert);
            allAlertTimes.add(alertTime);
        }
    }

    /**
     * Removes the specified alert from this alert system's dateAlertsMap.
     *
     * @param alert an alert in this alert system.
     */
    private void removeFromDateAlertsMap(Alert alert) {
        // removes alert from dateAlertsMap
        for (LocalDateTime alertTime : alert.getTimes()) {
            dateAlertsMap.get(alertTime).remove(alert);
            if (dateAlertsMap.get(alertTime).isEmpty()) { // no other alerts at alertTime, so removes alertTime
                dateAlertsMap.remove(alertTime);
                allAlertTimes.remove(alertTime);
            }
        }
    }


    //getters and setters

    /**
     * Gets the dateAlertsMap for this alert system.
     *
     * @return the dateAlertsMap in this alert system.
     */
    public Map<LocalDateTime, List<Alert>> getDateAlertsMap() {
        return dateAlertsMap;
    }

    /**
     * Gets a TreeSet of all the alert times in this alert system.
     *
     * @return the alert times in this alert system.
     */
    public TreeSet<LocalDateTime> getAllAlertTimes() {
        return allAlertTimes;
    }

    public Map<Event, List<Alert>> getEventAlertsMap() {
        return eventAlertsMap;
    }
}
