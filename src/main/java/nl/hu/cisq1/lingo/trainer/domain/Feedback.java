package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Feedback {
    private final String attempt;
    private final List<Mark> marks;
    private final Hint hint;

    public Feedback(String attempt, List<Mark> marks, Hint hint) {
        this.attempt = attempt;
        this.marks = marks;
        this.hint = hint;

    }

    boolean isWordGuessed() {
        return marks.stream().allMatch(mark -> mark == CORRECT);
    }

    boolean isGuessValid() {
        return marks.stream().noneMatch(mark -> mark == INVALID);
    }

    boolean isGuessInvalid() {
//        return marks.stream().anyMatch(mark -> mark == INVALID);
        return !isGuessValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return attempt.equals(feedback.attempt) && marks.equals(feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                '}';
    }
}
