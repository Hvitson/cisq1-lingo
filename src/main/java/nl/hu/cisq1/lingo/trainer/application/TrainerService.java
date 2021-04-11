package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public TrainerService(SpringGameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public Game findGame(Long id) throws NotFoundException {
        return gameRepository.findById(id).orElseThrow(() -> new NotFoundException("Game with id " + id + " not found"));
    }

    public Game createGame() {
        Game game = new Game();
        game.createRound(wordService.provideRandomWord(5));
        gameRepository.save(game);

        return game;
    }


    public Game createRound(Long id) throws InvalidRoundException, NotFoundException {
        Game game = findGame(id);
        Integer lengthNextWord = game.lengthNextWordToGuess();
        game.createRound(wordService.provideRandomWord(lengthNextWord));

        return this.gameRepository.save(game);
    }

    public Game doGuess(Long id, String attempt) throws InvalidRoundException, NotFoundException {
        Game game = findGame(id);
        game.doGuess(attempt);
        return this.gameRepository.save(game);
    }
}
