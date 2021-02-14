package nl.hu.cisq1.lingo.trainer.domain;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Feedback tests")
class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT));
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woord", List.of(PRESENT, CORRECT, CORRECT, CORRECT, CORRECT));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("guess is invalid if no letters are invalid")
    void guessIsValid() {
        Feedback feedback = new Feedback("woord", List.of(PRESENT, CORRECT, CORRECT, CORRECT, CORRECT));
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("guess is invalid if ALL letter is invalid")
    void guessIsInvalid() {
        Feedback feedback = new Feedback("woord", List.of(INVALID,INVALID,INVALID,INVALID));
        assertTrue(feedback.isGuessInvalid());
    }

}