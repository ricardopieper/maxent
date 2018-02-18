package maxent.features;

import maxent.NluFeature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrefixGenerator implements FeatureGenerator {

    public Collection<NluFeature> generate(String sentence) {

        String[] words = sentence.split("\\s");

        List<NluFeature> features = new ArrayList<>();

        for (int i=0; i < words.length - 1; i++) {
            features.addAll(generatePrefixesForWord(words[i]));
        }

        return features;
    }

    private List<NluFeature> generatePrefixesForWord(String word) {
        List<NluFeature> prefixes = new ArrayList<>();

        for (int i=0; i < word.length() - 1; i++) {
            String prefix = "prefix=[word="+word+",prefix="+word.substring(0, i)+"]";
            prefixes.add(new NluFeature(prefix));
        }

        return prefixes;
    }

}
