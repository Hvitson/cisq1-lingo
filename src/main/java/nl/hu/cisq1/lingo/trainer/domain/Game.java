package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;
import static nl.hu.cisq1.lingo.trainer.domain.GameState.*;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Round> rounds;
    private Integer score;
//    private GameState gameState;

    public Game() {
        rounds = new ArrayList<>();
        //todo: fix via controla
        rounds.add(createRound());
        score = 0;
//        gameState = PLAYING;
    }

    private void lengthNextWordToGuess() {
        if (isRoundOver()) {
            Integer numba = this.getLastRound().getLengthWordToGuess();
           if (numba == 5) {
               //maak wordToGuess met 6 letters
               createRound();
           } else if (numba == 6) {
               //maak wordToGuess met 7 letters
               createRound();
           } else if (numba == 7) {
               //maak wordToGuess met 5 letters
               createRound();
           } else {
               //Invalid iets gooien?
               throw new InvalidRoundException("Length incorrect");
           }
        } else {
            throw new InvalidRoundException("finish your last round before you start a new one!");
        }
    }

    //todo: testen? al 280572 keer getest b4
    private boolean isRoundOver() {
        return this.getLastRound().isRoundOver();
    }

    public Round getLastRound() {
        return rounds.get(rounds.size() - 1);
    }

    //todo: fix via controla
    public Round createRound() {
        //new Word vervangen door functie aan de hand van lengte volgende word zijn lengte bepaalt en ophaalt
        return new Round(new Word("wonen"));
    }

    public void startRound() {
        System.out.println("is it tho?: " + this.getLastRound().isRoundOver());
        if (isRoundOver()) {
            rounds.add(createRound());
        } else {
            throw new InvalidRoundException("finish your last round before you start a new one!");
        }
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "Game{" +
                "rounds=" + rounds +
                ", score=" + score +
                '}';
    }
}
