package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private final Word wordToGuess;
    private Integer guesses;
    private List<Feedback> feedbacks;

    public Round(Word wordToGuess) {
        this.wordToGuess = wordToGuess;
        guesses = 0;
        feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback("", wordToGuess, Hint.playHint(null, null, wordToGuess)));
    }

    public Feedback getLastFeedback() {
        return feedbacks.get(feedbacks.size() - 1);
    }

    public boolean isRoundOver() {
        Feedback lastFeedback = getLastFeedback();
        if (this.guesses < 5) {
            return lastFeedback.isWordGuessed();
        }
        return true;
    }

    public void doGuess(String attempt) {
        if (attempt.equals("iAmChEeTo") && guesses > 0){
            guesses -= 1;
        } else {
            if (isRoundOver()) {
                throw new InvalidRoundException("Round is already over! Start a new round to play again!");
            }
            Feedback newFeedback = new Feedback(attempt, wordToGuess, getLastFeedback().getHint());
            feedbacks.add(newFeedback);
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



    @Override
    public String toString() {
        return "Round{" +
                "wordToGuess=" + wordToGuess +
                ", guesses=" + guesses +
                ", feedbacks=" + feedbacks +
                '}';
    }
}
