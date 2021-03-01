package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {
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
//    @DisplayName("check if round is over after guessing 5 times")
//    void RoundIsOverGuessedCorrect() {
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
    @Test
    @DisplayName("check if last feedback is last feedback")
    void getLastFeedback() {
        Round round = new Round(new Word("pjooo"));
        //insert guess and add to Feedbacks list

        assertEquals(new Feedback("", new Word("pjooo"), new Hint(List.of('p','.','.','.','.'))).toString(), round.getLastFeedback().toString());
    }
}
