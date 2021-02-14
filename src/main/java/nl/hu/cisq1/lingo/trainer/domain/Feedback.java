package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;
import static nl.hu.cisq1.lingo.trainer.domain.Mark.INVALID;

public class Feedback {
    private final String attempt;
    private final List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;
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
}
