package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Mark;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

public class FeedbackResponse implements Serializable {
    @Nullable
    private final String attempt;
    @Nullable
    private final List<Mark> marks;

    public FeedbackResponse(Feedback feedback) {
        this.attempt = feedback.getAttempt();
        this.marks = feedback.getMarks();
    }

    @Nullable
    public String getAttempt() {
        return attempt;
    }

    @Nullable
    public List<Mark> getMarks() {
        return marks;
    }
}
