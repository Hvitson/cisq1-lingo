package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.AfterEach;
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
    private static Game game;

    @BeforeEach
    void createMocks() {
        game = new Game();

        when(wordService.provideRandomWord(5)).thenReturn("appel");

        game.createRound(wordService.provideRandomWord(5));

        when(service.startGame()).thenReturn(mock(Game.class));
    }

    @AfterEach
    void tearDown() {
        clearInvocations(gameRepository, wordService);
    }

    @Test
    @DisplayName("getAllGames")
    void getAllGames() {
        when(gameRepository.findAll()).thenReturn(List.of(game, new Game(), new Game()));

        assertEquals(3, service.getAllGames().size());
        assertEquals("appel", service.getAllGames().get(0).getLastRound().getWordToGuess());
        assertEquals(0, service.getAllGames().get(1).getScore());
        verify(gameRepository, times(3)).findAll();
    }

    @Test
    @DisplayName("findGame by id")
    void findGame() {
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));

        Game toFindGame = service.findGame(2L);

        assertEquals(game, toFindGame);
        assertEquals("appel", toFindGame.getLastRound().getWordToGuess());
        verify(gameRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("findGame by id but throw exception")
    void findGameThrowException() {
        assertThrows(InvalidGameException.class, () -> service.findGame(1L));
        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("start a new game")
    void startGame() {
        when(gameRepository.save(game)).thenReturn(game);

        Game newGame = service.startGame();

        assertEquals(PLAYING_ROUND, newGame.getState());
        assertEquals(1, newGame.getPlayingRoundNumber());
        assertNotNull(newGame.getRounds());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    @DisplayName("start a new round")
    void startRound() {
        assertEquals(1, game.getRounds().size());
        assertEquals("appel", game.getLastRound().getWordToGuess());

        game.doGuess("appel");

        when(wordService.provideRandomWord(6)).thenReturn("appell");
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);

        Game updatedGame = service.startRound(10L);

        assertEquals(2, updatedGame.getRounds().size());
        assertEquals("appell", updatedGame.getLastRound().getWordToGuess());
        verify(wordService, times(1)).provideRandomWord(6);
        verify(gameRepository, times(1)).findById(10L);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    @DisplayName("start a new round throws exception because game still ongoing")
    void startRoundThrowsException() {
        when(wordService.provideRandomWord(6)).thenReturn("appell");
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));

        assertThrows(
                InvalidRoundException.class,
                () -> service.startRound(10L)
        );
        verify(wordService, times(1)).provideRandomWord(6);
        verify(gameRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("do guess on a round that's ongoing")
    void doGuessOngoingRound() {
        assertEquals(0, game.getLastRound().getFeedbacks().size());

        when(gameRepository.findById(15L)).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);

        System.out.println(game);
        Game updatedGame = service.doGuess(15L, "allep");
        System.out.println("hoe "+updatedGame);
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
