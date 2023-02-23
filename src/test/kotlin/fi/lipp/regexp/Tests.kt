package fi.lipp.regexp

import fi.lipp.regexp.service.RegexpServiceImpl
import fi.lipp.regexp.model.Caret
import fi.lipp.regexp.model.Editor
import fi.lipp.regexp.model.InvalidPatternException
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class Tests {
    @Test
    fun `non-ascii character`() {
        assertInvalidPatternException("\uD83D\uDE43")
    }    
    
    @Test
    fun `unmatched group brace`() {
        assertInvalidPatternException("(group")
    }

    @Test
    fun `word plus group with unmatched brace`() {
        assertInvalidPatternException("word(group")
    }

    @Test
    fun `unmatched closing brace`() {
        assertInvalidPatternException("group)")
    }

    @Test
    fun `unmatched closing brace plus word`() {
        assertInvalidPatternException("group)word")
    }

    @Test
    fun `closing brace at start`() {
        assertInvalidPatternException(")word")
    }
    
    @Test
    fun `empty group`() {
        assertInvalidPatternException("()")
    }

    @Test
    fun `group plus modificator without group`() {
        assertInvalidPatternException("word+")
    }

    @Test
    fun `group plus modificator without group 2`() {
        assertInvalidPatternException("+word")
    }

    @Test
    fun `group plus modificator without group 3`() {
        assertInvalidPatternException("wor+d")
    }

    @Test
    fun `group plus modificator before group`() {
        assertInvalidPatternException("+(group)")
    }

    @Test
    fun `group plus modificator after group and word`() {
        assertInvalidPatternException("(group)x+")
    }

    @Test
    fun `group plus modificator after group and caret token`() {
        assertInvalidPatternException("(group)\\c+")
    }

    @Test
    fun `invalid escaped character`() {
        assertInvalidPatternException("(group)\\d")
    }

    @Test
    fun `invalid escaped character 2`() {
        assertInvalidPatternException("\\dword")
    }

    @Test
    fun `open escape at the end`() {
        assertInvalidPatternException("word\\")
    }

    @Test
    fun `escaped opening bracket`() {
        assertInvalidPatternException("\\(group)")
    }

    @Test
    fun `escaped question mark parsed correctly`() {
        doTest(
            """1. Who is your hero?
               2. If you could live anywhere, where would it be?
               3. What is your biggest fear?""".trimIndent(),
            "\\?",
            setOf(Pair(19, 20), Pair(84, 85), Pair(129, 130))
            )
    }
    
    @Test
    fun `escaped asterisk is parsed correctly`() {
        doTest(
            "In the 7-bit ASCII character set, ASCII code 42 is represented by the character * also known as the asterisk.",
            "\\*",
            setOf(Pair(80, 81))
        )
    }

    @Test
    fun `escaped plus is parsed correctly`() {
        doTest(
            "2 + 2 * 2 + 6 + 8 = ?",
            "\\+",
            setOf(Pair(2, 3), Pair(10, 11), Pair(14, 15))
        )
    }
    
    @Test
    fun `match word at start`() {
        doTest(
            "IdeaVim is a Vim engine for JetBrains IDEs",
            "IdeaVim",
            setOf(Pair(0, 7))
        )
    }

    @Test 
    fun `match word at the end`() {
        doTest(
            "IdeaVim is a Vim engine for JetBrains IDEs",
            "IDEs",
            setOf(Pair(38, 42))
        )
    }

    @Test
    fun `match word`() {
        doTest(
            "IdeaVim is a Vim engine for JetBrains IDEs",
            "Vim",
            setOf(Pair(4, 7), Pair(13, 16))
        )
    }
    
    @Test
    fun noignorecase() {
        doTest(
            "IdeaVim ideavim IDEAVIM",
            "IdeaVim",
            setOf(Pair(0, 7))
        )
    }
    
    @Test
    fun `word with no matches`() {
        doTest(
            "Dog, dog, dog.. Cat!",
            "spider",
            emptySet()
        )
    }
    
    @Test
    fun `pattern that matches any offset`() {
        val text = "Oh, hi Mark"
        doTest(
            text,
            "(Vim)?",
            (0 .. text.length).map { Pair(it, it) }.toSet()
        )
    }

    @Test
    fun `pattern that matches any offset 2`() {
        val text = "Oh, hi Mark"
        doTest(
            text,
            "(Vim)*",
            (0 .. text.length).map { Pair(it, it) }.toSet()
        )
    }
    
    @Test
    fun `match group with no modificator`() {
        doTest(
            "IdeaVim is a Vim engine for JetBrains IDEs",
            "(Vim)",
            setOf(Pair(4, 7), Pair(13, 16))
        )
    }

    @Test
    fun `match with inner groups`() {
        doTest(
            "IdeaVim a Vim engine for JetBrains IDESs.",
            "(IdeaVim (is )?(a (Vim)(.)?))",
            setOf(Pair(0, 13))
        )
    }

    @Test
    fun `match inner group madness`() {
        doTest(
            "IdeaVim is a Vim engine for JetBrains IDESs",
            "(I(d(e(a(V(i(m)))))))",
            setOf(Pair(0, 7))
        )
    }

    @Test
    fun `match group with multiple occurrences`() {
        doTest(
            "Vim, Vim, Nano, IdeaVim",
            "(Vim(,)?)",
            setOf(Pair(0, 3), Pair(0, 4), Pair(5, 8), Pair(5, 9), Pair(20, 23))
        )
    }

    @Test
    fun `match group and word`() {
        doTest(
            "1, 1,, 1, 1,,, 1",
            "1(,)*",
            setOf(Pair(0, 1), Pair(0, 2), Pair(3, 4), Pair(3, 5), Pair(3, 6), Pair(7, 8), Pair(7, 9), Pair(10, 11), Pair(10, 12), Pair(10, 13), Pair(10, 14), Pair(15, 16))
        )
    }

    @Test
    fun `match group and word 2`() {
        doTest(
            "1, 1,, 1, 1,,, 1",
            "1(,)+",
            setOf(Pair(0, 2), Pair(3, 5), Pair(3, 6), Pair(7, 9), Pair(10, 12), Pair(10, 13), Pair(10, 14))
        )
    }
    
    @Test
    fun `match word and group`() {
        doTest(
            "Ho-ho-ho",
            "Ho(-ho)*",
            setOf(Pair(0, 2), Pair(0, 5), Pair(0, 8))
        )
    }

    @Test
    fun `test occurrence of group token plus word token with caret`() {
        val result = RegexpServiceImpl().matchAll(buildEditor("Live one!two life, use one!!two IdeaVim", listOf(6)), "o\\cne(!)*two")
        assertEquals(setOf(Pair(5, 12)), result)
    }
    
    private fun doTest(text: String, pattern: String, expectedMatches: Set<Pair<Int, Int>>, caretOffsets: Collection<Int> = emptyList()) {
        val editor = buildEditor(text, caretOffsets)
        val actualMatches = RegexpServiceImpl().matchAll(editor, pattern)
        assertEquals(expectedMatches, actualMatches)
    }
    
    private fun assertInvalidPatternException(pattern: String) {
        try {
            val editor = buildEditor("", emptyList())
            RegexpServiceImpl().matchAll(editor, pattern)
        } catch (exception: InvalidPatternException) {
            return
        }
        fail("Pattern parsing should fail with " + InvalidPatternException::class.java.name)
    }
    
    private fun buildEditor(text: String, caretOffsets: Collection<Int>): Editor {
        return object : Editor {
            override fun getText(): String = text

            override fun getCarets(): Collection<Caret> {
                return caretOffsets.map { offset -> object : Caret {
                    override fun getCaretOffset(): Int = offset
                } }
            }
        }
    }
}