package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HintTest {
    private Hint hintWith;
    private Hint hintWithout;
    private String wordToGuess;

    @BeforeEach
    void createHints() {
        wordToGuess = "wonen";
        hintWithout = Hint.generateHint(wordToGuess, List.of());
        hintWith = Hint.generateHint(wordToGuess, List.of(
                new Feedback("weeee", wordToGuess),
                new Feedback("weeon", wordToGuess),
                new Feedback("weerr", wordToGuess)
        ));
    }

    @Test
    @DisplayName("Hint is created correctly without feedback")
    void hintCreatedWithoutFeedback() {
        assertEquals(List.of('w','.','.','.','.'), hintWithout);
    }

    @Test
    @DisplayName("Hint is created correctly with feedback")
    void hintCreatedWithFeedback() {
        assertEquals(List.of('w','.','.','e','n'), hintWith);
    }

    @Test
    @DisplayName("Hint hashcode test")
    void hintHashCode() {
        Hint hint1 = new Hint(List.of('w','.','.','.','.'));
        Hint hint2 = new Hint(List.of('w','.','.','e','n'));
        assertEquals(hint1.hashCode(), hintWithout.hashCode());
        assertEquals(hint2.hashCode(), hintWith.hashCode());
    }

    @Test
    @DisplayName("Hint hashcode 0 test")
    void hintHashCode0() {
        if (hintWith.hashCode() == 0) {
            assertFalse(true);
        }
        if (hintWithout.hashCode() == 0) {
            assertFalse(true);
        }
        assertFalse(false);
    }

    @Test
    @DisplayName("Hint equals test")
    void hintEquals() {
        Hint hint = new Hint(List.of('w','.','.','.','.'));
        assertTrue(hint.equals(hintWithout));
    }

    @Test
    @DisplayName("Hint not equals test")
    void hintNotEquals() {
        Hint hint = hintWith;
        assertTrue(hintWith.equals(hint));
    }

    @Test
    @DisplayName("Hint equals different type list test")
    void hintNotEqualsDifferentType() {
        List<Character> hint = List.of('w','.','.','.','.');
        assertFalse(hintWith.equals(hint));
    }

    @Test
    @DisplayName("Hint equals null test")
    void hintNotEqualsNull() {
        Hint hint = null;
        assertFalse(hintWith.equals(hint));
    }

    @Test
    @DisplayName("Hint partially equals test")
    void hintPartiallyEqualsHint() {
        Hint hintCheck = Hint.generateHint(wordToGuess, List.of(
                new Feedback("weeee", "woord"),
                new Feedback("woeon", "woord")
        ));
        assertFalse(hintWith.equals(hintCheck));
    }

    @Test
    @DisplayName("Hint toString test")
    void hintToString() {
        Hint hint = new Hint(List.of('w','.','.','.','.'));
        assertEquals(hint.toString(), hintWithout.toString());
        assertEquals("[w, ., ., e, n]", hintWith.toString());
    }
}
