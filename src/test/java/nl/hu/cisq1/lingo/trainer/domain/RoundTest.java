package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.state.StateRound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;

class RoundTest {

    @Test
    @DisplayName("Create empty Round")
    void createEmptyRound() {
        Round emptyRound = new Round();
        assertNull(emptyRound.getRoundId());
        assertNull(emptyRound.getWordToGuess());
        assertNull(emptyRound.getGuesses());
        assertNull(emptyRound.getFeedbacks());
    }


    @Test
    @DisplayName("get last feedback of round")
    void getLastFeedback() {
        Round round = new Round("wonen");
        round.doGuess("ds");

        assertNotNull(round.getLastFeedback());
    }

    @Test
    @DisplayName("get last feedback of round when there are more than 1")
    void getLastFeedbackWhenMulti() {
        Round round = new Round("wonen");
        round.doGuess("ds");

        assertNotNull(round.getLastFeedback());
    }

    @Test
    @DisplayName("get last feedback when it does not exists")
    void getLastFeedbackThrowsException() {
        Round round = new Round("Wonen");

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> round.getLastFeedback()
        );
    }

    static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of(
                        new Round("wonen"),
                        "wonee",
                        null,
                        null,
                        new Feedback("wonee", "wonen"),
                        new Hint(List.of('w','o','n','e','.')),
                        1,
                        WAITING_FOR_INPUT
                ),
                Arguments.of(
                        new Round("wonen"),
                        "df",
                        null,
                        null,
                        new Feedback("df", "wonen"),
                        new Hint(List.of('w','.','.','.','.')),
                        1,
                        WAITING_FOR_INPUT
                ),
                Arguments.of(
                        new Round("wonen"),
                        "dffffffff",
                        null,
                        null,
                        new Feedback("dffffffff", "wonen"),
                        new Hint(List.of('w','.','.','.','.')),
                        1,
                        WAITING_FOR_INPUT
                ),
                Arguments.of(
                        new Round("wonen"),
                        "wonen",
                        null,
                        null,
                        new Feedback("wonen", "wonen"),
                        new Hint(List.of('w','o','n','e','n')),
                        1,
                        WON
                ),
                Arguments.of(
                        new Round("wonen"),
                        "weeee",
                        "wonee",
                        1,
                        new Feedback("wonee", "wonen"),
                        new Hint(List.of('w','o','n','e','.')),
                        2,
                        WAITING_FOR_INPUT
                ),
                Arguments.of(
                        new Round("wonen"),
                        "wone",
                        "wonnn",
                        4,
                        new Feedback("wonnn", "wonen"),
                        new Hint(List.of('w','o','n','.','n')),
                        5,
                        LOST
                ),
                Arguments.of(
                        new Round("wonen"),
                        "weeee",
                        "wonee",
                        4,
                        new Feedback("wonee", "wonen"),
                        new Hint(List.of('w','o','n','e','.')),
                        5,
                        LOST
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("do guess on a round")
    void roundDoGuess(Round round, String attempt, String fillerAttempt, Integer guesses,
                                Feedback expectedFeedback, Hint expectedHint,
                                Integer expectedGuesses, StateRound expectedState) {
        round.doGuess(attempt);
        if (guesses != null) {
            for (int i = 0; i < guesses; i++) {
                round.doGuess(fillerAttempt);
            }
        }

        assertEquals(expectedFeedback, round.getLastFeedback());
        assertEquals(expectedHint.getChars(), round.getHint());
        assertEquals(expectedGuesses, round.getGuesses());
        assertEquals(expectedState, round.getState());
    }

    private static Stream<Arguments> provideRoundOverExamples() {
        return Stream.of(
                Arguments.of(
                        new Round("wonen"),
                        1,
                        WON,
                        new Feedback("wonen", "wonen")
                ),
                Arguments.of(
                        new Round("wonen"),
                        5, LOST,
                        new Feedback("fg", "wonen")
                ),
                Arguments.of(
                        new Round("wonen"),
                        6, LOST,
                        new Feedback("fg", "wonen")
                )
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
        assertThrows(
                InvalidRoundException.class,
                () -> round.doGuess("bababooey")
        );
    }

    private static Stream<Arguments> provideLengthWordExamples() {
        return Stream.of(
                Arguments.of(
                        new Round("wonen"),
                        5
                ),
                Arguments.of(
                        new Round("wonenn"),
                        6
                ),
                Arguments.of(
                        new Round("wonennn"),
                        7
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideLengthWordExamples")
    @DisplayName("Testing geuss on a round that is not playable")
    void getLengthWordToGuess(Round round, Integer expectedLength) {
        assertEquals(expectedLength, round.getLengthWordToGuess());
    }

    static Stream<Arguments> provideEqualsExamples() {
        Round round = new Round("wonen");
        return Stream.of(
                Arguments.of(
                        round,
                        round,
                        true
                ),
                Arguments.of(
                        new Round("wonen"),
                        round,
                        true
                ),
                Arguments.of(
                        new Round("wonen"),
                        new Round("wonen"),
                        true
                ),
                Arguments.of(
                        new Round("wanen"),
                        new Round("wonen"),
                        false
                ),
                Arguments.of(
                        new Round("wanen"),
                        null,
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideEqualsExamples")
    @DisplayName("Round equals and hashcode methods test")
    void roundEqualsHashcode(Round round, Round compareRound, boolean isEqual) {
        assertEquals(round.equals(compareRound), isEqual);

        if (compareRound != null) {
            assertEquals(Objects.equals(round.hashCode(), compareRound.hashCode()), isEqual);
        }
    }


}