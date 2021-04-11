package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService SERVICE;
    @Autowired
    private SpringGameRepository gameRepository;

    private Game game;

    @BeforeEach
    void setup() {
        gameRepository.deleteAll();
        game = SERVICE.createGame();
    }

    @AfterEach
    void tearDown() {
        gameRepository.deleteAll();
    }

    @Test
    @DisplayName("get a game")
    void getGame() {
        assertDoesNotThrow(
                () -> {
                    SERVICE.findGame(game.getGameId());
                }
        );
    }

    @Test
    @DisplayName("get a game throws exception when game with given id not found")
    void getGameThrows() {
        Long newId = game.getGameId()+1;

        assertThrows(
                NotFoundException.class,
                () -> SERVICE.findGame(newId)
        );
    }

    @Test
    @DisplayName("create new game")
    void createGame() {
        assertDoesNotThrow(
                () -> {
                    Game createdGame = SERVICE.createGame();
                    gameRepository.findById(createdGame.getGameId());
                }
        );
    }

    @Test
    @DisplayName("create new round on a game")
    void createRound() {
        Long id = game.getGameId();
        game.doGuess(game.getLastRound().getWordToGuess());
        gameRepository.save(game);

        assertDoesNotThrow(
                () -> {
                    SERVICE.createRound(id);
                    SERVICE.findGame(id);
                }
        );
    }

    @Test
    @DisplayName("create new round throws IndexOutOfBoundsException because no rounds exist")
    void createRoundThrowsIndexException() {
        game.getRounds().remove(0);
        Game emptyRoundGame = gameRepository.save(game);
        Long id = emptyRoundGame.getGameId();

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> SERVICE.createRound(id)
        );
    }

    @Test
    @DisplayName("create new round on a game throws exception because round still ongoing")
    void createRoundThrowsRoundException() {
        Long id = game.getGameId();

        assertThrows(
                InvalidRoundException.class,
                () -> SERVICE.createRound(id)
        );
    }

    @Test
    @DisplayName("create new round on a game throws exception because game not found")
    void createRoundThrowsNotFound() {
        Long newId = game.getGameId()+1;

        assertThrows(
                NotFoundException.class,
                () -> {
                    SERVICE.createRound(newId);
                    SERVICE.findGame(newId);
                }
        );
    }

    @Test
    @DisplayName("do guess on a round")
    void doGuess() {
        Long id = game.getGameId();

        assertDoesNotThrow(
                () -> {
                    SERVICE.doGuess(id , "jooow");
                    SERVICE.findGame(id);
                }
        );
    }

    @Test
    @DisplayName("do guess on round throws exception when game not found")
    void doGuessThrowsNotFound() {
        Long newId = game.getGameId()+1;

        assertThrows(
                NotFoundException.class,
                () -> {
                    SERVICE.doGuess(newId, "jooow");
                    SERVICE.findGame(newId);
                }
        );
    }

    @Test
    @DisplayName("do guess on a game's round but no rounds exist")
    void doGuessThrowsRoundExceptionWhenEmpty() {
        game.getRounds().remove(0);
        Game emptyRoundGame = gameRepository.save(game);
        Long id = emptyRoundGame.getGameId();

        assertThrows(
                InvalidRoundException.class,
                () -> SERVICE.doGuess(id, "jooow")
        );
    }

    @Test
    @DisplayName("do guess on a game's round but no rounds exist")
    void doGuessThrowsRoundExceptionWhenRoundOver() {
        Long id = game.getGameId();
        game.doGuess(game.getLastRound().getWordToGuess());
        gameRepository.save(game);

        assertThrows(
                InvalidRoundException.class,
                () -> SERVICE.doGuess(id, "jooow")
        );
    }
}
