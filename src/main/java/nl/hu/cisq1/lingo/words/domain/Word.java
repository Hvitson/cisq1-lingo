package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void setValue(String value) {
        this.value = value;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(value, word.value) && Objects.equals(length, word.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, length);
    }

    @Override
    public String toString() {
        return "Word{" +
                "value='" + value + '\'' +
                ", length=" + length +
                '}';
    }
}
