package nl.hu.cisq1.lingo.trainer.application;


import ch.qos.logback.core.recovery.ResilientOutputStreamBase;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGameException;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final SpringWordRepository wordRepository;

    public TrainerService(SpringGameRepository gameRepository, SpringWordRepository wordRepository) {
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
    }

    public Game FindGame(UUID id) {
        return gameRepository.findByGame_id(id).orElseThrow(() -> new InvalidGameException("Game with id "+ id +" not found"));
    }

    public Round startGame() {
        Game game = new Game();
        game.createRound(wordRepository.findRandomWordByLength(5).get().getValue());

        return game.getLastRound();
    }

    public Round doGuess(UUID id,String attempt) {
        Game game = FindGame(id);
        Round round = game.getLastRound();
        round.doGuess(attempt);

        return game.getLastRound();
    }

    public Round startGameRound(UUID id) {
        Game game = FindGame(id);
        Integer lengthNextWord = game.lengthNextWordToGuess();
        game.createRound(wordRepository.findRandomWordByLength(lengthNextWord).get().getValue());

        return game.getLastRound();
    }

}
