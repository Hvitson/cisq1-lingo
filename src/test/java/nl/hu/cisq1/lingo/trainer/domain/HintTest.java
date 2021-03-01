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

    @Test
    @DisplayName("hint is created correctly")
    void hintCreatedCorrectly() {
        Hint hint = new Hint(List.of('w','.','.','.','.'));
        assertEquals(new Hint(List.of('w','.','.','.','.')).getHint(), hint.getHint());
    }

    @Test
    @DisplayName("hint is created correctly when hint is null")
    void hintCreatedCorrectlyWithNull() {
        assertEquals(new Hint(List.of('w','.','.','.','.')).getHint(), Hint.playHint(null, List.of(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT), new Word("wonen")).getHint());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("hints is correct after update")
    void hintIsCorrect(Hint lastHint, Word value, List<Mark> marks, Hint expectedHint) {
        Hint hint = Hint.playHint(lastHint, marks, value);

        assertEquals(expectedHint.getHint(), hint.getHint());
    }

    //Incorrect hints tests
    private static Stream<Arguments> provideHintIncorrectExamples() {
        return Stream.of(
                //length of marks invalid not same length as word
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(CORRECT, PRESENT, ABSENT, ABSENT)),
                //length of previousHint invalid not same length as word
                Arguments.of(new Hint(List.of('w','.','.','.')), new Word("woest"), List.of(CORRECT, CORRECT, ABSENT, ABSENT, ABSENT)),
                //length of marks and previousHint not same length as word
                Arguments.of(new Hint(List.of('w','o','.','.','.')), new Word("woer"), List.of(CORRECT, CORRECT, ABSENT, CORRECT, CORRECT)),
                //marks INVALID
                Arguments.of(new Hint(List.of('w','o','.','r','d')), new Word("woord"), List.of(INVALID, INVALID, INVALID, INVALID, INVALID)),
                Arguments.of(new Hint(List.of('w','o','.','r','d')), new Word("woord"), List.of(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID))
        );
    }

    @ParameterizedTest
    @MethodSource("provideHintIncorrectExamples")
    @DisplayName("hint is incorrect when marks are invalid, marks and lastHint not same length as word")
    void hintIsNotCorrect(Hint lastHint, Word value, List<Mark> marks) {
        assertThrows(
                InvalidHintException.class,
                () -> Hint.playHint(lastHint, marks, value)
        );
    }
}
