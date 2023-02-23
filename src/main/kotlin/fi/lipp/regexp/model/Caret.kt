package fi.lipp.regexp.model

/**
 * This interface represents a simple IDE caret
 */
interface Caret {
    /**
     * Returns caret's position as offset in text
     */
    fun getCaretOffset(): Int
}