package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Feedback {
    private final String attempt;
    private final Word wordToGuess;
    private final Hint hint;
    private final List<Mark> marks;

    public Feedback(String attempt, Word wordToGuess, Hint lastHint) {
        //todo: zorgt voor onnodige tests? wordt opgegvangen binnen hints met InvalidHintFeedback()
        //todo: toLowerCase() nodig?
        this.attempt = attempt.toLowerCase();
        this.wordToGuess = wordToGuess;
        marks = createMarks(attempt, this.wordToGuess);
        hint = Hint.playHint(lastHint, marks, wordToGuess);
    }

    public static List<Mark> createMarks(String attempt, Word wordToGuess) {
        List<Mark> marks = new ArrayList<>();
        List<Character> charactersAttempt = new ArrayList<>();
        List<Character> charactersWordToGuess = wordToGuess.wordToChars();

        for (char character : attempt.toCharArray()) {
            charactersAttempt.add(character);
        }

        if (attempt.equals("")) {
            for (int i = 0; i < charactersWordToGuess.size(); i++) {
                if (i==0) {
                    marks.add(CORRECT);
                } else {
                    marks.add(ABSENT);
                }
            }
            return marks;
        }

        if (attempt.length() != wordToGuess.getLength()) {
            //todo(na overleg?): opvangen door InvalidFeedbackException waarom fout opslaan?
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

    boolean isGuessValid() {
        return marks.stream().noneMatch(mark -> mark == INVALID);
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public Hint getHint() {
        return hint;
    }

    //todo: fix tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(wordToGuess, feedback.wordToGuess) && Objects.equals(hint, feedback.hint) && Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, wordToGuess, hint, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                ", hint=" + hint +
                '}';
    }
}
