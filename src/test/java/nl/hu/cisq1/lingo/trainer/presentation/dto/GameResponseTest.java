package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameResponseTest {
    private Game game;

    @BeforeEach
    void createGameWithRoundAndFeedback() {
        game = new Game();
        game.createRound("wonen");
        game.doGuess("wonen");
        game.createRound("wonnn");
        game.doGuess("wonen");
    }

    @Test
    @DisplayName("create game response")
    void createGameResponse() {
        GameResponse response = new GameResponse(game);
        assertEquals(game.getGameId(), response.getGameId());
        assertEquals(game.getState(), response.getStateGame());
        assertEquals(game.getPlayingRoundNumber(), response.getPlayingRoundNumber());
        assertEquals(game.getScore(), response.getScore());
        assertEquals(game.getRounds().size(), response.getRounds().size());
    }

}
