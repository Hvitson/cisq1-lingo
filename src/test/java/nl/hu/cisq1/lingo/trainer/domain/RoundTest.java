package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {
    private Round round;
    private Word wordToGuess;

    @BeforeEach
    void beginRound() {
        wordToGuess = new Word("wonen");
        round = new Round(wordToGuess);
    }

    @Test
    @DisplayName("check if round is created it also creates correct hint")
    void creatingHintForRound() {
        Hint hint = round.getHint();
        assertEquals(List.of('w','.','.','.','.'), hint.getChars());
    }

    @Test
    @DisplayName("check if round is not still playable")
    void roundIsPlayable() {
        assertFalse(round.isRoundOver());
    }

    //check if also after guess
    @Test
    @DisplayName("check if round is created it also creates correct hint")
    void creatingRoundCreatesCorrectHint() {
        assertEquals(List.of('w','.','.','.','.'), round.getHint().getChars());

    }

    @Test
    @DisplayName("check if round is over after guessing 5 times")
    void RoundIsOverTooManyGuesses() {
        round.doGuess("wonee");
        round.doGuess("wonee");
        round.doGuess("wonee");
        round.doGuess("wonee");
        round.doGuess("wonee");
        assertTrue(round.isRoundOver());
    }

    @Test
    @DisplayName("test getLastFeedback when feedbacks bigger then 0")
    void roundGetLastFeedbackWhenContains() {
        round.doGuess("hallo");
        assertEquals(new Feedback("hallo", wordToGuess), round.getLastFeedback());
    }

    @Test
    @DisplayName("test getLastFeedback when feedbacks size is 0 throws exception")
    void roundGetLastFeedbackWhenEmpty() {
        assertThrows(
                InvalidFeedbackException.class,
                () -> round.getLastFeedback()
        );
    }

    //nadat doGuess toegevoegd
    @Test
    @DisplayName("check if round is over after guessing the wordToGuess")
    void RoundIsOverIfGuessedCorrect() {
        round.doGuess("wonen");
        assertTrue(round.isRoundOver());
    }

    @Test
    @DisplayName("check if last feedback is conform Feedback")
    void getLastFeedbackFormatCheck() {
        round.doGuess("hallo");
        assertEquals(new Feedback("hallo", wordToGuess), round.getLastFeedback());
    }

    @Test
    @DisplayName("check if last feedback is last feedback when wordToGuess is guessed")
    void getLastFeedbackWhenWordIsGuessed() {
        round.doGuess("wonen");
        assertEquals(new Feedback("wonen", wordToGuess), round.getLastFeedback());
    }

    @Test
    @DisplayName("check if last feedback is last feedback when guess is valid")
    void getLastFeedbackWhenGeussIsValid() {
        round.doGuess("wonee");
        assertEquals(new Feedback("wonee", wordToGuess), round.getLastFeedback());
    }

    @Test
    @DisplayName("check if round keeps giving best hint")
    void getLastFeedbackKeepsGivingBestHint() {
        round.doGuess("546");
        round.doGuess("wonee");
        round.doGuess("weeee");
        round.doGuess("1");
        assertEquals(new Hint(List.of('w', 'o', 'n', 'e', '.')), round.getHint());
    }

    @Test
    @DisplayName("check if last feedback is last feedback when guess is invalid")
    void getLastFeedbackWhenGeussIsInvalid() {
        round.doGuess("sd");

        assertEquals(new Feedback("sd", wordToGuess), round.getLastFeedback());
    }

    @Test
    @DisplayName("Testing testGeuss on a round")
    void doTestGuess() {
        Feedback feedback = new Feedback("schon", wordToGuess);
        List<Feedback> roundFeedbacks = round.getFeedbacks();
        roundFeedbacks.add(feedback);
        round.setGuesses(round.getGuesses() + 1);

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, PRESENT, CORRECT), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','n'), Hint.generateHint(wordToGuess, roundFeedbacks).getChars());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("Testing guess on a round")
    void doGuess() {
        round.doGuess("schon");

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, PRESENT, CORRECT), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','n'), round.getHint().getChars());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("test if hint is best hint")
    void doGuessAndInvalidGuess() {
        System.out.println(round);
        round.doGuess("woeee");
        System.out.println(round);
        round.doGuess("");
        System.out.println(round);

        assertEquals(List.of(INVALID), round.getLastFeedback().getMarks());
        assertEquals(new Hint(List.of('w','o','.','e','.')), round.getHint());
        assertEquals(2, round.getGuesses());
    }

    @Test
    @DisplayName("Testing geuss on a round with invalid guess")
    void doGuessWithInvalidGuess() {
        round.doGuess("df");

        assertEquals(List.of(INVALID, INVALID), round.getLastFeedback().getMarks());
        assertEquals(new Hint(List.of('w','.','.','.','.')), round.getHint());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("checking if cheeto worky")
    void cheetoWorky() {
        round.doGuess("worrr");
        Feedback feedbackVoorCheetoGuess = round.getLastFeedback();
        round.doGuess("iAmChEeTo");
        System.out.println(round.toString());
        assertEquals(0, round.getGuesses());
        assertEquals(new Hint(List.of('w','o','.','.','.')), round.getHint());
    }

    private static Stream<Arguments> provideRoundOverExamples() {
        return Stream.of(
                Arguments.of(new Round(new Word("wonen")), 1, new Feedback("wonen", new Word("wonen"))),
                Arguments.of(new Round(new Word("wonen")), 5, new Feedback("fg", new Word("wonen"))),
                Arguments.of(new Round(new Word("wonen")), 6, new Feedback("fg", new Word("wonen")))
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
    @DisplayName("checks if round toString is correct")
    void getLengthWordToGuess() {
        Round round = new Round(new Word("pjooo"));
        assertEquals(5, round.getLengthWordToGuess());

        Round round1 = new Round(new Word("pjooooo"));
        assertEquals(7, round1.getLengthWordToGuess());
    }

    //hash en equals
    @Test
    @DisplayName("Word hashcode test")
    void roundHashCode() {
        Round roundCheck = new Round(wordToGuess);
        assertEquals(roundCheck.hashCode(), round.hashCode());
    }

    @Test
    @DisplayName("Round hashcode 0 test")
    void roundHashCode0() {
        if (round.hashCode() == 0) {
            assertFalse(true);
        }
        assertFalse(false);
    }

    @Test
    @DisplayName("Round equals test")
    void roundEquals() {
        Round roundCheck = new Round(wordToGuess);
        assertTrue(roundCheck.equals(round));
    }

    @Test
    @DisplayName("Round partially equals test -> wordToGuess different")
    void roundPartiallyEqualsRound() {
        Round roundCheck = new Round(new Word("wooon"));
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round partially equals test -> guesses different")
    void roundPartiallyEqualsRoundGuesses() {
        Round roundCheck = new Round(new Word("wonen"));
        roundCheck.setGuesses(3);
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round partially equals test -> feedbacks different")
    void roundPartiallyEqualsRoundFeedbacks() {
        Round roundCheck = new Round(new Word("wooon"));
        roundCheck.getFeedbacks().add(new Feedback("woerd", new Word("woord")));
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round partially equals test -> hint different")
    void roundPartiallyEqualsRoundHint() {
        Round roundCheck = new Round(new Word("wooon"));
        roundCheck.doGuess("woeee");
        roundCheck.getFeedbacks().remove(roundCheck.getLastFeedback());
        roundCheck.doGuess("iAmChEeTo");
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round not equals test")
    void roundEqualsRoundBySet() {
        Round roundCheck = round;
        assertTrue(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round equals different type list test")
    void hintNotEqualsDifferentType() {
        assertFalse(round.equals(List.of(wordToGuess, 0, List.of(
                new Feedback("we", new Word("woord")),
                new Feedback("woort", new Word("woord"))
                ))));
    }

    @Test
    @DisplayName("Round equals null test")
    void hintNotEqualsNull() {
        Round roundCheck = null;
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round toString is created correctly")
    void roundToString() {
        assertEquals("Round{wordToGuess=Word{value='wonen', length=5}, guesses=0, feedbacks=[]}", round.toString());
    }
}