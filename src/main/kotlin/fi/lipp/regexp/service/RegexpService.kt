package fi.lipp.regexp.service

import fi.lipp.regexp.model.Editor
import fi.lipp.regexp.model.InvalidPatternException
import kotlin.jvm.Throws

interface RegexpService {
    /**
     * Returns indexes of all substrings that match the given pattern in the given editor
     */
    @Throws(InvalidPatternException::class)
    fun matchAll(editor: Editor, pattern: String): Set<Pair<Int, Int>>
}