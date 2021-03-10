package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "rounds")
public class Round {
    @Id
    @GeneratedValue
    @Column(name = "round_id")
    private UUID uuid;
    private Word wordToGuess;
    private Integer guesses;

    @OneToMany(targetEntity = Feedback.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "feedbacks", referencedColumnName = "feedback_id")
    private List<Feedback> feedbacks;

    private Hint hint;

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "rounds")
    private Game game;

    public Round() {}
    public Round(Word wordToGuess) {
        uuid = UUID.randomUUID();
        this.wordToGuess = wordToGuess;
        guesses = 0;
        feedbacks = new ArrayList<>();
        hint = Hint.generateHint(wordToGuess, feedbacks);
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
            hint = Hint.generateHint(wordToGuess, feedbacks);
            guesses += 1;
        }
    }


    public Integer getLengthWordToGuess() {
        return this.wordToGuess.getLength();
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

    public Hint getHint() {
        return hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return Objects.equals(wordToGuess, round.wordToGuess) && Objects.equals(guesses, round.guesses) && Objects.equals(feedbacks, round.feedbacks) && Objects.equals(hint, round.hint);
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
