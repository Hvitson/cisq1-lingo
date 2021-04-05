package nl.hu.cisq1.lingo.trainer.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Hint implements Serializable {
    private List<Character> chars;

    public Hint(List<Character> chars) {
        this.chars = chars;
    }

    public static Hint generateHint(String wordToGuess, List<Feedback> feedbacks) {
        List<Character> chars = new ArrayList<>();
        List<Character> charsWordToGuess = new ArrayList<>();

        for (Character character : wordToGuess.toCharArray()) {
            charsWordToGuess.add(character);
        }

        chars.add(charsWordToGuess.get(0));
        for (int i = 1; i < charsWordToGuess.size(); i++) {
            chars.add('.');
        }

        for (Feedback feedback : feedbacks) {
            List<Mark> marks = feedback.getMarks();

            for (int i = 1; i < marks.size(); i++) {
                if (marks.get(i) == CORRECT) {
                    chars.set(i, charsWordToGuess.get(i));
                }
            }
        }
        return new Hint(chars);
    }


    public List<Character> getChars() {
        return chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint = (Hint) o;
        return Objects.equals(chars, hint.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chars);
    }

    @Override
    public String toString() {
        return ""+ chars +"";
    }
}
