package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.state.StateRound;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.state.StateRound.*;

@Entity(name = "rounds")
public class Round implements Serializable {
    @Id
    @GeneratedValue
    private Long roundId;
    private String wordToGuess;
    private StateRound state;
    private Integer guesses;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @ElementCollection
    private List<Character> hint;

    public Round() {}
    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.state = WAITING_FOR_INPUT;
        this.guesses = 0;
        this.feedbacks = new ArrayList<>();
        this.hint = Hint.generateHint(wordToGuess, feedbacks).getChars();
    }

    public Feedback getLastFeedback() throws IndexOutOfBoundsException {
        if (feedbacks.isEmpty()) {
            throw new IndexOutOfBoundsException("This round does not contain any feedback");
        }
        return feedbacks.get(feedbacks.size() - 1);
    }

    public void doGuess(String attempt) throws InvalidRoundException {
        if (this.getState() == WAITING_FOR_INPUT) {
            Feedback newFeedback = new Feedback(attempt, this.wordToGuess);
            this.feedbacks.add(newFeedback);
            if (this.getLastFeedback().isGuessValid()) {
                this.hint = Hint.generateHint(wordToGuess, feedbacks).getChars();
            }
            this.guesses += 1;
            if (this.getLastFeedback().isWordGuessed()) {
                this.state = WON;
            }
            if (guesses == 5) {
                this.state = LOST;
            }
        } else {
            throw new InvalidRoundException("Round is over!");
        }
    }

    public Long getRoundId() {
        return roundId;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public Integer getLengthWordToGuess() {
        return this.wordToGuess.length();
    }

    public StateRound getState() {
        return state;
    }

    public Integer getGuesses() {
        return guesses;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public List<Character> getHint() {
        return hint;
    }

    public void setGuesses(Integer guesses) {
        this.guesses = guesses;
    }

    public void setState(StateRound state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return Objects.equals(roundId, round.roundId) &&
                wordToGuess.equals(round.wordToGuess) &&
                state == round.state &&
                guesses.equals(round.guesses) &&
                Objects.equals(feedbacks, round.feedbacks) &&
                Objects.equals(hint, round.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordToGuess, guesses, feedbacks, hint);
    }
}
