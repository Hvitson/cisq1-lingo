package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;


import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;
import static nl.hu.cisq1.lingo.trainer.domain.state.StateGame.*;
import static org.mockito.Mockito.*;


class GameTest {
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
        assertNull(game.getGameId());
        assertEquals(0, game.getPlayingRoundNumber());
        assertEquals(0, game.getScore());
        assertEquals(WAITING_FOR_ROUND, game.getState());
    }

    @Test
    @DisplayName("lengthNextWordToGuess returns right Integer before and after word is guessed with word length 5")
    void fuctionReturnsRightInteger6() {
        System.out.println(game);
        game.createRound("wonen");
        assertEquals(6, game.lengthNextWordToGuess());
        game.doGuess("wonen");
        assertEquals(6, game.lengthNextWordToGuess());
    }

    @Test
    @DisplayName("lengthNextWordToGuess returns right Integer before and after word is guessed with word length 6")
    void functionReturnsRightInteger7() {
        game.createRound("wonenn");
        assertEquals(7, game.lengthNextWordToGuess());
        game.doGuess("wonenn");
        assertEquals(7, game.lengthNextWordToGuess());
    }

    @Test
    @DisplayName("lengthNextWordToGuess returns right Integer before and after word is guessed with word length 7")
    void functionReturnsRightInteger5() {
        game.createRound("wonennn");
        assertEquals(5, game.lengthNextWordToGuess());
        game.doGuess("wonennn");
        assertEquals(5, game.lengthNextWordToGuess());
    }

    @Test
    @DisplayName("lengthNextWordToGuess throws exception when length not supported")
    void lengthNextWordToGuessLengthNotSupportedThrows() {
        game.createRound("woon");
        Game game1 = new Game();
        game1.createRound("woonwoon");

        assertEquals(4, game.getLastRound().getLengthWordToGuess());
        assertThrows(InvalidRoundException.class
                , () -> game.lengthNextWordToGuess());
        assertEquals(8, game1.getLastRound().getLengthWordToGuess());
        assertThrows(InvalidRoundException.class
                , () -> game1.lengthNextWordToGuess());
    }

    @Test
    @DisplayName("get last round from created game")
    void getLastRound() {
        game.createRound(wordToGuess);
        assertEquals(1, game.getPlayingRoundNumber());
        assertEquals(1, game.getRounds().size());
        assertEquals(new Round(wordToGuess), game.getLastRound());
    }

    @Test
    @DisplayName("get last round from created game when there are 2 rounds")
    void getLastRoundFromRounds() {
        game.createRound("wonenen");
        game.doGuess("wonenen");
        game.createRound(wordToGuess);

        assertEquals(2, game.getPlayingRoundNumber());
        assertEquals(2, game.getRounds().size());
        assertEquals(new Round(wordToGuess), game.getLastRound());
    }


    @Test
    @DisplayName("check if doGuess works on ongoing round")
    void doGuessWhenRoundOngoing() {
        game.createRound(wordToGuess);
        for(int i = 1; i < 6; i++) {
            game.doGuess("jooow");
            assertEquals(i, game.getLastRound().getGuesses());
            System.out.println(game.getLastRound().getGuesses());
            assertEquals(i, game.getLastRound().getFeedbacks().size());
            assertEquals("jooow", game.getLastRound().getLastFeedback().getAttempt());
        }
    }

    @Test
    @DisplayName("check if doGuess changes stateGame and stateRound after wordToGuess is guessed")
    void doGuessWhenOngoingRoundGuessed() {
        assertEquals(WAITING_FOR_ROUND, game.getState());

        game.createRound(wordToGuess);

        assertEquals(PLAYING_ROUND, game.getState());
        assertEquals(WAITING_FOR_INPUT, game.getLastRound().getState());

        game.doGuess(wordToGuess);

        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertEquals(WON, game.getLastRound().getState());
    }

    @Test
    @DisplayName("check if doGuess changes stateGame and stateRound after wordToGuess is not guessed after 5 tries")
    void doGuessWhenOngoingRoundNotGuessed() {
        assertEquals(WAITING_FOR_ROUND, game.getState());

        game.createRound(wordToGuess);

        assertEquals(PLAYING_ROUND, game.getState());
        assertEquals(WAITING_FOR_INPUT, game.getLastRound().getState());

        for(int i = 1; i < 6; i++) {
            game.doGuess("jooow");
        }

        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertEquals(LOST, game.getLastRound().getState());
    }

    @Test
    @DisplayName("check if doGuess throws exception when trying to guess on a game thats already over")
    void doGuessRoundAlreadyOver() {
        game.createRound(wordToGuess);
        game.doGuess(wordToGuess);

        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertThrows(
                InvalidRoundException.class,
                () -> game.doGuess(wordToGuess));

        game.createRound(wordToGuess);
        for(int i = 1; i < 6; i++) {
            game.doGuess("jooow");
        }

        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertThrows(
                InvalidRoundException.class,
                () -> game.doGuess(wordToGuess));
    }

    @Test
    @DisplayName("try to start game when game is not finished")
    void createGameWhenGameNotFinished() {
        game.createRound(wordToGuess);

        assertThrows(InvalidRoundException.class,
                ()-> game.createRound(wordToGuess)
        );
    }

    @Test
    @DisplayName("check if able to create new round after round is won")
    void createGameWhenRoundIsOverByWinning() {
        game.createRound(wordToGuess);
        game.doGuess(wordToGuess);
        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertEquals(WON, game.getLastRound().getState());
        game.createRound(wordToGuess);

        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("check if able to create new round after round is won")
    void createGameWhenGameIsOverByLosing() {
        game.createRound(wordToGuess);
        for(int i = 0; i < 5; i++) {
            game.doGuess("jooow");
        }


        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertEquals(LOST, game.getLastRound().getState());
        assertEquals(5, game.getLastRound().getGuesses());
        assertEquals(1, game.getRounds().size());

        game.createRound("wonen");

        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("score is added after guessing word correctly")
    void scoreAddedAfterCorrectGuess() {
        assertEquals(0, game.getScore());

        game.createRound(wordToGuess);
        game.doGuess(wordToGuess);

        assertEquals(25, game.getScore());

        game.createRound(wordToGuess);
        game.doGuess("hoiii");
        game.doGuess(wordToGuess);

        assertEquals(45, game.getScore());
    }

    @Test
    @DisplayName("calculate score")
    void calculateRoundScore() {
        for(int i = 1; i < 6; i++) {
            Integer actualY = game.calculateScore(i);
            Integer expectedY = ( 5 * ( 5 - i ) ) + 5;
            assertEquals(expectedY, actualY);
        }
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
            fail();
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
    @DisplayName("Game toString check")
    void gameToString() {
        assertEquals("Game{rounds=[], score=0}", game.toString());
        game.createRound("banen");
        assertEquals("Game{rounds=[Round{wordToGuess=banen, guesses=0, feedbacks=[]}], score=0}", game.toString());
    }
}
