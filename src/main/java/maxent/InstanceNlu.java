package maxent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InstanceNlu {

    public String label;
    public NluFeature[] features;

    public InstanceNlu(String label, NluFeature[] bigrams) {
        this.label = label;
        this.features = bigrams;
    }

    public String getLabel() {
        return label;
    }

    public NluFeature[] getFeatures() {
        return features;
    }

    @Override
    public String toString() {

        String f = Arrays.stream(features).map(NluFeature::toString).collect(Collectors.joining());

        return "Instance{" +
                "label=" + label +
                ", maxent.features=" + f +
                '}';
    }
}
