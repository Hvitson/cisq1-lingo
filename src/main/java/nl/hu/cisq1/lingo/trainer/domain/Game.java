package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.state.StateGame;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.state.StateGame.*;
import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;

@Entity(name = "games")
public class Game implements Serializable {
    @Id
    @GeneratedValue
    private Long gameId;
    private Integer playingRoundNumber;
    private Integer score;
    private StateGame state;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Round> rounds = new ArrayList<>();

    public Game() {
        this.playingRoundNumber = 0;
        this.state = WAITING_FOR_ROUND;
        this.score = 0;
    }

    public void createRound(String wordToGuess) throws InvalidRoundException {
        if (rounds.isEmpty() && state == WAITING_FOR_ROUND || state != PLAYING_ROUND && getLastRound().getState() != WAITING_FOR_INPUT ) {
            rounds.add(new Round(wordToGuess));
            playingRoundNumber += 1;
            state = PLAYING_ROUND;
        }
        else {
            throw new InvalidRoundException("finish your last round before you start a new one!");
        }
    }

    public Round getLastRound() throws IndexOutOfBoundsException {
        if (rounds.isEmpty()) {
            throw new IndexOutOfBoundsException("you have to create a round before you can get it");
        }
        return rounds.get(rounds.size() - 1);
    }

    public void doGuess(String attempt) throws InvalidRoundException {
        if (this.getRounds().isEmpty()) {
            throw new InvalidRoundException("you have to start a round before you can play one!");
        }
        Round currentRound = getLastRound();
        if (currentRound.getState() == WAITING_FOR_INPUT) {
            currentRound.doGuess(attempt);
            if (currentRound.getState() != WAITING_FOR_INPUT) {
                score += calculateScore(currentRound.getGuesses());
                state = WAITING_FOR_ROUND;
            }
        } else {
            throw new InvalidRoundException("Round already over, start a new round!");
        }
    }

    public Integer calculateScore(Integer guesses) {
        return (5 * ( 5 - guesses )) + 5;
    }

    public Integer lengthNextWordToGuess() throws InvalidRoundException {
        Integer numba = this.getLastRound().getLengthWordToGuess();
        if (numba == 5) {
            return 6;
        } else if (numba == 6) {
            return 7;
        } else if (numba == 7) {
            return 5;
        } else {
            throw new InvalidRoundException("Length not supported");
        }
    }

    public Long getGameId() {
        return gameId;
    }

    public Integer getPlayingRoundNumber() {
        return playingRoundNumber;
    }

    public Integer getScore() {
        return score;
    }

    public StateGame getState() {
        return state;
    }

    public List<Round> getRounds() {
        return rounds;
    }
}
