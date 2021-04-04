package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackResponseTest {
    private Game game;

    @BeforeEach
    void createGameWithRoundAndFeedback() {
        game = new Game();
        game.createRound("wonen");
        game.doGuess("wonnn");
    }

    @Test
    @DisplayName("create feedback response")
    void createFeedbackResponse() {
        FeedbackResponse response = new FeedbackResponse(game.getLastRound().getLastFeedback());
        assertEquals(game.getLastRound().getLastFeedback().getMarks(), response.getMarks());
        assertEquals(game.getLastRound().getLastFeedback().getAttempt(), response.getAttempt());
    }

}
