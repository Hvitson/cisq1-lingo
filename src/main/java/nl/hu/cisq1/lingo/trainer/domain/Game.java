package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;
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
    @Column(name = "game_id")
    private UUID uuid;
    private Integer score;

    @OneToMany(targetEntity = Round.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "rounds", referencedColumnName = "round_id")
    private final List<Round> rounds = new ArrayList<>();

    public Game() {
        uuid = UUID.randomUUID();
        score = 0;
        //todo: fix via controla
        rounds.add(createRound());
    }

//    private void lengthNextWordToGuess() {
//        if (getLastRound().isRoundOver()) {
//            Integer numba = this.getLastRound().getLengthWordToGuess();
//           if (numba == 5) {
//               //maak wordToGuess met 6 letters
//               createRound();
//           } else if (numba == 6) {
//               //maak wordToGuess met 7 letters
//               createRound();
//           } else if (numba == 7) {
//               //maak wordToGuess met 5 letters
//               createRound();
//           } else {
//               throw new WordLengthNotSupportedException(numba);
//           }
//        } else {
//            throw new InvalidRoundException("finish your last round before you start a new one!");
//        }
//    }

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

    //todo: fix via controla
    public Round createRound() {
        //new Word vervangen door functie aan de hand van lengte volgende word zijn lengte bepaalt en ophaalt
        return new Round(new Word("wonen"));
    }

    public void startRound() {
        if (isRoundOver()) {
            rounds.add(createRound());
        } else {
            throw new InvalidRoundException("finish your last round before you start a new one!");
        }
    }

    //game guess?

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
