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
        assertEquals(List.of(INVALID), marks);
    }

    @Test
    @DisplayName("marks created with wrong attempt")
    void marksCreatedCorrectlyWithWrongAttempt() {
        List<Mark> marks = Feedback.createMarks("rr", new Word("wooon"));
        assertEquals(List.of(INVALID, INVALID), marks);
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
    @DisplayName("guess is invalid if no letters are invalid")
    void guessIsValid() {
        Feedback feedback = new Feedback("woort", new Word("woord"), new Hint(List.of('w','o','o','r','.')));
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("guess is valid if no letters are invalid")
    void feedbackIsCorrect() {
        Feedback feedback = new Feedback("woerd", new Word("woord"), new Hint(List.of('w','o','.','r','d')));
        assertEquals(new Feedback("woerd", new Word("woord"), new Hint(List.of('w','o','.','r','d'))).toString(), feedback.toString());
    }


    //todo: overbodig wordt opgevangen binnen hint? if uit feedback constructor?
    private static Stream<Arguments> provideIncorrectFeedbackExamples() {
        return Stream.of(
                //marks length incorrect
                Arguments.of(new Hint(List.of('w','.','.','.','.')), "wonen", new Word("wee")),
                //hints length incorrect
                Arguments.of(new Hint(List.of('w','.')), "wonen", new Word("wen")),
                //marks and hints length incorrect
                Arguments.of(new Hint(List.of('w','.','.','.','.')), "", new Word("wne")),
                //marks invalid
                Arguments.of(new Hint(List.of('w','.','.','.','.')), "wonen", new Word("wne")),
                Arguments.of(new Hint(List.of('w','.','.','.','.')), "w"    , new Word("woord"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectFeedbackExamples")
    @DisplayName("hint is incorrect when marks are invalid, marks and lastHint not same length as word")
    void feedbackIsNotCorrect(Hint hint, String attempt, Word word) {
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback(attempt, word, hint)
        );
    }
}