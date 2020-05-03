package CalendarSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A system for handling the creation and storage of Memos.
 *
 * @see Memo
 */
public class MemoSystem implements Serializable {

    private static final long serialVersionUID = 7263156044109039531L;

    /**
     * A list of stored memos.
     */
    public ArrayList<Memo> memos;

    /**
     * Initializes a new MemoSystem object with no memos.
     */
    public MemoSystem() {
        this.memos = new ArrayList<>();
    }

    /**
     * Creates a memo associated with the specified events, and containing the specified note.
     *
     * @param events a list of events associated with the memo.
     * @param note   the message to be stored in the memo.
     */
    public void createMemo(List<Event> events, String note) {
        // initialize a new Memo object with specified note
        Memo memo = new Memo(note);

        // adds memo to the list of memos stored in this memo system
        this.memos.add(memo);

        // adds memo to each event's memos from the input list of events
        for (Event event : events) {
            event.getMemos().add(memo);
        }
    }

    /**
     * Gets a list of all the memos stored in this memo system.
     *
     * @return the list of stored memos.
     */
    public ArrayList<Memo> getMemos() {
        return memos;
    }

    /**
     * Gets a memo by its id number. If there is no memo in this memo system
     * with this id number, returns <code>null</code>.
     *
     * @param id the id number of a memo in this memo system.
     * @return the memo with the specified id number.
     */
    public Memo getMemo(int id) {
        for (Memo memo : memos) {
            if (memo.getIdNumber() == id) {
                return memo;
            }
        }
        System.out.println("This is not a valid id number for a memo.");
        return null;
    }

    /**
     * Removes the specified memo from the stored memos, if it exists.
     *
     * @param memo a memo stored in this memo system.
     */
    public void deleteMemo(Memo memo) {
        if (this.memos.contains(memo)) {
            this.memos.remove(memo);
            System.out.println("Memo deleted");
        } else {
            System.out.println("Entered invalid Memo"); // memo not in this memo system
        }
    }

    /**
     * Returns true if this memo system contains memos.
     *
     * @return true if there is at least one stored memo, otherwise false
     */
    public boolean isEmpty() {
        if (memos == null) {
            return false;
        } else {
            return memos.isEmpty();
        }
    }
}