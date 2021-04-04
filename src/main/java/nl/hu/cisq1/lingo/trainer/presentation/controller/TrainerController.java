//package nl.hu.cisq1.lingo.trainer.presentation.controller;
//
//import nl.hu.cisq1.lingo.trainer.application.TrainerService;
//import nl.hu.cisq1.lingo.trainer.domain.Game;
//import nl.hu.cisq1.lingo.trainer.domain.Round;
//import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGameException;
//import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
//import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponse;
//import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/games")
//public class TrainerController {
//    private final TrainerService SERVICE;
//
//    public TrainerController(TrainerService SERVICE) {
//        this.SERVICE = SERVICE;
//    }
//
//    private GameResponse convertGameToResponse(Game game) {
//        return new GameResponse(game);
//    }
//
//    private RoundResponse convertRoundToResponse(Round round) {
//        return new RoundResponse(round);
//    }
//
//    private List<GameResponse> convertGameListToResponse(List<Game> game) {
//        return game.stream()
//                .map(this::convertGameToResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @GetMapping
//    public ResponseEntity<List<GameResponse>> GetGames() {
//        List<Game> games = this.SERVICE.getAllGames();
//
//        return new ResponseEntity<>(convertGameListToResponse(games), HttpStatus.OK);
//    }
//
//
//    @GetMapping("/{gameId}")
//    public ResponseEntity<GameResponse> GetRound(@PathVariable Long gameId) throws InvalidGameException {
//        Game game = this.SERVICE.findGame(gameId);
//
//        return new ResponseEntity<>(convertGameToResponse(game), HttpStatus.OK);
//    }
//
//    @GetMapping("/currentRound/{gameId}")
//    public ResponseEntity<RoundResponse> GetGame(@PathVariable Long gameId) throws InvalidGameException {
//        Game game = this.SERVICE.findGame(gameId);
//
//        return new ResponseEntity<>(convertRoundToResponse(game.getLastRound()), HttpStatus.OK);
//    }
//
//    @PostMapping("/createGame")
//    public ResponseEntity<GameResponse> startGame() {
//        Game game = this.SERVICE.startGame();
//        return new ResponseEntity<>(convertGameToResponse(game),HttpStatus.CREATED);
//    }
//
//    @PostMapping("/createRound/{gameId}")
//    public ResponseEntity<RoundResponse> startRound(@PathVariable Long gameId) throws InvalidGameException, InvalidRoundException {
//        Game game = this.SERVICE.startRound(gameId);
//
//        return new ResponseEntity<>(convertRoundToResponse(game.getLastRound()), HttpStatus.CREATED);
//    }
//
//    @PostMapping("/{gameId}/{attempt}")
//    public ResponseEntity<RoundResponse> startRound(@PathVariable Long gameId, @PathVariable String attempt) throws InvalidGameException, InvalidRoundException {
//        Game game = this.SERVICE.doGuess(gameId, attempt);
//
//        return new ResponseEntity<>(convertRoundToResponse(game.getLastRound()), HttpStatus.CREATED);
//    }
//
//}
