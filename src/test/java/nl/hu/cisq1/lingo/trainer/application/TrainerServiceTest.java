package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static nl.hu.cisq1.lingo.trainer.domain.state.StateGame.*;
import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TrainerServiceTest {
    private final SpringGameRepository gameRepository = mock(SpringGameRepository.class);
    private final WordService wordService = mock(WordService.class);
    private final TrainerService service = new TrainerService(gameRepository, wordService);
    private Game game;

    @BeforeEach
    void createMocks() {
        game = new Game();

        when(wordService.provideRandomWord(5)).thenReturn("appel");

        game.createRound(wordService.provideRandomWord(5));

        when(service.createGame()).thenReturn(mock(Game.class));
    }

    @AfterEach
    void tearDown() {
        clearInvocations(gameRepository, wordService);
    }

    @Test
    @DisplayName("findGame by id")
    void findGame() throws NotFoundException {
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));

        Game toFindGame = service.findGame(2L);

        assertEquals(game, toFindGame);
        assertEquals("appel", toFindGame.getLastRound().getWordToGuess());
        verify(gameRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("findGame by id but throw exception")
    void findGameThrowException() {
        assertThrows(NotFoundException.class, () -> service.findGame(1L));

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("start a new game")
    void startGame() {
        when(gameRepository.save(game)).thenReturn(game);

        Game newGame = (assertDoesNotThrow(() -> service.createGame()));

        assertEquals(PLAYING_ROUND, newGame.getState());
        assertEquals(1, newGame.getPlayingRoundNumber());
        assertEquals(WAITING_FOR_INPUT, newGame.getLastRound().getState());
    }

    @Test
    @DisplayName("start a new game does not throw")
    void startGameDoesNotThrow() {
        when(gameRepository.save(game)).thenReturn(game);
        assertDoesNotThrow(() -> service.createGame());
    }

    @Test
    @DisplayName("start a new round on a game")
    void startRound() {
        when(wordService.provideRandomWord(6)).thenReturn("appell");
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);

        Game updatedGame = (assertDoesNotThrow(() -> service.createRound(10L)));

        assertEquals(2, updatedGame.getRounds().size());
        assertEquals("appell", updatedGame.getLastRound().getWordToGuess());
        verify(wordService, times(1)).provideRandomWord(6);
        verify(gameRepository, times(1)).findById(10L);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    @DisplayName("start a new round throws exception when game is ongoing")
    void startRoundThrowsException() {
        when(wordService.provideRandomWord(6)).thenReturn("appell");
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));

        assertThrows(
                InvalidRoundException.class,
                () -> service.createRound(10L)
        );
        verify(wordService, times(1)).provideRandomWord(6);
        verify(gameRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("do guess on a round that's ongoing")
    void doGuessOngoingRound() {
        when(gameRepository.findById(15L)).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);

        Game updatedGame = (assertDoesNotThrow(() -> service.doGuess(15L, "allep")));

        assertEquals(1, updatedGame.getLastRound().getFeedbacks().size());
        assertEquals(1, updatedGame.getLastRound().getGuesses());
        assertEquals("allep", updatedGame.getLastRound().getLastFeedback().getAttempt());
        verify(gameRepository, times(1)).findById(15L);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    @DisplayName("do guess on a round that's won")
    void doGuessWonRound() {
        when(gameRepository.findById(15L)).thenReturn(Optional.of(game));

        game.doGuess("appel");

        assertEquals(WON, game.getLastRound().getState());
        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertThrows(
                InvalidRoundException.class,
                () -> service.doGuess(15L, "jooow")
        );
        verify(gameRepository, times(1)).findById(15L);
    }

    @Test
    @DisplayName("do guess on a round that's lost")
    void doGuessLostRound() {
        when(gameRepository.findById(15L)).thenReturn(Optional.of(game));
        for (int i = 0; i < 5; i++) {
            game.doGuess("jooow");
        }

        assertEquals(LOST, game.getLastRound().getState());
        assertEquals(WAITING_FOR_ROUND, game.getState());
        assertThrows(
                InvalidRoundException.class,
                () -> service.doGuess(15L, "jooow")
        );
        verify(gameRepository, times(1)).findById(15L);
    }
}
