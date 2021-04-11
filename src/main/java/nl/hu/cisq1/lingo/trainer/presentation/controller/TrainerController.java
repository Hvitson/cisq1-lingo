package nl.hu.cisq1.lingo.trainer.presentation.controller;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponse;
import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class TrainerController {
    private final TrainerService SERVICE;

    public TrainerController(TrainerService SERVICE) {
        this.SERVICE = SERVICE;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long gameId) throws NotFoundException {
        Game game = this.SERVICE.findGame(gameId);

        return new ResponseEntity<>(new GameResponse(game), HttpStatus.OK);
    }

    @GetMapping("/currentRound/{gameId}")
    public ResponseEntity<RoundResponse> getRound(@PathVariable Long gameId) throws NotFoundException {
        Game game = this.SERVICE.findGame(gameId);

        return new ResponseEntity<>(new RoundResponse(game.getLastRound()), HttpStatus.OK);
    }

    @PostMapping("/createGame")
    public ResponseEntity<GameResponse> createGame() {
        Game game = this.SERVICE.createGame();
        return new ResponseEntity<>(new GameResponse(game),HttpStatus.CREATED);
    }

    @PostMapping("/createRound/{gameId}")
    public ResponseEntity<RoundResponse> createRound(@PathVariable Long gameId) throws InvalidRoundException, NotFoundException {
        Game game = this.SERVICE.createRound(gameId);

        return new ResponseEntity<>(new RoundResponse(game.getLastRound()), HttpStatus.CREATED);
    }

    @PostMapping("/{gameId}/{attempt}")
    public ResponseEntity<RoundResponse> doGuessOnRound(@PathVariable Long gameId, @PathVariable String attempt) throws InvalidRoundException, NotFoundException {
        Game game = this.SERVICE.doGuess(gameId, attempt);

        return new ResponseEntity<>(new RoundResponse(game.getLastRound()), HttpStatus.OK);
    }

}
