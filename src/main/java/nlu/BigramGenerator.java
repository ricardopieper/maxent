package nlu;

import java.util.ArrayList;
import java.util.List;

public class BigramGenerator {

    public Bigram[] generate(String sentence) {

        String[] words = sentence.split("\\s");

        List<Bigram> bigrams = new ArrayList<>();

        for (int i=0; i < words.length - 1; i++) {
            Bigram b = new Bigram(words[i], words[i + 1]);
            bigrams.add(b);
        }

        return bigrams.toArray(new Bigram[0]);

    }

}
