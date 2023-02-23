package fi.lipp.regexp.model

/**
 * This interface represents a simple IDE editor that has some text and some carets in it
 */
interface Editor {
    /**
     * The text that is opened in your editor
     */
    fun getText(): String

    /**
     * Modern IDEs and Vim allow user to work with multiple carets in the same editor at the same time
     */
    fun getCarets(): Collection<Caret>
}