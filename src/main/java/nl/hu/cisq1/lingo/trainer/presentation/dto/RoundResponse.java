package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.state.StateRound;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoundResponse implements Serializable {
    @NotNull
    private final Long roundId;
    @NotNull
    private final StateRound stateRound;
    @NotNull
    private final Integer guesses;
    @NotNull
    private final List<Character> hint;
    @Nullable
    private List<FeedbackResponse> feedbackList;

    public RoundResponse(Round round) {
        this.roundId = round.getRoundId();
        this.stateRound = round.getState();
        this.guesses = round.getGuesses();
        this.hint = round.getHint();
        this.feedbackList = new ArrayList<>();
        for (Feedback feedback : round.getFeedbacks()) {
            feedbackList.add(new FeedbackResponse(feedback));
        }
    }

    public Long getRoundId() {
        return roundId;
    }

    public StateRound getStateRound() {
        return stateRound;
    }

    public Integer getGuesses() {
        return guesses;
    }

    public List<Character> getHint() {
        return hint;
    }

    @Nullable
    public List<FeedbackResponse> getFeedbackList() {
        return feedbackList;
    }
}
