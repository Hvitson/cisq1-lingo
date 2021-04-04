package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.state.StateRound;
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
import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;

public class RoundTest {
    private Round round;
    private String wordToGuess;

    @BeforeEach
    void beginRound() {
        wordToGuess = "wonen";
        round = new Round(wordToGuess);
    }

    @Test
    @DisplayName("Create empty Round")
    void createEmptyFeedback() {
        Round emptyRound = new Round();
        assertEquals(null, emptyRound.getRoundId());
        assertEquals(null, emptyRound.getWordToGuess());
        assertEquals(null, emptyRound.getGuesses());
        assertEquals(null, emptyRound.getFeedbacks());
    }

    @Test
    @DisplayName("check if round is created it also creates correct hint")
    void creatingHintForRound() {
        List<Character> hint = round.getHint();
        assertEquals(List.of('w','.','.','.','.'), hint);
    }

    @Test
    @DisplayName("check if StateRound is 'WAITING_FOR_INPUT' after round is created")
    void roundIsPlayable() {
        assertTrue(round.getState() == WAITING_FOR_INPUT);
        assertTrue(round.getState() != WON);
        assertTrue(round.getState() != LOST);
    }

    @Test
    @DisplayName("check if StateRound is 'WAITING_FOR_INPUT' after word not guessed en guesses are lower than 5")
    void roundIsPlayableAfterGuessesingLessThanFiveTimes() {
        round.doGuess("jooow");
        assertEquals(1, round.getGuesses());
        assertTrue(round.getState() == WAITING_FOR_INPUT);
        assertTrue(round.getState() != WON);
        assertTrue(round.getState() != LOST);
    }

    //check if also after guess
    @Test
    @DisplayName("check if round is created it also creates correct hint")
    void creatingRoundCreatesCorrectHint() {
        assertEquals(List.of('w','.','.','.','.'), round.getHint());

    }

    @Test
    @DisplayName("check if StateRound changes to LOST after guessing 5 times")
    void RoundIsOverTooManyGuesses() {
        round.doGuess("wonee");
        round.doGuess("wonee");
        round.doGuess("wonee");
        round.doGuess("wonee");
        round.doGuess("wonee");
        assertTrue(round.getState() == LOST);
        assertTrue(round.getState() != WAITING_FOR_INPUT);
        assertTrue(round.getState() != WON);
    }

    @Test
    @DisplayName("check if StateRound changes to WON after guessing the word")
    void RoundIsOverIfGuessedCorrect() {
        round.doGuess("wonen");
        assertTrue(round.getState() == WON);
        assertTrue(round.getState() != WAITING_FOR_INPUT);
        assertTrue(round.getState() != LOST);
    }

    @Test
    @DisplayName("test getLastFeedback when feedbacks bigger than 0")
    void roundGetLastFeedbackWhenContains() {
        round.doGuess("hallo");
        assertEquals(new Feedback("hallo", wordToGuess), round.getLastFeedback());
    }

    @Test
    @DisplayName("test getLastFeedback throws exception when list is empty, 0 or null")
    void roundGetLastFeedbackThrowsException() {
        Round round = new Round("Wonen");
        System.out.println(round.getFeedbacks());
        assertThrows(InvalidFeedbackException.class, () -> round.getLastFeedback());
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
        assertEquals(List.of('w', 'o', 'n', 'e', '.'), round.getHint());
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
        assertEquals(List.of('w','.','.','.','n'), round.getHint());
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
        assertEquals(List.of('w','o','.','e','.'), round.getHint());
        assertEquals(2, round.getGuesses());
    }

    @Test
    @DisplayName("Testing geuss on a round with invalid guess")
    void doGuessWithInvalidGuess() {
        round.doGuess("df");

        assertEquals(List.of(INVALID), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','.'), round.getHint());
        assertEquals(1, round.getGuesses());
    }

    @Test
    @DisplayName("Testing geuss on a round with too long guess")
    void doGuessWithTooLongGuess() {
        round.doGuess("dfffffff");

        assertEquals(List.of(INVALID), round.getLastFeedback().getMarks());
        assertEquals(List.of('w','.','.','.','.'), round.getHint());
        assertEquals(1, round.getGuesses());
    }

    private static Stream<Arguments> provideRoundOverExamples() {
        return Stream.of(
                Arguments.of(new Round("wonen"), 1, WON, new Feedback("wonen", "wonen")),
                Arguments.of(new Round("wonen"), 5, LOST, new Feedback("fg", "wonen")),
                Arguments.of(new Round("wonen"), 6, LOST, new Feedback("fg", "wonen"))
        );
    }

    //this oki?
    @ParameterizedTest
    @MethodSource("provideRoundOverExamples")
    @DisplayName("Testing geuss on a round that is not playable")
    void doGuessWhenRoundIsOver(Round round, Integer guesses, StateRound state, Feedback feedback) {
        round.setGuesses(guesses);
        round.getFeedbacks().add(feedback);
        round.setState(state);
        System.out.println(round);
        assertThrows(
                InvalidRoundException.class,
                () -> round.doGuess("bababooey")
        );
    }

    @Test
    @DisplayName("checks if round toString is correct")
    void getLengthWordToGuess() {
        Round round = new Round("pjooo");
        assertEquals(5, round.getLengthWordToGuess());

        Round round1 = new Round("pjooooo");
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
        Round roundCheck = new Round("wooon");
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round partially equals test -> guesses different")
    void roundPartiallyEqualsRoundGuesses() {
        Round roundCheck = new Round("wonen");
        roundCheck.setGuesses(3);
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round partially equals test -> feedbacks different")
    void roundPartiallyEqualsRoundFeedbacks() {
        Round roundCheck = new Round("wooon");
        roundCheck.getFeedbacks().add(new Feedback("woerd", "woord"));
        assertFalse(round.equals(roundCheck));
    }

    @Test
    @DisplayName("Round partially equals test -> hint different")
    void roundPartiallyEqualsRoundHint() {
        Round roundCheck = new Round("wooon");
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
                new Feedback("we", "woord"),
                new Feedback("woort", "woord")
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
        assertEquals("Round{wordToGuess=wonen, guesses=0, feedbacks=[]}", round.toString());
    }
}