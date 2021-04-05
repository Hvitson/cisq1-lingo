package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
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
        state = WAITING_FOR_INPUT;
        guesses = 0;
        this.feedbacks = new ArrayList<>();
        this.hint = Hint.generateHint(wordToGuess, feedbacks).getChars();
    }

    public Feedback getLastFeedback() throws InvalidFeedbackException {
        if (!feedbacks.isEmpty()) {
            return feedbacks.get(feedbacks.size() - 1);
        }
        throw new InvalidFeedbackException("This round does not contain any feedback");
    }

    public void doGuess(String attempt) throws InvalidRoundException {
        if (this.getState() == WAITING_FOR_INPUT) {
            Feedback newFeedback = new Feedback(attempt, wordToGuess);
            feedbacks.add(newFeedback);
            hint = Hint.generateHint(wordToGuess, feedbacks).getChars();
            guesses += 1;
            if (attempt.equals(wordToGuess)) {
                state = WON;
            }
            if (guesses == 5) {
                state = LOST;
            }
        } else {
            throw new InvalidRoundException("Round already over!");
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
        if (Objects.equals(wordToGuess, round.wordToGuess)) {
            if (Objects.equals(guesses, round.guesses)) {
                if (Objects.equals(feedbacks, round.feedbacks)) {
                    return Objects.equals(hint, round.hint);
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordToGuess, guesses, feedbacks, hint);
    }

    @Override
    public String toString() {
        return "Round{" +
                "wordToGuess=" + wordToGuess +
                ", guesses=" + guesses +
                ", feedbacks=" + feedbacks +
                '}';
    }
}
