package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {
    private Round round;

    @BeforeEach
    void beginRound() {
        round = new Round(new Word("wonen"));
    }



    //todo: ook alleen toString()
    @Test
    @DisplayName("check if round is created it also creates correct hint")
    void creatingRoundCreatesCorrectHint() {
        assertEquals(new Hint(List.of('w','.','.','.','.')).toString(), round.getLastFeedback().getHint().toString());
    }


    @Test
    @DisplayName("check if round is not still playable")
    void roundIsPlayable() {
        assertFalse(round.isRoundOver());
    }

    @Test
    @DisplayName("check if round is over after guessing 5 times")
    void RoundIsOverTooManyGuesses() {
        round.setGuesses(5);
        assertTrue(round.isRoundOver());
    }

    //todo: fix
//    @Test
//    @DisplayName("check if round is over after guessing the wordToGuess")
//    void RoundIsOverIfGuessedCorrect() {
//        Round round = new Round(new Word("pjooo"));
//        //insert guess dat het woord raad
//
//        assertTrue(round.isRoundOver());
//    }

    //todo: waarom zonder toString() no worky
    @Test
    @DisplayName("check if last feedback is conform Feedback")
    void getLastFeedbackFormatCheck() {
        assertEquals(new Feedback("", new Word("wonen"), new Hint(List.of('w','.','.','.','.'))).toString(), round.getLastFeedback().toString());
    }

    @Test
    @DisplayName("check if last feedback is last feedback when wordToGuess is guessed")
    void getLastFeedbackWhenWordIsGuessed() {
        round.doGuess("wonen");
        assertEquals(new Feedback("wonen", new Word("wonen"), new Hint(List.of('w','o','n','e','n'))).toString(), round.getLastFeedback().toString());
    }

    @Test
    @DisplayName("check if last feedback is last feedback when guess is valid")
    void getLastFeedbackWhenGeussIsValid() {
        round.doGuess("wonee");
        assertEquals(new Feedback("wonee", new Word("wonen"), new Hint(List.of('w','o','n','e','.'))).toString(), round.getLastFeedback().toString());
    }

    @Test
    @DisplayName("check if last feedback is last feedback when guess is invalid")
    void getLastFeedbackWhenGeussIsInvalid() {
        round.doGuess("sd");

        assertEquals(new Feedback("sd", new Word("wonen"), new Hint(List.of('w','.','.','.','.'))).toString(), round.getLastFeedback().toString());
    }

    //overbodig?
    @Test
    @DisplayName("Testing testGeuss on a round")
    void doTestGuess() {
        Feedback feedback = new Feedback("schon", new Word("wonen"), new Hint(List.of('w','.','.','.','.')));
        List<Feedback> roundFeedbacks = round.getFeedbacks();
        roundFeedbacks.add(feedback);
        round.setGuesses(round.getGuesses() + 1);

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, PRESENT, CORRECT), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','n'), round.getLastFeedback().getHint().getHint());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("Testing geuss on a round")
    void doGuess() {
        round.doGuess("schon");

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, PRESENT, CORRECT), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','n'), round.getLastFeedback().getHint().getHint());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("Testing geuss on a round with invalid guess")
    void doGuessWithInvalidGuess() {
        round.doGuess("df");

        assertEquals(List.of(INVALID, INVALID), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','.'), round.getLastFeedback().getHint().getHint());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("checking if cheeto worky")
    void cheetoWorky() {
        round.doGuess("df");
        Feedback feedbackVoorCheetoGuess = round.getLastFeedback();
        round.doGuess("iAmChEeTo");
        System.out.println(round.toString());
        assertEquals(0, round.getGuesses());
        assertEquals(List.of('w','.','.','.','.'), feedbackVoorCheetoGuess.getHint().getHint());
    }

    private static Stream<Arguments> provideRoundOverExamples() {
        return Stream.of(
                Arguments.of(new Round(new Word("wonen")), 1, new Feedback("wonen", new Word("wonen"), new Hint(List.of('w','.','.','.','.')))),
                Arguments.of(new Round(new Word("wonen")), 5, new Feedback("fg", new Word("wonen"), new Hint(List.of('w','.','.','.','.')))),
                Arguments.of(new Round(new Word("wonen")), 6, new Feedback("fg", new Word("wonen"), new Hint(List.of('w','.','.','.','.'))))
        );
    }

    //this oki?
    @ParameterizedTest
    @MethodSource("provideRoundOverExamples")
    @DisplayName("Testing geuss on a round that is not playable")
    void doGuessWhenRoundIsOver(Round round, Integer guesses, Feedback feedback) {
        round.setGuesses(guesses);
        round.getFeedbacks().add(feedback);
        assertThrows(
                InvalidRoundException.class,
                () -> round.doGuess("bababooey")
        );
    }

    @Test
    @DisplayName("checks if rounds last feedback toString is correct")
    void getFeedbackToString() {
        Round round = new Round(new Word("pjooo"));
        assertEquals("Feedback{attempt='', marks=[CORRECT, ABSENT, ABSENT, ABSENT, ABSENT], hint=[p, ., ., ., .]}", round.getLastFeedback().toString());
    }

    @Test
    @DisplayName("checks if round toString is correct")
    void getRoundToString() {
        Round round = new Round(new Word("pjooo"));
        assertEquals("Round{wordToGuess=Word{value='pjooo', length=5}, guesses=0, feedbacks=[Feedback{attempt='', marks=[CORRECT, ABSENT, ABSENT, ABSENT, ABSENT], hint=[p, ., ., ., .]}]}", round.toString());
    }

    @Test
    @DisplayName("checks if round toString is correct")
    void getLengthWordToGuess() {
        Round round = new Round(new Word("pjooo"));
        assertEquals(5, round.getLengthWordToGuess());

        Round round1 = new Round(new Word("pjooooo"));
        assertEquals(7, round1.getLengthWordToGuess());
    }
}
