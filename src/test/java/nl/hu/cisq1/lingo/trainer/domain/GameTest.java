package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    private Game game;
    private Word wordToGuess;

    @BeforeEach
    void createStartGame() {
        game = new Game();
        wordToGuess = new Word("wonen");
    }

    @Test
    @DisplayName("get last round from created game")
    void getLastRound() {
        assertEquals(new Round(wordToGuess).toString(), game.getLastRound().toString());
    }

    //todo: how to test?
//    @Test
//    @DisplayName("get last round from created game")
//    void createRound() {
//
//        assertEquals( game.getLastRound().toString());
//    }

    //todo: tip voor dit? want moet straks aangeroepen worden en
    // wordt dan lastiger en onoverzichtelijker dan nu?
    @Test
    @DisplayName("get last round from created game when there are 2 games")
    void getLastRoundFromRounds() {
        Round toBe = new Round(new Word("wonen"));
        game.getLastRound().doGuess("wonen");
        game.startRound();

        assertEquals(toBe.toString(), game.getLastRound().toString());
        assertEquals(2, game.getRounds().size());
    }

    //tests voor Invalid...Exceptions



}
