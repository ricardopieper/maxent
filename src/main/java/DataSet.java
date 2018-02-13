import nlu.Bigram;
import nlu.BigramGenerator;
import nlu.InstanceNlu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The dataset supported is using same format of lzhang's maxent package.
 * It's also similar to libsvm's data format.
 * <p>
 * e.g. c1 f21 f30 f40 f51 f60 f70 f81 f91 f101 f111 f120 f130 f144 f150 f160 f171
 * <p>
 * Created with IntelliJ IDEA.
 * User: tpeng  <pengtaoo@gmail.com>
 * Date: 7/5/12
 * Time: 11:20 PM
 */
public class DataSet {

    public static List<Instance> readDataSet(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<Instance> instances = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> tokens = Arrays.asList(line.split("\\s"));
            String s1 = tokens.get(0);
            int label = Integer.parseInt(s1.substring(s1.length() - 1));

            int[] features = tokens.stream().skip(1)
                    .mapToInt(s -> Integer.parseInt(s.substring(s.length() - 1)))
                    .toArray();

            Instance instance = new Instance(label, features);
            instances.add(instance);
        }
        return instances;
    }

}
