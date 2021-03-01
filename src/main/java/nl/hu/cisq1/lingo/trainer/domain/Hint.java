package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintException;
import nl.hu.cisq1.lingo.words.domain.Word;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

import java.util.ArrayList;
import java.util.List;

public class Hint {
    private List<Character> hint;

    public Hint(List<Character> characters) {
        hint = characters;
    }

    public static Hint playHint(Hint lastHint, List<Mark> marks, Word word) {
        if (lastHint == null) {
            return createFirstHint(word);
        }
        if (word.getLength() != marks.size() || word.getLength() != lastHint.getHint().size() || marks.stream().anyMatch(mark -> mark == INVALID)) {
            throw new InvalidHintException("invalid hint");
        }
         else {
            return createHint(lastHint, marks, word);
        }
    }

    public static Hint createFirstHint(Word word) {
        List<Character> startHint = new ArrayList<>();
        List<Character> wordChars = word.wordToChars();

        for (int i = 0; i < wordChars.size(); i++) {
            if(i == 0){
                startHint.add(wordChars.get(0));
            }
            else {
                startHint.add('.');
            }
        }
        return new Hint(startHint);
    }

    public static Hint createHint(Hint lastHint, List<Mark> marks, Word word) {
        List<Character> newHint = new ArrayList<>();
        int i = 0;

        System.out.println("hint voor transformatie: " + lastHint);
        for (Character character : lastHint.getHint()) {
            if (character == '.') {
                if (marks.get(i) == CORRECT) {
                    newHint.add(word.wordToChars().get(i));
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
