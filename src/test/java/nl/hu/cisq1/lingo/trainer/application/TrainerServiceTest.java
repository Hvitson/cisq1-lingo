package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static nl.hu.cisq1.lingo.trainer.domain.state.StateGame.*;
import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    SpringGameRepository gameRepository = mock(SpringGameRepository.class);
    WordService wordService = mock(WordService.class);
    TrainerService service = new TrainerService(gameRepository, wordService);
    Game game = new Game();

    @BeforeEach
    void createMocks() {
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        when(service.getAllGames()).thenReturn(List.of(game, new Game(), new Game()));
        when(gameRepository.findGameByGameId(2L)).thenReturn(Optional.of(game));
        when(service.startGame()).thenReturn(mock(Game.class));

        game.createRound(wordService.provideRandomWord(5));
    }

    @Test
    @DisplayName("getAllGames")
    void getAllGames() {
        assertEquals(3, service.getAllGames().size());
        assertEquals("appel", service.getAllGames().get(0).getLastRound().getWordToGuess());
        assertEquals(0, service.getAllGames().get(1).getScore());
    }

    @Test
    @DisplayName("findGame by id")
    void findGame() {
        Game toFindGame = service.findGame(2L);

        assertEquals("appel", toFindGame.getLastRound().getWordToGuess());
        assertEquals(0, toFindGame.getScore());
    }

    @Test
    @DisplayName("findGame by id but throw exception")
    void findGameThrowException() {
        assertThrows(InvalidGameException.class, () -> service.findGame(1L));
    }

    @Test
    @DisplayName("start a new game")
    void startGame() {
        Game newGame = service.startGame();
        assertEquals(PLAYING_ROUND, newGame.getState());
        assertEquals(1, newGame.getPlayingRoundNumber());
        assertEquals(WAITING_FOR_INPUT, newGame.getLastRound().getState());
    }

    @Test
    @DisplayName("start a new round")
    void startRound() {
        assertEquals(0, game.getScore());
        game.doGuess("appel");
        assertEquals(25, game.getScore());
        when(wordService.provideRandomWord(6)).thenReturn("appell");
        when(gameRepository.findGameByGameId(10L)).thenReturn(Optional.of(game));
        Game updatedGame = service.startRound(10L);
        assertEquals(25, updatedGame.getScore());
        assertEquals(2, updatedGame.getRounds().size());
        assertEquals("appell", updatedGame.getLastRound().getWordToGuess());
    }

    @Test
    @DisplayName("start a new round throws exception because game still ongoing")
    void startRoundThrowsException() {
        when(wordService.provideRandomWord(6)).thenReturn("appell");
        when(gameRepository.findGameByGameId(10L)).thenReturn(Optional.of(game));

        assertThrows(InvalidRoundException.class,
                () -> service.startRound(10L));
    }

    @Test
    @DisplayName("do guess on a round that's ongoing")
    void doGuessOngoingRound() {
        assertEquals(0, game.getLastRound().getFeedbacks().size());
        when(gameRepository.findGameByGameId(15L)).thenReturn(Optional.of(game));
        Game updatedGame = service.doGuess(15L, "allep");
        assertEquals(1, updatedGame.getLastRound().getFeedbacks().size());
        assertEquals(1, updatedGame.getLastRound().getGuesses());
        assertEquals("allep", updatedGame.getLastRound().getLastFeedback().getAttempt());
    }

    @Test
    @DisplayName("do guess on a round that's won")
    void doGuessWonRound() {
        game.doGuess("appel");
        assertEquals(WON, game.getLastRound().getState());
        assertEquals(WAITING_FOR_ROUND, game.getState());
        when(gameRepository.findGameByGameId(15L)).thenReturn(Optional.of(game));
        assertThrows(InvalidRoundException.class,
                () -> service.doGuess(15L, "jooow"));
    }

    @Test
    @DisplayName("do guess on a round that's lost")
    void doGuessLostRound() {
        for (int i = 0; i < 5; i++) {
            game.doGuess("jooow");
        }
        assertEquals(LOST, game.getLastRound().getState());
        assertEquals(WAITING_FOR_ROUND, game.getState());
        when(gameRepository.findGameByGameId(15L)).thenReturn(Optional.of(game));
        assertThrows(InvalidRoundException.class,
                () -> service.doGuess(15L, "jooow"));
    }
}
