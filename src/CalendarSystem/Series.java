package CalendarSystem;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a collection of related events.
 *
 * @see Event
 */
public class Series implements Serializable {

    /**
     * The name of this series.
     */
    private String name;

    /**
     * The collection of events in this series.
     */
    private Collection<Event> myEvents;

    /**
     * Initializes a Series object with the specified name and specified events.
     *
     * @param name   the name of the series.
     * @param events the events to be part of the series.
     */
    public Series(String name, Collection<Event> events) {
        this.name = name;
        this.myEvents = events;
    }

    /**
     * Gets the name of this series.
     *
     * @return the name of this series.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets all the events that are part of this series.
     *
     * @return the collection of events that make up this series.
     */
    public Collection<Event> getEvents() {
        return this.myEvents;
    }

    /**
     * Adds the specified event to this series.
     *
     * @param e the event to be added to the series.
     */
    public void addEvent(Event e) {myEvents.add(e);}
}
