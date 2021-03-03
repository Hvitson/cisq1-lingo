package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
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

    //todo: ook alleen toString()
    @Test
    @DisplayName("check if round is created it also creates correct hint")
    void creatingRoundCreatesCorrectHint() {
        Round round = new Round(new Word("hallo"));
        assertEquals(new Hint(List.of('h','.','.','.','.')).toString(), round.getLastFeedback().getHint().toString());
    }


    @Test
    @DisplayName("check if round is not still playable")
    void roundIsPlayable() {
        Round round = new Round(new Word("pjooo"));

        assertFalse(round.isRoundOver());
    }

    @Test
    @DisplayName("check if round is over after guessing 5 times")
    void RoundIsOverTooManyGuesses() {
        Round round = new Round(new Word("pjooo"));
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
        Round round = new Round(new Word("pjooo"));
        assertEquals(new Feedback("", new Word("pjooo"), new Hint(List.of('p','.','.','.','.'))).toString(), round.getLastFeedback().toString());
    }

    //todo: waarom zonder toString() no worky
//    @Test
//    @DisplayName("check if last feedback is last feedback when wordToGuess is guessed")
//    void getLastFeedbackWhenWordIsGuessed() {
//        Round round = new Round(new Word("pjooo"));
//        //insert guess and add to Feedbacks list + guess is wordToGuess
//
//        assertEquals(new Feedback("", new Word("pjooo"), new Hint(List.of('p','.','.','.','.'))).toString(), round.getLastFeedback().toString());
//    }
//
//    @Test
//    @DisplayName("check if last feedback is last feedback when guess is valid")
//    void getLastFeedbackWhenGeussIsValid() {
//        Round round = new Round(new Word("pjooo"));
//        //insert guess and add to Feedbacks list + valid guess
//        //isGuessValid()? <- moet correct
//        assertEquals(new Feedback("", new Word("pjooo"), new Hint(List.of('p','.','.','.','.'))).toString(), round.getLastFeedback().toString());
//    }
//
//    @Test
//    @DisplayName("check if last feedback is last feedback when guess is invalid")
//    void getLastFeedbackWhenGeussIsInvalid() {
//        Round round = new Round(new Word("pjooo"));
//        //insert guess and add to Feedbacks list + invalid guess
//        //isGuessValid()? <- moet false
//
//        assertEquals(new Feedback("", new Word("pjooo"), new Hint(List.of('p','.','.','.','.'))).toString(), round.getLastFeedback().toString());
//    }

    //todo: werkt alleen met toString()
    @Test
    @DisplayName("Testing testGeuss on a round")
    void doTestGuess() {
        Round round = new Round(new Word("wonen"));
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
    void doActualGuess() {
        Round round = new Round(new Word("wonen"));
        round.doGuess("schon");

        System.out.println(round.getLastFeedback().getMarks());
        System.out.println(round.getLastFeedback().getHint());

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, PRESENT, CORRECT), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','n'), round.getLastFeedback().getHint().getHint());
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
}
