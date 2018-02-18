package maxent;

public class NluFeature {

    private String feature;

    public NluFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NluFeature that = (NluFeature) o;

        return feature != null ? feature.equals(that.feature) : that.feature == null;
    }

    @Override
    public int hashCode() {
        return feature != null ? feature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return feature;
    }
}
