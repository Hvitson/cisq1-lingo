package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private final Word wordToGuess;
    private Integer guesses;
    private final List<Feedback> feedbacks;

    public Round(Word wordToGuess) {
        this.wordToGuess = wordToGuess;
        guesses = 0;
        this.feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback("", wordToGuess, Hint.createFirstHint(wordToGuess)));
    }

    //tests
    public Feedback getLastFeedback() {
        System.out.println("feedback.size() : "+feedbacks.size());
        return this.feedbacks.get(feedbacks.size() - 1);
    }

    public boolean isRoundOver() {
        Feedback lastFeedback = getLastFeedback();
        if (this.guesses < 5) {
            return lastFeedback.isWordGuessed();
        }
        return true;
    }

    public Feedback doGuess(String attempt) {
        Feedback feedback = new Feedback(attempt, wordToGuess, getLastFeedback().getHint());
        return feedback;
        //ge
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
