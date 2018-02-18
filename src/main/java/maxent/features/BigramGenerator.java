package maxent.features;

import maxent.NluFeature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BigramGenerator implements FeatureGenerator {

    public Collection<NluFeature> generate(String sentence) {

        String[] words = sentence.split("\\s");

        List<NluFeature> bigrams = new ArrayList<>();

        for (int i=0; i < words.length - 1; i++) {
            bigrams.add(new NluFeature("bigram=["+words[i]+","+words[i+1]+"]"));
        }
        return bigrams;
    }

}
