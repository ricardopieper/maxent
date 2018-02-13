package nlu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InstanceNlu {

    public String label;
    public Bigram[] features;

    public InstanceNlu(String label, Bigram[] bigrams) {
        this.label = label;
        this.features = bigrams;
    }

    public String getLabel() {
        return label;
    }

    public Bigram[] getFeatures() {
        return features;
    }

    @Override
    public String toString() {

        String f = Arrays.stream(features).map(Bigram::toString).collect(Collectors.joining());

        return "Instance{" +
                "label=" + label +
                ", features=" + f +
                '}';
    }
}
