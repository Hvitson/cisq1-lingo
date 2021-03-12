package nl.hu.cisq1.lingo.trainer.domain;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Feedback tests")
class FeedbackTest {
    private Feedback feedbackCorrect, feedbackInvalid, feedbackValid;
    private String wordToGuess;

    @BeforeEach
    void createFeedbacks() {
        wordToGuess = "woord";
        feedbackCorrect = new Feedback("woord", wordToGuess);
        feedbackInvalid = new Feedback("we", wordToGuess);
        feedbackValid = new Feedback("woort", wordToGuess);
    }

    @Test
    @DisplayName("marks created correctly")
    void marksCreatedAbsentAttempt() {
        List<Mark> marks = Feedback.createMarks("weeee", wordToGuess);
        assertEquals(List.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT), marks);
    }

    @Test
    @DisplayName("marks created correctly")
    void marksCreatedWithPresentAttempt() {
        List<Mark> marks = Feedback.createMarks("wrdoo", wordToGuess);
        assertEquals(List.of(CORRECT, PRESENT, PRESENT, PRESENT, PRESENT), marks);
    }

    @Test
    @DisplayName("marks created without attempt")
    void marksCreatedWithoutAttempt() {
        List<Mark> marks = Feedback.createMarks("", wordToGuess);
        assertEquals(List.of(INVALID), marks);
    }



    @Test
    @DisplayName("marks created with wrong attempt")
    void marksCreatedCorrectlyWithWrongAttempt() {
        List<Mark> marks = Feedback.createMarks("rr", wordToGuess);
        assertEquals(List.of(INVALID, INVALID), marks);
    }

    @Test
    @DisplayName("marks created with attempt null")
    void marksCreatedWithAttemptNull() {
        assertThrows(
                InvalidFeedbackException.class,
                () -> Feedback.createMarks(null, wordToGuess)
        );
    }

    @Test
    @DisplayName("gets list marks from feedback")
    void getMarks() {
        assertEquals(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), feedbackCorrect.getMarks());
    }

    @Test
    @DisplayName("gets list of marks from feedback containing Invalid because wrong attempt")
    void getInvalidMarks() {
        assertEquals(List.of(INVALID, INVALID), feedbackInvalid.getMarks());
    }

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() {
        assertTrue(feedbackCorrect.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        assertFalse(feedbackValid.isWordGuessed());
    }

    @Test
    @DisplayName("guess is valid if no marks are invalid")
    void guessIsValid() {
        assertTrue(feedbackValid.isGuessValid());
    }

    @Test
    @DisplayName("guess is valid if no marks are invalid")
    void guessIsInvalid() {
        assertFalse(feedbackInvalid.isGuessValid());
    }

    //hash en equals
    @Test
    @DisplayName("Feedback hashcode test")
    void feedbackHashCode() {
        Feedback feedback = new Feedback("woort", wordToGuess);
        assertEquals(feedback.hashCode(), feedbackValid.hashCode());
    }

    @Test
    @DisplayName("Feedback hashcode 0 test")
    void feedbackHashCode0() {
        if (feedbackValid.hashCode() == 0) {
            assertFalse(true);
        }
        assertFalse(false);
    }

    @Test
    @DisplayName("Feedback equals test")
    void feedbackEquals() {
        Feedback feedback = new Feedback("woort", wordToGuess);
        System.out.println(feedbackValid);
        assertTrue(feedback.equals(feedbackValid));
    }

    @Test
    @DisplayName("Feedback partially equals test")
    void feedbackPartiallyEqualsAttempt() {
        Feedback feedback = new Feedback("woors", wordToGuess);
        System.out.println(feedbackValid);
        assertFalse(feedback.equals(feedbackValid));
    }

    @Test
    @DisplayName("Feedback partially equals test")
    void feedbackPartiallyEqualsWord() {
        Feedback feedback = new Feedback("woort", "woort");
        System.out.println(feedbackValid);
        assertFalse(feedback.equals(feedbackValid));
    }

    @Test
    @DisplayName("Feedback equals test")
    void feedbackNotEquals() {
        Feedback feedback = feedbackValid;
        assertTrue(feedbackValid.equals(feedback));
    }

    @Test
    @DisplayName("Feedback equals different type list test")
    void hintNotEqualsDifferentType() {
        assertFalse(feedbackValid.equals(List.of("woort", new Word("woord"), List.of(CORRECT, CORRECT, CORRECT, CORRECT, ABSENT))));
    }

    @Test
    @DisplayName("Feedback equals null test")
    void hintNotEqualsNull() {
        Feedback feedback = null;
        assertFalse(feedbackValid.equals(feedback));
    }

    @Test
    @DisplayName("feedback toString is created correctly")
    void feedbackToString() {
        Feedback feedback = new Feedback("woerd", wordToGuess);
        System.out.println(feedback);
        assertEquals("Feedback{attempt='woerd', marks=[CORRECT, CORRECT, ABSENT, CORRECT, CORRECT]}", feedback.toString());
    }
}