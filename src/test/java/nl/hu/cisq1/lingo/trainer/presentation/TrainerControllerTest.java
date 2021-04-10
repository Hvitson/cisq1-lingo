package nl.hu.cisq1.lingo.trainer.presentation;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.presentation.controller.TrainerController;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponse;
import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TrainerControllerTest {
    private final TrainerService SERVICE = mock(TrainerService.class);
    private final TrainerController CONTROLLER = new TrainerController(SERVICE);
    private static Game game;


    @BeforeEach
    void createMocks() {
        game = new Game();
        game.createRound("appel");
    }

    @AfterEach
    void tearDown() {
        clearInvocations(SERVICE);
    }

    @Test
    @DisplayName("get response entity of game response by game id")
    void getGame() throws NotFoundException {
        when(SERVICE.findGame(2L)).thenReturn(game);
        ResponseEntity<GameResponse> gameById = CONTROLLER.getGame(2L);

        assertEquals(HttpStatus.OK, gameById.getStatusCode());
        verify(SERVICE, times(1)).findGame(2L);
    }

    @Test
    @DisplayName("getGame throws exception when game id is not found")
    void getGameThrowsException() throws NotFoundException {
        when(SERVICE.findGame(2L)).thenThrow(NotFoundException.class);

        assertThrows(
                NotFoundException.class,
                () -> CONTROLLER.getGame(2L)
        );
        verify(SERVICE, times(1)).findGame(2L);
    }

    @Test
    @DisplayName("get response entity of round response by game id")
    void getRound() throws NotFoundException {
        when(SERVICE.findGame(2L)).thenReturn(game);
        ResponseEntity<RoundResponse> roundById =  CONTROLLER.getRound(2L);

        assertEquals(HttpStatus.OK, roundById.getStatusCode());
        verify(SERVICE, times(1)).findGame(2L);
    }

    @Test
    @DisplayName("getRound throws exception when game id not found")
    void getRoundThrowsException() throws NotFoundException {
        when(SERVICE.findGame(2L)).thenThrow(NotFoundException.class);

        assertThrows(
                NotFoundException.class,
                () -> CONTROLLER.getRound(2L)
        );
        verify(SERVICE, times(1)).findGame(2L);
    }

    @Test
    @DisplayName("create game")
    void createGame() {
        when(SERVICE.createGame()).thenReturn(game);
        ResponseEntity<GameResponse> createdGame =  CONTROLLER.createGame();

        assertEquals(HttpStatus.CREATED, createdGame.getStatusCode());
        verify(SERVICE, times(1)).createGame();
    }

    @Test
    @DisplayName("create round on a game")
    void createRound() throws NotFoundException {
        when(SERVICE.createRound(2L)).thenReturn(game);
        ResponseEntity<RoundResponse> createdGame = CONTROLLER.createRound(2L);

        assertEquals(HttpStatus.CREATED, createdGame.getStatusCode());
        verify(SERVICE, times(1)).createRound(2L);
    }

    @Test
    @DisplayName("create round throws NotFoundException when game id is not found")
    void createRoundThrowsException() throws NotFoundException {
        when(SERVICE.createRound(2L)).thenThrow(NotFoundException.class);

        assertThrows(
                NotFoundException.class,
                () -> CONTROLLER.createRound(2L)
        );
        verify(SERVICE, times(1)).createRound(2L);
    }

    @Test
    @DisplayName("create round throws InvalidRoundException when round is still ongoing")
    void createRoundThrowsExceptionOngoing() throws NotFoundException {
        when(SERVICE.createRound(2L)).thenThrow(NotFoundException.class);

        assertThrows(
                NotFoundException.class,
                () -> CONTROLLER.createRound(2L)
        );
        verify(SERVICE, times(1)).createRound(2L);
    }

    @Test
    @DisplayName("create round on a game")
    void doGuess() throws NotFoundException {
        when(SERVICE.doGuess(2L, "jooow")).thenReturn(game);
        ResponseEntity<RoundResponse> guessedOnRound = CONTROLLER.doGuessOnRound(2L, "jooow");

        assertEquals(HttpStatus.OK, guessedOnRound.getStatusCode());
        verify(SERVICE, times(1)).doGuess(2L, "jooow");
    }

    @Test
    @DisplayName("doGuess throws exception when game id not found")
    void doGuessThrowsException() throws NotFoundException {
        when(SERVICE.doGuess(2L, "hallo")).thenThrow(NotFoundException.class);

        assertThrows(
                NotFoundException.class,
                () -> CONTROLLER.doGuessOnRound(2L, "hallo")
        );
        verify(SERVICE, times(1)).doGuess(2L, "hallo");
    }

    @Test
    @DisplayName("doGuess throws exception when round is over")
    void doGuessThrowsExceptionOngoing() throws NotFoundException, InvalidRoundException{
        when(SERVICE.doGuess(2L, "hallo")).thenThrow(InvalidRoundException.class);

        assertThrows(
                InvalidRoundException.class,
                () -> CONTROLLER.doGuessOnRound(2L, "hallo")
        );
        verify(SERVICE, times(1)).doGuess(2L, "hallo");
    }
}
