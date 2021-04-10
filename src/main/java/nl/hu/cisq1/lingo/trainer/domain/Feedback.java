package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

@Entity(name = "feedbacks")
public class Feedback implements Serializable {
    @Id
    @GeneratedValue
    private Long feedbackId;
    private String attempt;
    private String wordToGuess;

    @Enumerated
    @ElementCollection
    private List<Mark> marks;

    public Feedback() {};
    public Feedback(String attempt, String wordToGuess) {
        this.attempt = attempt.toLowerCase();
        this.wordToGuess = wordToGuess;
        marks = createMarks(attempt, this.wordToGuess);
    }

    public static List<Mark> createMarks(String attempt, String wordToGuess) {
        List<Mark> marks = new ArrayList<>();

        if (attempt == null || attempt.length() != wordToGuess.length()) {
            marks.add(INVALID);
            return marks;
        }

        List<Character> charsAttempt = new ArrayList<>();
        List<Character> charsWordToGuess = new ArrayList<>();

        for (Character character : wordToGuess.toCharArray()) {
            charsWordToGuess.add(character);
            marks.add(ABSENT);
        }
        for (char character : attempt.toCharArray()) {
            charsAttempt.add(character);
        }

        for (int i = 0; i < charsWordToGuess.size(); i++) {
            if (charsAttempt.get(i).equals(charsWordToGuess.get(i))) {
                charsWordToGuess.set(i, null);
                charsAttempt.set(i, null);
                marks.set(i, CORRECT);
            }
        }
        for (int i = 0; i < charsWordToGuess.size(); i++) {
            if (charsAttempt.get(i) != null && charsWordToGuess.contains(charsAttempt.get(i))) {
                int y = charsWordToGuess.indexOf(charsAttempt.get(i));
                charsWordToGuess.set(y, null);
                marks.set(i, PRESENT);
            }
        }
        return marks;
    }

    public boolean isWordGuessed() {
        return marks.stream().allMatch(mark -> mark == CORRECT);
    }

    public boolean isGuessValid() {
        return marks.stream().noneMatch(mark -> mark == INVALID);
    }

    public String getAttempt() {
        return attempt;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(wordToGuess, feedback.wordToGuess) && Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, wordToGuess, marks);
    }
}
