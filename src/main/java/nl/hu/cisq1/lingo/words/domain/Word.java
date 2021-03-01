package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "words")
public class Word {
    @Id
    @Column(name = "word")
    private String value;
    private Integer length;

    public Word() {}
    public Word(String word) {
        this.value = word;
        this.length = word.length();
    }

    public String getValue() {
        return value;
    }

    public Integer getLength() {
        return length;
    }

    public List<Character> wordToChars() {
        List<Character> chars = new ArrayList<>();

        for (Character character : this.value.toCharArray()) {
            chars.add(character);
        }

        return chars;
    }

    @Override
    public String toString() {
        return "Word{" +
                "value='" + value + '\'' +
                ", length=" + length +
                '}';
    }
}
