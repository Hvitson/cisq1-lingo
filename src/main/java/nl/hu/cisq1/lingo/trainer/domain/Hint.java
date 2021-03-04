package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintException;
import nl.hu.cisq1.lingo.words.domain.Word;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

import java.util.ArrayList;
import java.util.List;

public class Hint {
    private final List<Character> hint;

    public Hint(List<Character> hint) {
        this.hint = hint;
    }

    public static Hint playHint(Hint lastHint, List<Mark> marks, Word wordToGuess) {
        if (lastHint == null) {
            return createFirstHint(wordToGuess);
        } else {
            return createHint(lastHint, marks, wordToGuess);
        }
    }

    private static Hint createFirstHint(Word wordToGuess) {
        List<Character> startHint = new ArrayList<>();

        for (int i = 0; i < wordToGuess.wordToChars().size(); i++) {
            if(i == 0){
                startHint.add(wordToGuess.wordToChars().get(0));
            }
            else {
                startHint.add('.');
            }
        }
        return new Hint(startHint);
    }

    private static Hint createHint(Hint lastHint, List<Mark> marks, Word wordToGuess) {
        List<Character> newHint = new ArrayList<>();
        int i = 0;
        System.out.println("hint voor transformatie: " + lastHint);
        if (marks.contains(INVALID)) {
            return lastHint;
        }
        for (Character character : lastHint.getHint()) {
            if (character == '.') {
                if (marks.get(i) == CORRECT) {
                    newHint.add(wordToGuess.wordToChars().get(i));
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
        return hint;
    }

    @Override
    public String toString() {
        return ""+ hint +"";
    }
}
