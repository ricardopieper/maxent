package nlu;

public class Bigram {
    private String word1;
    private String word2;

    public Bigram(String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bigram bigram = (Bigram) o;

        if (!word1.equals(bigram.word1)) return false;
        return word2.equals(bigram.word2);
    }

    @Override
    public int hashCode() {
        int result = word1.hashCode();
        result = 31 * result + word2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Bigram{" +
                "word1='" + word1 + '\'' +
                ", word2='" + word2 + '\'' +
                '}';
    }
}
