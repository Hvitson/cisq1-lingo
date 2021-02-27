package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintException;
import nl.hu.cisq1.lingo.words.domain.Word;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

import java.util.ArrayList;
import java.util.List;

public class Hint {
    private List<Character> hint;

    public Hint(List<Character> characters) {
        this.hint = characters;
    }

    public static Hint createStartHint(Word word) {
        List<Character> startHint = new ArrayList<>();
        List<Character> wordToGuess = word.wordToChars();

        for (int i = 0; i < wordToGuess.size(); i++) {
            if(i == 0){
                startHint.add(wordToGuess.get(0));
            }
            else {
                startHint.add('.');
            }
        }
        return new Hint(startHint);
    }

    public static Hint calculateHint(Hint lastHint, List<Mark> marks, Word value) {
        List<Character> newHint = new ArrayList<>();
        int i = 0;

        if (lastHint == null) {
            lastHint = createStartHint(value);
        }
        if (marks.size() != lastHint.getHint().size() || marks.size() != value.getLength()) {
            throw new InvalidHintException("invalid hint");
        }

        System.out.println("hint voor transformatie: " + lastHint);
        for (Character character : lastHint.getHint()) {
            System.out.println(character);
            if (character == '.') {
                if (marks.get(i) == CORRECT) {
                    newHint.add(value.wordToChars().get(i));
                } else {
                    newHint.add('.');
                }
            } else {
                newHint.add(character);
            }
            i += 1;
        }
        System.out.println("Hint na transformatie: " + newHint);

        return new Hint(newHint);
    }

    public List<Character> getHint() {
        return this.hint;
    }

    @Override
    public String toString() {
        return ""+ hint +"";
    }
}
