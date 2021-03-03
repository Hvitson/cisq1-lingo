package nl.hu.cisq1.lingo.trainer.domain;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Feedback tests")
class FeedbackTest {

    @Test
    @DisplayName("marks created correctly")
    void marksCreatedCorrectlyWithAttempt() {
        List<Mark> marks = Feedback.createMarks("weeee", new Word("wooon"));
        assertEquals(List.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT), marks);
    }

    @Test
    @DisplayName("marks created without attempt")
    void marksCreatedCorrectlyWithoutAttempt() {
        List<Mark> marks = Feedback.createMarks("", new Word("wooon"));
        assertEquals(List.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT), marks);
    }

    @Test
    @DisplayName("marks created with wrong attempt")
    void marksCreatedCorrectlyWithWrongAttempt() {
        List<Mark> marks = Feedback.createMarks("rr", new Word("wooon"));
        assertEquals(List.of(INVALID, INVALID), marks);
    }

    @Test
    @DisplayName("gets list marks from feedback")
    void getMarks() {
        Feedback feedback = new Feedback("woord", new Word("woord"), new Hint(List.of('w','o','o','r','.')));
        assertEquals(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), feedback.getMarks());
    }

    @Test
    @DisplayName("gets list of marks from feedback containing Invalid because wrong attempt")
    void getInvalidMarks() {
        Feedback feedback = new Feedback("wr", new Word("woord"),
                Hint.playHint(new Hint(List.of('w','o','o','r','.')), List.of(INVALID, INVALID), new Word("woord")));
        assertEquals(List.of(INVALID, INVALID), feedback.getMarks());
    }

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", new Word("woord"), new Hint(List.of('w','o','o','r','d')));
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woort", new Word("woord"), new Hint(List.of('w','o','o','r','.')));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("guess is valid if no marks are invalid")
    void guessIsValid() {
        Feedback feedback = new Feedback("wooor", new Word("woord"), new Hint(List.of('w','o','o','r','.')));
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("guess is valid if no marks are invalid")
    void guessIsInvalid() {
        Feedback feedback = new Feedback("er", new Word("woord"), new Hint(List.of('w','o','o','r','.')));
        assertFalse(feedback.isGuessValid());
    }

    //todo: werkt alleen met toString
    @Test
    @DisplayName("gets hint from feedback")
    void feedbackGetHint() {
        Feedback feedback = new Feedback("er", new Word("woord"), new Hint(List.of('w','o','o','r','.')));
        assertEquals(new Hint(List.of('w','o','o','r','.')).toString(), feedback.getHint().toString());
    }

    //todo: vragen how to get worky werkt alleen met toString
    @Test
    @DisplayName("feedback is created correctly")
    void feedbackIsCorrect() {
        Feedback feedback = new Feedback("woerd", new Word("woord"), new Hint(List.of('w','o','.','r','d')));
        assertTrue(feedback.isGuessValid());
        assertEquals(new Feedback("woerd", new Word("woord"), new Hint(List.of('w','o','.','r','d'))).toString(), feedback.toString());
    }

    @Test
    @DisplayName("feedback toString is created correctly")
    void feedbackToString() {
        Feedback feedback = new Feedback("woerd", new Word("woord"), new Hint(List.of('w','o','.','r','d')));
        System.out.println(feedback);
        assertEquals("Feedback{attempt='woerd', marks=[CORRECT, CORRECT, ABSENT, CORRECT, CORRECT], hint=[w, o, ., r, d]}", feedback.toString());
    }
}