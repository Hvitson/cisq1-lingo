package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private UUID game_id;
    private Integer score;

    @OneToMany(targetEntity = Round.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_rounds")
    private final List<Round> rounds = new ArrayList<>();

    public Game() {
        score = 0;
    }

    public Integer lengthNextWordToGuess() {
        if (getLastRound().isRoundOver()) {
            Integer numba = this.getLastRound().getLengthWordToGuess();
            if (numba == 0) {
                return 5;
            } else if (numba == 5) {
                return 6;
            } else if (numba == 6) {
                return 7;
           } else if (numba == 7) {
                return 5;
           } else {
               throw new InvalidRoundException("Length not supported");
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

    public void setScore(Integer score) {
        this.score = score;
    }

    public void createRound(String wordToGuess) {
        if (rounds.isEmpty()) {
            rounds.add(new Round(wordToGuess));
        }
        if (isRoundOver()) {
            rounds.add(new Round(wordToGuess));
        } else {
            throw new InvalidRoundException("finish your last round before you start a new one!");
        }
    }

    public List<Round> getRounds() {
        return rounds;
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
