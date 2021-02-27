package nl.hu.cisq1.lingo.trainer.domain;

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

public class HintTest {
    private static Stream<Arguments> provideHintExamples() {
        //zorg dat string Word wordt
        return Stream.of(
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("wonen"), List.of(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT), new Hint(List.of('w','.','.','.','.'))),
                Arguments.of(new Hint(List.of('w','.','.','.','.')), new Word("woest"), List.of(CORRECT, CORRECT, ABSENT, ABSENT, ABSENT), new Hint(List.of('w','o','.','.','.'))),
                Arguments.of(new Hint(List.of('w','o','.','.','.')), new Word("woerd"), List.of(CORRECT, CORRECT, ABSENT, CORRECT, CORRECT), new Hint(List.of('w','o','.','r','d'))),
                Arguments.of(new Hint(List.of('w','o','.','r','d')), new Word("woord"), List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), new Hint(List.of('w','o','o','r','d')))
        );
    }

    @Test
    @DisplayName("hint is formed correctly")
    void hintCorrectlyMade() {
        Word word = new Word("woord");
        Hint hint = Hint.createStartHint(word);

        assertEquals(new Hint(List.of('w','.','.','.','.')).toString(), hint.toString());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("check hints after update corresponds expected new hint")
    void hintIsCorrect(Hint lastHint, Word value, List<Mark> marks, Hint expectedHint) {
        Hint hint = Hint.calculateHint(lastHint, marks, value);

        assertEquals(expectedHint.getHint(), hint.getHint());
    }
}
