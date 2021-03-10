package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Feedback {
    private final String attempt;
    private final Word wordToGuess;
    private final List<Mark> marks;

    public Feedback(String attempt, Word wordToGuess) {
        this.attempt = attempt.toLowerCase();
        this.wordToGuess = wordToGuess;
        marks = createMarks(attempt, this.wordToGuess);
    }

    public static List<Mark> createMarks(String attempt, Word wordToGuess) {
        if (attempt == null) {
            throw new InvalidFeedbackException("attempt can't be null!");
        }
        List<Mark> marks = new ArrayList<>();
        List<Character> charactersAttempt = new ArrayList<>();
        List<Character> charactersWordToGuess = wordToGuess.wordToChars();

        for (char character : attempt.toCharArray()) {
            charactersAttempt.add(character);
        }
        if (attempt.length() != wordToGuess.getLength()) {
            if(attempt.length() <= 0) {
                marks.add(INVALID);
            }
            for (int i = 0; i< attempt.length(); i++) {
                marks.add(INVALID);
            }
            return marks;
        }

        for (int i = 0; i < charactersWordToGuess.size(); i++) {
            if (charactersAttempt.get(i) == charactersWordToGuess.get(i)) {
                marks.add(CORRECT);
            } else if (charactersWordToGuess.contains(charactersAttempt.get(i))) {
                marks.add(PRESENT);
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
