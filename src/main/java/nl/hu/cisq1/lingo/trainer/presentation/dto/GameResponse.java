package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.state.StateGame;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameResponse implements Serializable {
    @NotNull
    private final Long gameId;
    @NotNull
    private final StateGame stateGame;
    @NotNull
    private final Integer playingRoundNumber;
    @NotNull
    private final Integer score;
    @NotNull
    private final List<RoundResponse> rounds;

    public GameResponse(Game game) {
        this.gameId = game.getGameId();
        this.stateGame = game.getState();
        this.playingRoundNumber = game.getPlayingRoundNumber();
        this.score = game.getScore();
        this.rounds = new ArrayList<>();
        for (Round round : game.getRounds()) {
            rounds.add(new RoundResponse(round));
        }
    }

    public Long getGameId() {
        return gameId;
    }

    public StateGame getStateGame() {
        return stateGame;
    }

    public Integer getPlayingRoundNumber() {
        return playingRoundNumber;
    }

    public Integer getScore() {
        return score;
    }

    public List<RoundResponse> getRounds() {
        return rounds;
    }
}
