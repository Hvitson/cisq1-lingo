package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.state.StateGame;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public Integer lengthNextWordToGuess() {
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

    public Round getLastRound() {
            return rounds.get(rounds.size() - 1);
    }

    public void createRound(String wordToGuess) throws InvalidRoundException {
        if (rounds.isEmpty() && this.state == WAITING_FOR_ROUND || getLastRound().getState() != WAITING_FOR_INPUT) {
            rounds.add(new Round(wordToGuess));
            playingRoundNumber += 1;
            state = PLAYING_ROUND;
        }
//        else if (getLastRound().getState() != WAITING_FOR_INPUT) {
//            rounds.add(new Round(wordToGuess));
//            playingRoundNumber += 1;
//            state = PLAYING_ROUND;
//        }
        else {
            throw new InvalidRoundException("finish your last round before you start a new one!");
        }
    }

    public void doGuess(String attempt) throws InvalidRoundException {
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

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(rounds, game.rounds) && Objects.equals(score, game.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rounds, score);
    }

    @Override
    public String toString() {
        return "Game{" +
                "rounds=" + rounds +
                ", score=" + score +
                '}';
    }
}
