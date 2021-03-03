package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HintTest {
    private static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT), new Hint(List.of('w','.','.','.','.'))),
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("woest"), List.of(CORRECT, CORRECT, ABSENT, ABSENT, ABSENT), new Hint(List.of('w','o','.','.','.'))),
                Arguments.of(new Hint(List.of('w','o','.','.','.')), new Word("woerd"), List.of(CORRECT, CORRECT, ABSENT, CORRECT, CORRECT), new Hint(List.of('w','o','.','r','d'))),
                Arguments.of(new Hint(List.of('w','o','.','r','d')), new Word("woord"), List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), new Hint(List.of('w','o','o','r','d')))
        );
    }

    private static Stream<Arguments> provideWrongMarksExamples() {
        return Stream.of(
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID)),
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(INVALID, INVALID, INVALID, INVALID, INVALID)),
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(INVALID, INVALID, INVALID, INVALID)),
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(CORRECT, INVALID,INVALID, INVALID, INVALID))
        );
    }

    @Test
    @DisplayName("hint is created correctly")
    void hintCreatedCorrectly() {
        Hint hint = new Hint(List.of('w','.','.','.','.'));
        assertEquals(List.of('w','.','.','.','.'), hint.getHint());
    }

    @Test
    @DisplayName("hint is created correctly when hint is null")
    void hintCreatedCorrectlyWithNull() {
        Hint hint = Hint.playHint(null, List.of(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT), new Word("wonen"));
        assertEquals(List.of('w','.','.','.','.'), hint.getHint());
    }

    @Test
    @DisplayName("hint is created correctly when marks contain invalid")
    void hintCreatedCorrectlyWithInvalid() {
        Hint hint = Hint.playHint(null, List.of(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT), new Word("wonen"));
        assertEquals(List.of('w','.','.','.','.'), hint.getHint());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("hints are correct after update")
    void hintIsCorrect(Hint lastHint, Word wordToGuess, List<Mark> marks, Hint expectedHint) {
        Hint hint = Hint.playHint(lastHint, marks, wordToGuess);
        assertEquals(expectedHint.getHint(), hint.getHint());
    }

    @ParameterizedTest
    @MethodSource("provideWrongMarksExamples")
    @DisplayName("if marks contain invalid last hint is new hint")
    void hintIsCorrect(Hint lastHint, Word wordToGuess, List<Mark> marks) {
        Hint hint = Hint.playHint(lastHint, marks, wordToGuess);

        assertEquals(lastHint.getHint(), hint.getHint());
    }
}
