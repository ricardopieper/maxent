package nlu;

import features.BigramGenerator;
import features.FeatureGenerator;
import features.PrefixGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataSetNlu {

    private static FeatureGenerator[] featureGenerators = new FeatureGenerator[] { new BigramGenerator(), new PrefixGenerator()};

    public static List<nlu.InstanceNlu> readDataSet(String path, BigramGenerator bigramGenerator) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<nlu.InstanceNlu> instances = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("\\|");

            String label = split[0];

            nlu.NluFeature[] allFeatures = Arrays.stream(featureGenerators)
                    .flatMap(x -> x.generate(split[1]).stream())
                    .toArray(nlu.NluFeature[]::new);

            nlu.InstanceNlu instance = new nlu.InstanceNlu(label, allFeatures);
            instances.add(instance);
        }
        return instances;
    }
}
