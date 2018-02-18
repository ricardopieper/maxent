package nlu;

import nlu.features.BigramGenerator;
import nlu.features.FeatureGenerator;
import nlu.features.PrefixGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataSetNlu {

    private static FeatureGenerator[] featureGenerators = new FeatureGenerator[] { new BigramGenerator(), new PrefixGenerator()};

    public static List<InstanceNlu> readDataSet(String path, BigramGenerator bigramGenerator) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<InstanceNlu> instances = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("\\|");

            String label = split[0];

            NluFeature[] allFeatures = Arrays.stream(featureGenerators)
                    .flatMap(x -> x.generate(split[1]).stream())
                    .toArray(NluFeature[]::new);

            InstanceNlu instance = new InstanceNlu(label, allFeatures);
            instances.add(instance);
        }
        return instances;
    }
}
