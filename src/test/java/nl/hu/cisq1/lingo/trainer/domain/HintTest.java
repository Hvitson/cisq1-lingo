package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HintTest {
    static Stream<Arguments> provideCorrectHintExamples() {
        return Stream.of(
                Arguments.of(
                        "wonen",
                        List.of(),
                        new Hint(List.of('w','.','.','.','.'))
                ),
                Arguments.of(
                        "wonen", List.of(
                                new Feedback("weeee", "wonen"),
                                new Feedback("weeon", "wonen"),
                                new Feedback("weerr", "wonen")),
                        new Hint(List.of('w','.','.','e','n'))
                ),
                Arguments.of(
                        "wonen", List.of(
                                new Feedback("wonen", "wonen"),
                                new Feedback("weeon", "wonen"),
                                new Feedback("weerr", "wonen")),
                        new Hint(List.of('w','o','n','e','n'))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideCorrectHintExamples")
    @DisplayName("Hint is created correctly with and without feedbacks")
    void hintCreatedCorrectly(String word, List<Feedback> feedbacks, Hint expected) {
        Hint hint = Hint.generateHint(word, feedbacks);
        assertEquals(expected, hint);
    }

    static Stream<Arguments> provideIncorrectGuessesExamples() {
        return Stream.of(
                Arguments.of(
                        "wonen",
                        List.of(
                                new Feedback("woooo", "wonen"),
                                new Feedback("reuteyuteyutyutyu", "wonen")
                        ),
                        new Hint(List.of('w','o','.','.','.'))
                ),
                Arguments.of(
                        "wonen",
                        List.of(new Feedback("reuteyuteyutyutyu", "wonen")),
                        new Hint(List.of('w','.','.','.','.'))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectGuessesExamples")
    @DisplayName("Hint is created correctly with wrong guess")
    void hintCreatedWithoutFeedback(String word, List<Feedback> feedbacks, Hint expected) {
        Hint hint = Hint.generateHint(word, feedbacks);
        assertEquals(expected, hint);
    }

    @Test
    @DisplayName("get chars gives list of characters")
    void getChars() {
        Hint hint = new Hint(List.of('w','o','.','.','.'));
        assertNotNull(hint.getChars());
        assertEquals(List.of('w','o','.','.','.'), hint.getChars());
    }

    static Stream<Arguments> provideEqualsExamples() {
        Hint hint = new Hint(List.of('w','o','.','.','.'));
        return Stream.of(
                Arguments.of(
                        hint,
                        hint,
                        true
                ),
                Arguments.of(
                        new Hint(List.of('w','.','.','.','.')),
                        new Hint(List.of('w','.','.','.','.')),
                        true
                ),
                Arguments.of(
                        hint,
                        new Hint(List.of('w','o','.','.','.')),
                        true
                ),
                Arguments.of(
                        new Hint(List.of('w','o','.','.','.')),
                        new Hint(List.of('w','.','.','.','.')),
                        false
                        ),
                Arguments.of(
                        new Hint(List.of('w','.','.','.','.')),
                        new Hint(List.of('w','o','.','.','.')),
                        false
                ),
                Arguments.of(
                        new Hint(List.of('w','o','.','.','.')),
                        null,
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideEqualsExamples")
    @DisplayName("Hint equals and hashcode methods test")
    void hintEqualsHashcode(Hint hint, Hint compareHint, boolean isEqual) {
        assertEquals(hint.equals(compareHint), isEqual);
        if (compareHint != null) {
            assertEquals(Objects.equals(hint.hashCode(), compareHint.hashCode()), isEqual);
        }
    }

    static Stream<Arguments> provideNotEqualsExamples() {
        return Stream.of(
                Arguments.of(
                        new Hint(List.of('w','o','.','.','.')),
                        new Hint(List.of('w','.','.','.','.'))
                ),
                Arguments.of(
                        new Hint(List.of('w','o','.','.','.')),
                        new Hint(List.of('w','.','.','.','.','.'))
                ),
                Arguments.of(
                        new Hint(List.of('w','o','.','.','.')),
                        null
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotEqualsExamples")
    @DisplayName("Hint equals and hashcode methods test")
    void hintNotEquals(Hint hint, Hint compareHint) {
        assertFalse(hint.equals(compareHint));
    }
}
