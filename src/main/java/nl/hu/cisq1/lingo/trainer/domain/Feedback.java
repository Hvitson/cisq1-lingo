package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

@Entity(name = "feedbacks")
public class Feedback implements Serializable {
    @Id
    @GeneratedValue
    private UUID feedback_id;
    private String attempt;
    private String wordToGuess;

    @Lob //anders?
    private List<Mark> marks;

    @ManyToOne(targetEntity = Round.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "round_feedbacks")
    private Round round;

    public Feedback() {};
    public Feedback(String attempt, String wordToGuess) {
        this.attempt = attempt.toLowerCase();
        this.wordToGuess = wordToGuess;
        marks = createMarks(attempt, this.wordToGuess);
    }

    public static List<Mark> createMarks(String attempt, String wordToGuess) {
        if (attempt == null) {
            throw new InvalidFeedbackException("attempt can't be null!");
        }
        List<Mark> marks = new ArrayList<>();
        List<Character> charsAttempt = new ArrayList<>();
        List<Character> charsWordToGuess = new ArrayList<>();

        for (Character character : wordToGuess.toCharArray()) {
            charsWordToGuess.add(character);
        }

        for (char character : attempt.toCharArray()) {
            charsAttempt.add(character);
        }
        if (attempt.length() != charsWordToGuess.size()) {
            if(attempt.length() <= 0) {
                marks.add(INVALID);
            }
            for (int i = 0; i< attempt.length(); i++) {
                marks.add(INVALID);
            }
            return marks;
        }

        for (int i = 0; i < charsWordToGuess.size(); i++) {
            if (charsAttempt.get(i).equals(charsWordToGuess.get(i))) {
                marks.add(CORRECT);
                charsWordToGuess.set(i, '.');
            } else if (charsWordToGuess.contains(charsAttempt.get(i))) {
                marks.add(PRESENT);
                charsWordToGuess.set(i, '.');
            } else {
                marks.add(ABSENT);
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

    public List<Mark> getMarks() {
        return marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        //todo: return blijft geel bij tests + -> verschillende manieren geraakt
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(wordToGuess, feedback.wordToGuess) && Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, wordToGuess, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                '}';
    }
}
