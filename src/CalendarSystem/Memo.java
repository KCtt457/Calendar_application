package CalendarSystem;

import java.io.Serializable;

/**
 * Represents a memo.
 * A memo contains a note and can be associated with one or multiple events.
 *
 * @see Event
 */
public class Memo implements Serializable {

    /**
     * The id number for this memo.
     */
    private int idNum;

    /**
     * The total number of memos created.
     */
    private static int numOfMemos = 0;

    /**
     * The message that this memo stores.
     */
    private String note;

    /**
     * Initializes a new Memo object containing the specified note.
     *
     * @param note the message to be stored in this memo.
     */
    public Memo(String note) {
        numOfMemos++; // updates the total number of memos created
        this.idNum = numOfMemos;
        this.note = note;
    }

    /**
     * Gets the id number of this memo.
     *
     * @return this memo's id number.
     */
    public int getIdNumber() {
        return this.idNum;
    }

    /**
     * Gets the note stored in this memo.
     *
     * @return this memo's note.
     */
    public String getNote() {
        return this.note;
    }

    // Methods for editing this memo

    /**
     * Changes this memo's note to the specified new note.
     *
     * @param newNote the new note to be stored in this memo.
     */
    public void setNote(String newNote) {
        this.note = newNote;
    }

    @Override
    public String toString() {
        return "Memo " + this.idNum + ": " + this.note;
    }

    /**
     * Gets the total number of memos created.
     *
     * @return the total number of memos created.
     */
    static public int getNumOfMemos() {
        return numOfMemos;
    }

}