package maxent.features;

import maxent.NluFeature;
import java.util.Collection;

public interface FeatureGenerator  {
    Collection<NluFeature> generate(String sentence);
}
