package model;

import java.util.*;

/**
 * All Events in A family tree of an individual
 */
public class AllEvents {
    private List<Event> famEvents;

    /**
     * Constructs all events of Family tree and initializes the Array
     */
    public AllEvents() {
        famEvents = new ArrayList<Event>();
    }

    public void addEvent(Event newMember) {
        famEvents.add(newMember);
    }

    public Event[] getArray() {
        Event[] familyEvents = new Event[famEvents.size()];
        familyEvents = famEvents.toArray(familyEvents);
        return familyEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AllEvents)) return false;
        AllEvents allEvents = (AllEvents) o;
        return famEvents.equals(allEvents.famEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(famEvents);
    }
}
