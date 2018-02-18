package features;

import nlu.NluFeature;
import java.util.Collection;

public interface FeatureGenerator  {
    Collection<NluFeature> generate(String sentence);
}
