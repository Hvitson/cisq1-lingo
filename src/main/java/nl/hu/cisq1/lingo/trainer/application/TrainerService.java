package nl.hu.cisq1.lingo.trainer.application;


import ch.qos.logback.core.recovery.ResilientOutputStreamBase;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGameException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public TrainerService(SpringGameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game findGame(Long id) throws InvalidGameException {
        return gameRepository.findGameByGameId(id).orElseThrow(() -> new InvalidGameException("Game with id " + id + " not found"));
    }

    public Game startGame() {
        Game game = new Game();
        game.createRound(wordService.provideRandomWord(5));

        gameRepository.save(game);

        return game;
    }


    public Game startRound(Long id) throws InvalidRoundException {
        Game game = findGame(id);
        Integer lengthNextWord = game.lengthNextWordToGuess();
        game.createRound(wordService.provideRandomWord(lengthNextWord));

        gameRepository.save(game);

        return game;
    }

    public Game doGuess(Long id, String attempt) {
        Game game = findGame(id);
        game.doGuess(attempt);

        gameRepository.save(game);

        return game;
    }
}
