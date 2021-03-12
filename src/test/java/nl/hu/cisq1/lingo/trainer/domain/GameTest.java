package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameTest {
    private Game game;
    private String wordToGuess;

    @BeforeEach
    void createStartGame() {
        game = new Game();
        wordToGuess = "wonen";
    }

    @Test
    @DisplayName("test game constructor")
    void gameConstructor() {
        assertEquals(new Game(), game);
    }

    @Test
    @DisplayName("get last round from created game")
    void getLastRound() {
        assertEquals(new Round(wordToGuess), game.getLastRound());
    }

    @Test
    @DisplayName("get last round from created game when there are 2 rounds")
    void getLastRoundFromRounds() {
        Round toBe = new Round(wordToGuess);
        game.getLastRound().doGuess("wonen");
        game.createRound("wonen");

        assertEquals(toBe.toString(), game.getLastRound().toString());
        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("try to start game when game is not finished")
    void createGameWhenGameNotFinished() {
        assertThrows(InvalidRoundException.class,
                ()-> game.createRound("wonen")
        );
    }

    //hash en equals
    @Test
    @DisplayName("Game hashcode test")
    void gameHashCode() {
        Game gameCheck = new Game();
        assertEquals(gameCheck.hashCode(), game.hashCode());
    }

    @Test
    @DisplayName("Game hashcode 0 test")
    void feedbackHashCode0() {
        if (game.hashCode() == 0) {
            assertFalse(true);
        }
        assertFalse(false);
    }

    @Test
    @DisplayName("Game equals test")
    void gameEqualsGame() {
        Game gameCheck = new Game();
        assertTrue(gameCheck.equals(game));
    }

    @Test
    @DisplayName("Game partially equals test -> score different")
    void gamePartiallyEqualsGame() {
        Game gameCheck = new Game();
        gameCheck.setScore(50);
        assertFalse(gameCheck.equals(game));
    }

    @Test
    @DisplayName("Game equals different type")
    void gameEqualsDifferentType() {
        assertFalse(game.equals(List.of(new Feedback("jooow", wordToGuess))));
    }

    @Test
    @DisplayName("Game not equals test")
    void gameEqualsBySet() {
        Game gameCheck = game;
        assertTrue(gameCheck.equals(game));
    }

    @Test
    @DisplayName("game not equals game round")
    void gameRoundsNotEqualsgameRound() {
        Game gameCheck = new Game();
        gameCheck.setScore(50);
        gameCheck.getRounds().add(new Round(wordToGuess));
        assertFalse(game.equals(gameCheck));
    }

    @Test
    @DisplayName("Game equals null test")
    void hintNotEqualsNull() {
        Game gameCheck = null;
        assertFalse(game.equals(gameCheck));
    }

    @Test
    @DisplayName("Game toString is created correctly")
    void gameToString() {
        assertEquals("Game{rounds=[Round{wordToGuess=wonen, guesses=0, feedbacks=[]}], score=0}", game.toString());
    }


    // voor length tests en throw
//    private static Stream<Arguments> provideWrongLengthExamples() {
//        return Stream.of(
//                Arguments.of("hallotjes", 8),
//                Arguments.of("boer", 4)
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideWrongLengthExamples")
//    @DisplayName("exception throw test")
//    void wordThrowException(String value, Integer length) {
//        game.
//        assertThrows(
//                InvalidRoundException.class,
//                () -> round.
//        );
//    }

}
