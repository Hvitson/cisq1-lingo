//package nl.hu.cisq1.lingo.trainer.presentation;
//
//import nl.hu.cisq1.lingo.trainer.application.TrainerService;
//import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
//import nl.hu.cisq1.lingo.trainer.domain.Game;
//import nl.hu.cisq1.lingo.trainer.presentation.controller.TrainerController;
//import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponse;
//import nl.hu.cisq1.lingo.words.application.WordService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TrainerControllerTest {
//    SpringGameRepository gameRepository = mock(SpringGameRepository.class);
//    WordService wordService = mock(WordService.class);
//    TrainerService service = new TrainerService(gameRepository, wordService);
//    TrainerController controller = mock(TrainerController.class);
//    Game game = new Game();
//
//    @BeforeEach
//    void createMocks() {
//        when(wordService.provideRandomWord(5)).thenReturn("appel");
//        when(gameRepository.findGameByGameId(2L)).thenReturn(Optional.of(game));
//        when(service.getAllGames()).thenReturn(List.of(game));
//        game.createRound(wordService.provideRandomWord(5));
//    }
//
//    @Test
//    @DisplayName("change game to GameResponse")
//    void createGameResponse() {
//        when(controller.GetGames()).thenReturn(ResponseEntity.ok(List.of(new GameResponse(game))));
//        ResponseEntity<List<GameResponse>> games = controller.GetGames();
//        System.out.println(games.toString());
//    }
//}
