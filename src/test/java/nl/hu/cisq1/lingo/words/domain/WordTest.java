package nl.hu.cisq1.lingo.words.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class WordTest {
    private Word word;

    @BeforeEach
    void createWord() {
        word = new Word("woord");
    }

    @Test
    @DisplayName("test empty word constructor")
    void emptyWordConstructor() {
        Word leeg = new Word();
        assertEquals(null, leeg.getLength());
        assertEquals(null, leeg.getValue());
    }

    @Test
    @DisplayName("length is based on given word")
    void lengthBasedOnWord() {
        assertEquals(5, word.getLength());
    }

    @Test
    @DisplayName("length is based on given word")
    void valueBasedOnWord() {
        assertEquals("woord", word.getValue());
    }

    //hashcode, equals & toString
    @Test
    @DisplayName("Word hashcode test")
    void wordHashCode() {
        Word wordToGuess = new Word("woord");
        assertEquals(wordToGuess.hashCode(), word.hashCode());
    }

    @Test
    @DisplayName("Word hashcode 0 test")
    void wordHashCode0() {
        if (word.hashCode() == 0) {
            assertFalse(true);
        }
        assertFalse(false);
    }

    @Test
    @DisplayName("Word equals test")
    void wordEquals() {
        Word wordToGuess = new Word("woord");
        assertTrue(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word equals test afterset")
    void wordEqualsAfterSet() {
        Word wordToGuess = word;
        assertTrue(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word not equals test")
    void wordNotEquals() {
        Word wordToGuess = new Word("worrd");
        assertFalse(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word equals different type test")
    void wordNotEqualsDifferentType() {
        String wordToGuess = "woord";
        assertFalse(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word equals null test")
    void wordNotEqualsNull() {
        Word wordToGuess = null;
        assertFalse(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word partially equals test -> length different")
    void wordPartiallyEqualsLength() {
        Word wordToGuess = new Word("woord");

        wordToGuess.setLength(6);
        assertFalse(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word partially equals test -> value different")
    void wordPartiallyEqualsValue() {
        Word wordToGuess = new Word("woord");

        wordToGuess.setValue("woerd");
        assertFalse(word.equals(wordToGuess));
    }

    @Test
    @DisplayName("Word toString is created correctly")
    void wordToString() {
        assertEquals("Word{value='woord', length=5}", word.toString());
    }
}
