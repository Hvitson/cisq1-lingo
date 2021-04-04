package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundResponseTest {
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
    @DisplayName("create round response")
    void createRoundResponse() {
        RoundResponse response = new RoundResponse(game.getLastRound());
        assertEquals(game.getLastRound().getRoundId(), response.getRoundId());
        assertEquals(game.getLastRound().getState(), response.getStateRound());
        assertEquals(game.getLastRound().getGuesses(), response.getGuesses());
        assertEquals(game.getLastRound().getHint(), response.getHint());
        assertEquals(game.getLastRound().getFeedbacks().size(), response.getFeedbackList().size());
    }

}
