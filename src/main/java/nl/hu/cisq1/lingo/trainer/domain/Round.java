package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Round {
    private final UUID id;
    private final Word word;
    private Integer guesses;
    private final List<Feedback> feedbacks;

    public Round(Word word) {
        id = UUID.randomUUID();
        this.word = word;
        guesses = 0;
        this.feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback("", word, Hint.createFirstHint(word)));
    }

    //tests
    public Feedback getLastFeedback() {
        System.out.println("feedback.size() : "+feedbacks.size());
        return this.feedbacks.get(feedbacks.size() - 1);
    }

    public boolean isRoundOver() {
        Feedback lastFeedback = getLastFeedback();
        System.out.println("feedback volgens getLastFeedback" + lastFeedback);
        if (this.guesses < 5) {
            return lastFeedback.isWordGuessed();
        }
        return true;
    }

    //todo: functie voor een guess op round

    public void setGuesses(Integer guesses) {
        this.guesses = guesses;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", word=" + word +
                ", guesses=" + guesses +
                ", feedbacks=" + feedbacks +
                '}';
    }
}
