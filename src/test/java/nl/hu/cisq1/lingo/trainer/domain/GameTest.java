package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.state.StateGame;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.WAITING_FOR_INPUT;
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
    @DisplayName("get last feedback of round")
    void getLastRound() {
        game.createRound("jooow");

        assertDoesNotThrow(() -> game.getLastRound());
        assertNotNull(game.getLastRound());
    }

    @Test
    @DisplayName("get last feedback of round")
    void getLastRoundWhenMulti() {
        game.createRound("jooow");
        game.doGuess("jooow");
        game.createRound("jooow");

        assertDoesNotThrow(() -> game.getLastRound());
        assertNotNull(game.getLastRound());
        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("get last feedback when it does not exists")
    void getLastRoundThrowsException() {
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> game.getLastRound()
        );
    }

    @Test
    @DisplayName("do guess on rounds 1 to 5")
    void doGuessWhenRoundOngoing() {
        game.createRound("wonen");
        for(int i = 1; i < 6; i++) {

            assertDoesNotThrow(() -> game.doGuess("jooow"));
            assertEquals(i, game.getLastRound().getGuesses());
            assertEquals(i, game.getLastRound().getFeedbacks().size());
        }
    }

    @Test
    @DisplayName("guess word to see if state chages game's state to WAITING_FOR_ROUND when round is won")
    void doGuessWhenOngoingRoundGuessed() {
        game.createRound("wonen");

        assertDoesNotThrow(() -> game.doGuess("wonen"));
        assertEquals(WAITING_FOR_ROUND, game.getState());
    }

    @Test
    @DisplayName("guess word to see if state chages game's state to WAITING_FOR_ROUND when round is lost")
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

    //todo: lengthNextWordToGuess tests
    // equals en hash

//    @Test
//    @DisplayName("lengthNextWordToGuess returns right Integer before and after word is guessed with word length 5")
//    void fuctionReturnsRightInteger6() {
//        System.out.println(game);
//        game.createRound("wonen");
//        assertEquals(6, game.lengthNextWordToGuess());
//        game.doGuess("wonen");
//        assertEquals(6, game.lengthNextWordToGuess());
//    }
//
//    @Test
//    @DisplayName("lengthNextWordToGuess returns right Integer before and after word is guessed with word length 6")
//    void functionReturnsRightInteger7() {
//        game.createRound("wonenn");
//        assertEquals(7, game.lengthNextWordToGuess());
//        game.doGuess("wonenn");
//        assertEquals(7, game.lengthNextWordToGuess());
//    }
//
//    @Test
//    @DisplayName("lengthNextWordToGuess returns right Integer before and after word is guessed with word length 7")
//    void functionReturnsRightInteger5() {
//        game.createRound("wonennn");
//        assertEquals(5, game.lengthNextWordToGuess());
//        game.doGuess("wonennn");
//        assertEquals(5, game.lengthNextWordToGuess());
//    }
//
//    @Test
//    @DisplayName("lengthNextWordToGuess throws exception when length not supported")
//    void lengthNextWordToGuessLengthNotSupportedThrows() {
//        game.createRound("woon");
//        Game game1 = new Game();
//        game1.createRound("woonwoon");
//
//        assertEquals(4, game.getLastRound().getLengthWordToGuess());
//        assertThrows(InvalidRoundException.class
//                , () -> game.lengthNextWordToGuess());
//        assertEquals(8, game1.getLastRound().getLengthWordToGuess());
//        assertThrows(InvalidRoundException.class
//                , () -> game1.lengthNextWordToGuess());
//    }
//
//
//
//
//
//
//
//    //hash en equals
//    @Test
//    @DisplayName("Game hashcode test")
//    void gameHashCode() {
//        Game gameCheck = new Game();
//        assertEquals(gameCheck.hashCode(), game.hashCode());
//    }
//
//    @Test
//    @DisplayName("Game hashcode 0 test")
//    void feedbackHashCode0() {
//        if (game.hashCode() == 0) {
//            fail();
//        }
//        assertFalse(false);
//    }
//
//    @Test
//    @DisplayName("Game equals test")
//    void gameEqualsGame() {
//        Game gameCheck = new Game();
//
//        assertTrue(gameCheck.equals(game));
//    }
//
//    @Test
//    @DisplayName("Game partially equals test -> score different")
//    void gamePartiallyEqualsGame() {
//        Game gameCheck = new Game();
//
//        gameCheck.setScore(50);
//
//        assertFalse(gameCheck.equals(game));
//    }
//
//    @Test
//    @DisplayName("Game equals different type")
//    void gameEqualsDifferentType() {
//        assertFalse(game.equals(List.of(new Feedback("jooow", wordToGuess))));
//    }
//
//    @Test
//    @DisplayName("Game not equals test")
//    void gameEqualsBySet() {
//        Game gameCheck = game;
//
//        assertTrue(gameCheck.equals(game));
//    }
//
//    @Test
//    @DisplayName("game not equals game round")
//    void gameRoundsNotEqualsgameRound() {
//        Game gameCheck = new Game();
//        gameCheck.setScore(50);
//        gameCheck.getRounds().add(new Round(wordToGuess));
//        assertFalse(game.equals(gameCheck));
//    }
//
//    @Test
//    @DisplayName("Game equals null test")
//    void hintNotEqualsNull() {
//        Game gameCheck = null;
//        assertFalse(game.equals(gameCheck));
//    }
}
