package CalendarSystem;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A system that handles the storage and creation of series.
 *
 * @see Series
 */
public class SeriesSystem implements Serializable {

    /**
     * A list of series stored in this series system.
     */
    private ArrayList<Series> mySeries = new ArrayList<>();

    /**
     * Creates a new Series object using the specified name and collection of events and adds
     * it to this series system's stored series.
     *
     * @param name the name of the series.
     * @param ls   a collection of events to be part of the series.
     */
    public void createSeries(String name, Collection<Event> ls) {
        Series s = new Series(name, ls);
        mySeries.add(s);

        for (Event e : ls) {
            e.associateSeries(s);
        }
    }

    public ArrayList<Series> getSeries() {
        return mySeries;
    }

    /**
     * Builds a new Series object using the given parameters, stores
     * it in this series system and returns the collection of events in the series.
     *
     * @param d     the duration of each event in the series.
     * @param freq  duration between each event.
     * @param num   the number of events in the series.
     * @param first the dateTime of the first event in the series.
     */
    public Collection<Event> buildSeries(String name, Duration d, Period freq, int num, LocalDateTime first) {
        ArrayList<Event> newEvents = new ArrayList<>();
        newEvents.add(new Event(name + ": Event 1", first, first.plus(d)));
        for (int i = 2; i <= num; i++) {
            first = first.plus(freq);
            newEvents.add(new Event(name + ": Event " + i, first, first.plus(d)));
        }
        createSeries(name, newEvents);
        return newEvents;
    }

    /**
     * Adds the specified event to the specified series.
     *
     * @param series a series in this series system.
     * @param e      an event to be added to the series.
     */
    public void addEvent(Series series, Event e) {
        series.addEvent(e);
        e.associateSeries(series);
    }


    /**
     * Gets a collection of events that are part of the specified series.
     *
     * @param seriesName the name of the series to search for.
     * @return the events in the series if they exist, otherwise an empty list.
     */
    public Collection<Event> findEventsBySeries(String seriesName) {
        for (Series s : this.mySeries) {
            if (s.getName().equals(seriesName)) {
                return s.getEvents();
            }
        }
        return new ArrayList<>();
    }
}