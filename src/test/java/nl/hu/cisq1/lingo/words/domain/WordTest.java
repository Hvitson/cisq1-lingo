package nl.hu.cisq1.lingo.words.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordTest {

    @Test
    @DisplayName("length is based on given word")
    void lengthBasedOnWord() {
        Word word = new Word("woord");
        int length = word.getLength();
        assertEquals(5, length);
    }

    @Test
    @DisplayName("length is same after toChar()")
    void wordToCharLength(){
        Word word = new Word("yeeet");
        List<Character> wordArray = word.wordToChars();

        assertEquals(word.getLength(), wordArray.size());
    }
}
