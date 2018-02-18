package nlu;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InstanceNlu {

    public String label;
    public nlu.NluFeature[] features;

    public InstanceNlu(String label, nlu.NluFeature[] bigrams) {
        this.label = label;
        this.features = bigrams;
    }

    public String getLabel() {
        return label;
    }

    public nlu.NluFeature[] getFeatures() {
        return features;
    }

    @Override
    public String toString() {

        String f = Arrays.stream(features).map(nlu.NluFeature::toString).collect(Collectors.joining());

        return "Instance{" +
                "label=" + label +
                ", features=" + f +
                '}';
    }
}
