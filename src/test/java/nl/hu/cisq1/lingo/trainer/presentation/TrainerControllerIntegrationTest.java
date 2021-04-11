package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrainerService SERVICE;
    @Autowired
    private SpringGameRepository gameRepository;

    private Game game;

    @BeforeEach
    void createMocks() {
        gameRepository.deleteAll();
        game = SERVICE.createGame();
    }

    @AfterEach
    void tearDown() {
        gameRepository.deleteAll();
    }

    @Test
    @DisplayName("get game")
    void getGame() throws Exception {
        mockMvc.perform(
                get("/games/{gameId}", game.getGameId()))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").exists())
                .andExpect(jsonPath("$.stateGame").value("PLAYING_ROUND"))
                .andExpect(jsonPath("$.playingRoundNumber").value(1))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.rounds").isArray()
                );
    }

    @Test
    @DisplayName("get game throws exception when not found")
    void getGameThrows() throws Exception {
        Long id = game.getGameId()+1;
        mockMvc.perform(
                get("/games/{gameId}", id))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game with id "+ id + " not found"));
    }

    @Test
    @DisplayName("get round")
    void getRound() throws Exception {
        System.out.println(game.getLastRound().getHint());
        mockMvc.perform(
                get("/games/currentRound/{gameId}", game.getGameId()))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roundId").value(game.getLastRound().getRoundId()))
                .andExpect(jsonPath("$.stateRound").value(WAITING_FOR_INPUT.toString()))
                .andExpect(jsonPath("$.guesses").value(0))
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.feedbackList").isArray());
    }

    @Test
    @DisplayName("get round throws exception when game is not found")
    void getRoundThrows() throws Exception {
        Long id = game.getGameId()+1;
        mockMvc.perform(
                get("/games/currentRound/{gameId}", id))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game with id "+ id + " not found"));
    }

    @Test
    @DisplayName("create a game")
    void createGame() throws Exception {
        mockMvc.perform(
                post("/games/createGame"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId").exists())
                .andExpect(jsonPath("$.stateGame").value("PLAYING_ROUND"))
                .andExpect(jsonPath("$.playingRoundNumber").value(1))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.rounds").isArray()
                );
    }

    @Test
    @DisplayName("create a round")
    void createRound() throws Exception {
        SERVICE.doGuess(game.getGameId(), game.getLastRound().getWordToGuess());

        mockMvc.perform(
                post("/games/createRound/{gameId}", game.getGameId()))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roundId").exists())
                .andExpect(jsonPath("$.stateRound").value(WAITING_FOR_INPUT.toString()))
                .andExpect(jsonPath("$.guesses").value(0))
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.feedbackList").isArray());
    }

    @Test
    @DisplayName("create a round throws exception when round is still ongoing")
    void createRoundThrows() throws Exception {
        mockMvc.perform(
                post("/games/createRound/{gameId}", game.getGameId()))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("finish your last round before you start a new one!"));
    }

    @Test
    @DisplayName("do guess on ongoing round, after guess its still ongoing")
    void doGuessOngoing() throws Exception {
        mockMvc.perform(
                post("/games/{gameId}/{attempt}", game.getGameId(), "jooow"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roundId").value(game.getLastRound().getRoundId()))
                .andExpect(jsonPath("$.stateRound").value(WAITING_FOR_INPUT.toString()))
                .andExpect(jsonPath("$.guesses").exists())
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.feedbackList").isArray());
    }

    @Test
    @DisplayName("do guess on ongoing round, after guess game is won")
    void doGuessWon() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/games/{gameId}/{attempt}", game.getGameId(), game.getLastRound().getWordToGuess());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roundId").value(game.getLastRound().getRoundId()))
                .andExpect(jsonPath("$.stateRound").value(WON.toString()))
                .andExpect(jsonPath("$.guesses").exists())
                .andExpect(jsonPath("$.hint").exists())
                .andExpect(jsonPath("$.feedbackList").exists());
    }

    @Test
    @DisplayName("do guess on ongoing round, after guess game is lost")
    void doGuessLost() throws Exception {
        System.out.println(game.getLastRound().getGuesses());
        SERVICE.doGuess(game.getGameId(), "weewe");

        mockMvc.perform(
                post("/games/{gameId}/{attempt}", game.getGameId(), "jooow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roundId").value(game.getLastRound().getRoundId()))
                .andExpect(jsonPath("$.stateRound").exists())
                .andExpect(jsonPath("$.guesses").value(2))
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.feedbackList").isArray());
    }

    @Test
    @DisplayName("do guess on ongoing round throws exception when round is already over")
    void doGuessThrowsWhenRoundOver() throws Exception {
        SERVICE.doGuess(game.getGameId(), game.getLastRound().getWordToGuess());

        mockMvc.perform(post("/games/{gameId}/{attempt}", game.getGameId(), "jooow"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Round already over, start a new round!"));
    }

    @Test
    @DisplayName("do guess on ongoing round throws exception when game not found")
    void doGuessThrowsWhenGameNotFound() throws Exception {
        Long id = game.getGameId()+1;

        mockMvc.perform(post("/games/{gameId}/{attempt}", id, "jooow"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game with id "+ id + " not found"));
    }
}
