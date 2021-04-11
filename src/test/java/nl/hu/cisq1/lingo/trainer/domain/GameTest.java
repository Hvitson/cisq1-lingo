package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static nl.hu.cisq1.lingo.trainer.domain.state.StateGame.*;

class GameTest {
    private static Game game;

    @BeforeEach
    void setup() {
        game = new Game();
    }

    @Test
    @DisplayName("create a game")
    void gameConstructor() {
        assertEquals(0, game.getPlayingRoundNumber());
        assertEquals(0, game.getScore());
        assertEquals(WAITING_FOR_ROUND, game.getState());
    }

    @Test
    @DisplayName("create round for game")
    void createGame() {
        assertDoesNotThrow(() -> game.createRound("wonen"));
    }

    @Test
    @DisplayName("create round when round is won")
    void createGameWhenRoundIsOverByWinning() {
        game.createRound("wonen");
        game.doGuess("wonen");

        assertDoesNotThrow(() -> game.createRound("wonen"));
    }

    @Test
    @DisplayName("create round when round is lost")
    void createGameWhenGameIsOverByLosing() {
        game.createRound("wonen");
        for(int i = 0; i < 5; i++) {
            game.doGuess("jooow");
        }

        assertDoesNotThrow(() -> game.createRound("wonen"));
    }

    @Test
    @DisplayName("throw exception when round ongoing")
    void createRoundForGameThrows() {
        game.createRound("wonen");

        assertThrows(
                InvalidRoundException.class,
                () -> game.createRound("wonen")
        );
    }

    @Test
    @DisplayName("get last round of game")
    void getLastRound() {
        game.createRound("jooow");

        assertDoesNotThrow(() -> game.getLastRound());
        assertNotNull(game.getLastRound());
    }

    @Test
    @DisplayName("get last round of round when more than one round")
    void getLastRoundWhenMulti() {
        game.createRound("jooow");
        game.doGuess("jooow");
        game.createRound("jooow");

        assertDoesNotThrow(() -> game.getLastRound());
        assertNotNull(game.getLastRound());
        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("get last round throws when it does not exists")
    void getLastRoundThrowsException() {
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> game.getLastRound()
        );
    }

    @Test
    @DisplayName("do 5 guesses on rounds to see if it throws")
    void doGuessWhenRoundOngoing() {
        game.createRound("wonen");
        for(int i = 1; i < 6; i++) {

            assertDoesNotThrow(() -> game.doGuess("jooow"));
            assertEquals(i, game.getLastRound().getGuesses());
            assertEquals(i, game.getLastRound().getFeedbacks().size());
        }
    }

    @Test
    @DisplayName("win round to see if state is changed to WAITING_FOR_ROUND")
    void doGuessWhenOngoingRoundGuessed() {
        game.createRound("wonen");

        assertDoesNotThrow(() -> game.doGuess("wonen"));
        assertEquals(WAITING_FOR_ROUND, game.getState());
    }

    @Test
    @DisplayName("lose round to see if state is changed to WAITING_FOR_ROUND")
    void doGuessWhenOngoingRoundNotGuessed() {
        game.createRound("wonen");
        for(int i = 0; i < 4; i++) {
            game.doGuess("jooow");
        }

        assertDoesNotThrow(() -> game.doGuess("jooow"));
        assertEquals(WAITING_FOR_ROUND, game.getState());
    }

    @Test
    @DisplayName("do guess on round of game that is over by winning, to check if it throws exception")
    void doGuessRoundAlreadyOverWon() {
        game.createRound("wonen");
        game.doGuess("wonen");

        assertThrows(
                InvalidRoundException.class,
                () -> game.doGuess("wonen"));
    }

    @Test
    @DisplayName("do guess on round of game that is over by losing, to check if it throws exception")
    void doGuessRoundAlreadyOverLost() {
        game.createRound("wonen");
        for(int i = 0; i < 5; i++) {
            game.doGuess("jooow");
        }

        assertThrows(
                InvalidRoundException.class,
                () -> game.doGuess("wonen"));
    }

    @Test
    @DisplayName("do guess on game that has no rounds")
    void doGuessNoRounds() {
        assertThrows(
                InvalidRoundException.class,
                () -> game.doGuess("wonen"));
    }

    static Stream<Arguments> provideGuessAmountExamples() {
        return Stream.of(
                Arguments.of(
                        1,
                        25
                ),
                Arguments.of(
                        2,
                        20
                ),
                Arguments.of(
                        3,
                        15
                ),
                Arguments.of(
                        4,
                        10
                ),
                Arguments.of(
                        5,
                        5
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideGuessAmountExamples")
    @DisplayName("calculate scores")
    void calculateGuessScore(Integer guesses, Integer expectedScore) {
        Integer score = game.calculateScore(guesses);

        assertEquals(expectedScore, score);
    }

    @Test
    @DisplayName("score added after guessing word")
    void scoreAdded() {
        game.createRound("wonen");
        game.doGuess("wonen");
        assertEquals(25, game.getScore());
    }

    static Stream<Arguments> provideLengthExamples() {
        return Stream.of(
                Arguments.of(
                        new Game(),
                        "wonen",
                        6
                ),
                Arguments.of(
                        new Game(),
                        "wonenn",
                        7
                ),
                Arguments.of(
                        new Game(),
                        "wonennn",
                        5
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideLengthExamples")
    @DisplayName("lengthNextWordToGuess throws when length of word not supported")
    void lengthNextWordToGuess(Game providedGame, String wordToGuess, Integer expectedLength) {
        providedGame.createRound(wordToGuess);

        assertDoesNotThrow(() -> providedGame.lengthNextWordToGuess());
        assertEquals(expectedLength, providedGame.lengthNextWordToGuess());
    }

    static Stream<Arguments> provideIncorrectLengthExamples() {
        return Stream.of(
                Arguments.of(
                        new Game(),
                        "wone"
                ),
                Arguments.of(
                        new Game(),
                        "wonenghnn"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectLengthExamples")
    @DisplayName("lengthNextWordToGuess throws when length is wordToGuess is not supported")
    void lengthNextWordToGuessThrows(Game providedGame, String wordToGuess) {
        providedGame.createRound(wordToGuess);

        assertThrows(InvalidRoundException.class,
                () -> providedGame.lengthNextWordToGuess()
        );
    }

    @Test
    @DisplayName("lengthNextWordToGuess throws when game has no rounds")
    void lengthNextWordToGuessThrowsOutOFBound() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> game.lengthNextWordToGuess()
        );
    }
}
