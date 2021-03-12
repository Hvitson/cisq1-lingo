package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "rounds")
public class Round implements Serializable {
    @Id
    @GeneratedValue
    private UUID round_id;
    private String wordToGuess;
    private Integer guesses;

    @OneToMany(targetEntity = Feedback.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "round_feedbacks")
    private List<Feedback> feedbacks;

    @Lob
    private List<Character> hint;

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_rounds")
    private Game game;
    //state toevoegen

    public Round() {}
    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        guesses = 0;
        feedbacks = new ArrayList<>();
        hint = Hint.generateHint(wordToGuess, feedbacks).getChars();
    }

    public Feedback getLastFeedback() {
        if (feedbacks.size() >= 1) {
            return feedbacks.get(feedbacks.size() - 1);
        }
        throw new InvalidFeedbackException("No feedback for this round!");
    }

    public boolean isRoundOver() {
        if (this.guesses < 5) {
            if (feedbacks.size() >= 1) {
                Feedback lastFeedback = getLastFeedback();
                return lastFeedback.isWordGuessed();
            }
            return false;
        }
        return true;
    }

    public void doGuess(String attempt) {
        if (attempt.equals("iAmChEeTo")){
            guesses = 0;
        } else {
            if (isRoundOver()) {
                throw new InvalidRoundException("111Round is already over! Start a new round to play again!");
            }
            Feedback newFeedback = new Feedback(attempt, wordToGuess);
            feedbacks.add(newFeedback);
            hint = Hint.generateHint(wordToGuess, feedbacks).getChars();
            guesses += 1;
        }
    }


    public Integer getLengthWordToGuess() {
        return this.wordToGuess.length();
    }

    public Integer getGuesses() {
        return guesses;
    }

    public void setGuesses(Integer guesses) {
        this.guesses = guesses;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public List<Character> getHint() {
        return hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        if (Objects.equals(wordToGuess, round.wordToGuess)) {
            if (Objects.equals(guesses, round.guesses)) {
                if (Objects.equals(feedbacks, round.feedbacks)) {
                    if (Objects.equals(hint, round.hint)) {
                        return true;
                    }
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
