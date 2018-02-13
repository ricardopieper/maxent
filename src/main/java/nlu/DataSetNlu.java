package nlu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataSetNlu {

    public static List<InstanceNlu> readDataSet(String path, BigramGenerator bigramGenerator) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<InstanceNlu> instances = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("\\|");

            String label = split[0];

            Bigram[] bigrams = bigramGenerator.generate(split[1]);

            InstanceNlu instance = new InstanceNlu(label, bigrams);
            instances.add(instance);
        }
        return instances;
    }
}
