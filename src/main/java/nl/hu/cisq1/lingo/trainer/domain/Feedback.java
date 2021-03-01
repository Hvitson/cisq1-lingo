package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Feedback {
    private final String attempt;
    private final Word word;
    private final Hint hint;
    private final List<Mark> marks;

    public Feedback(String attempt, Word word, Hint lastHint) {
        //todo: zorgt voor onnodige tests? wordt opgegvangen binnen hints met InvalidHintFeedback()
//        if (attempt.length() != lastHint.getHint().size() || attempt.length() != word.getLength()) {
//            throw new InvalidFeedbackException("Invalid feedback");
//        }
        this.attempt = attempt;
        this.word = word;
        marks = createMarks(attempt, this.word);
        hint = Hint.playHint(lastHint, marks, word);
    }

        public static List<Mark> createMarks(String attempt, Word word) {
        List<Mark> marks = new ArrayList<>();
        List<Character> charactersAttempt = new ArrayList<>();
        List<Character> charactersWord = word.wordToChars();

        for (char character : attempt.toCharArray()) {
            charactersAttempt.add(character);
        }

        if (attempt.equals("")) {
            for (int i = 0; i < charactersWord.size(); i++) {
                if (i==0) {
                    marks.add(CORRECT);
                } else {
                    marks.add(ABSENT);
                }
            }
            return marks;
        }

        if (attempt.length() != word.getLength()) {
            //todo(na overleg?): opvangen door InvalidFeedbackException waarom fout opslaan?

            for (int i = 0; i< attempt.length(); i++) {
                marks.add(INVALID);
            }
            return marks;
        }



        //invalid toevoegen abi lan
        for (int i = 0; i < charactersWord.size(); i++) {
            if (charactersAttempt.get(i) == charactersWord.get(i)) {
                marks.add(CORRECT);
            } else if (charactersWord.contains(charactersAttempt.get(i))) {
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

    boolean isGuessInvalid() {
        return marks.stream().anyMatch(mark -> mark == INVALID);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(marks, feedback.marks) && Objects.equals(hint, feedback.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks, hint);
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
