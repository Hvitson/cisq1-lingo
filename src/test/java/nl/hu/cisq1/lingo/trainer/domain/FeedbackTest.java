package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Feedback tests")
class FeedbackTest {

    @Test
    @DisplayName("Create empty Feedback")
    void createEmptyFeedback() {
        Feedback emptyFeedback = new Feedback();
        assertNull(emptyFeedback.getAttempt());
        assertNull(emptyFeedback.getMarks());
    }

    static Stream<Arguments> provideMarksExamples() {
        return Stream.of(
                Arguments.of(
                        "weeee",
                        "woord",
                        List.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT)
                ),
                Arguments.of(
                        "woooo",
                        "woord",
                        List.of(CORRECT, CORRECT, CORRECT, ABSENT, ABSENT)
                ),
                Arguments.of(
                        "wrrrr",
                        "woord",
                        List.of(CORRECT, ABSENT, ABSENT, CORRECT, ABSENT)
                ),
                Arguments.of(
                        "wrroo",
                        "woord",
                        List.of(CORRECT, PRESENT, ABSENT, PRESENT, PRESENT)
                ),
                Arguments.of(
                        "wrroo",
                        "woord",
                        List.of(CORRECT, PRESENT, ABSENT, PRESENT, PRESENT)
                ),
                Arguments.of(
                        "wroor",
                        "woord",
                        List.of(CORRECT, PRESENT, CORRECT, PRESENT, ABSENT)
                ),
                Arguments.of(
                        "",
                        "woord",
                        List.of(INVALID)
                ),
                Arguments.of(
                        "fdsf",
                        "woord",
                        List.of(INVALID)
                ),
                Arguments.of(
                        "gfshfsghsghf",
                        "woord",
                        List.of(INVALID)
                ),
                Arguments.of(
                        null,
                        "woord",
                        List.of(INVALID)
                ),
                Arguments.of(
                        "woord",
                        "woord",
                        List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideMarksExamples")
    @DisplayName("marks created correctly")
    void marksCreatedAbsentAttempt(String guess, String wordToGuess, List<Mark> expectedMarks) {
        List<Mark> marks = Feedback.createMarks(guess, wordToGuess);
        assertEquals(expectedMarks, marks);
    }

    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", "woord");
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woort","woord");
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Guess is invalid if guess length is not same as word length")
    void guessIsInvalid() {
        Feedback feedback = new Feedback("woort","woord");
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("Guess is not invalid if guess length is same as word length")
    void guessIsNotInvalid() {
        Feedback feedback = new Feedback("ds","woord");
        assertFalse(feedback.isGuessValid());
    }

    static Stream<Arguments> provideFeedbacksExamples() {
        Feedback feedback = new Feedback("wonen", "wonen");
        return Stream.of(
                Arguments.of(
                        feedback,
                        feedback,
                        true
                ),
                Arguments.of(
                        new Feedback(
                                "wonen",
                                "wonen"
                        ),
                        new Feedback(
                                "wonen",
                                "wonen"
                        ),
                        true
                ),
                Arguments.of(
                        feedback,
                        new Feedback(
                                "wonen",
                                "wonen"
                        ),
                        true
                ),
                Arguments.of(
                        new Feedback(
                                "wonen",
                                "wanen"
                        ),
                        new Feedback(
                                "wonen",
                                "wonen"
                        ),
                        false
                ),
                Arguments.of(
                        new Feedback(
                                "wanen",
                                "wonen"
                        ),
                        new Feedback(
                                "wonen",
                                "wonen"
                        ),
                        false
                ),
                Arguments.of(
                        new Feedback(
                                "wanen",
                                "wonen"
                        ),
                        null,
                        false
                ),
                Arguments.of(
                        new Feedback(
                                "wonen",
                                "wanen"
                        ),
                        null,
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFeedbacksExamples")
    @DisplayName("Feedback equals and hashcode methods test")
    void feedbackEqualsHashcode(Feedback feedback, Feedback compareFeedback, boolean isEqual) {
        assertEquals(feedback.equals(compareFeedback), isEqual);

        if (compareFeedback != null) {
            assertEquals(Objects.equals(feedback.hashCode(), compareFeedback.hashCode()), isEqual);
        }
    }
}