package nlu;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: tpeng  <pengtaoo@gmail.com>
 * Date: 7/5/12
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaxEntNlu {

    private final static boolean DEBUG = true;

    private final int ITERATIONS = 200;

    private static final double EPSILON = 0.001;

    // the number of training instances
    private int N;

    // the empirical expectation value of f(x, y)
    private double empirical_expects[];

    // the weight to learn.
    private double w[];

    private List<InstanceNlu> instances = new ArrayList<>();

    private List<FeatureFunctionNlu> functions = new ArrayList<>();

    private List<Bigram[]> features = new ArrayList<>();

    private List<String> labels;

    public static void main(String... args) throws FileNotFoundException {
        List<InstanceNlu> instances = DataSetNlu.readDataSet("examples/nlu.train", new BigramGenerator());
        MaxEntNlu me = new MaxEntNlu(instances);
        me.train();

        List<InstanceNlu> trainInstances = DataSetNlu.readDataSet("examples/nlu.test", new BigramGenerator());
        int pass = 0;
        for (InstanceNlu instance : trainInstances) {
            String predict = me.classify(instance);
            if (predict.equals(instance.getLabel())) {
                pass += 1;
            }
        }

        System.out.println("accuracy: " + 1.0 * pass / trainInstances.size());
    }

    public MaxEntNlu(List<InstanceNlu> trainInstance) {

        instances.addAll(trainInstance);
        N = instances.size();
        createFeatFunctions(instances);
        w = new double[functions.size()];
        empirical_expects = new double[functions.size()];
        calc_empirical_expects();
    }

    private void createFeatFunctions(List<InstanceNlu> instances) {

        for (InstanceNlu instance : instances) {
            Bigram[] f = instance.features;
            features.add(f);
        }

        for (InstanceNlu instance : instances) {
            Bigram[] f = instance.features;
            for (int j = 0; j < f.length; j++) {
                functions.add(new FeatureFunctionNlu(j, f[j], instance.label));
            }
        }

        labels = instances.stream().map(x -> x.label).distinct().collect(Collectors.toList());

        if (DEBUG) {
            System.out.println("# features = " + features.size());
            System.out.println("# functions = " + functions.size());
            System.out.println("# labels = " + labels.size());
        }
    }

    // calculates the p(y|x)
    private double[][] calc_prob_y_given_x() {

        double[][] cond_prob = new double[features.size()][labels.size()];

        for (int y = 0; y < labels.size(); y++) {
            for (int i = 0; i < features.size(); i++) {
                double z = 0;
                for (int j = 0; j < functions.size(); j++) {
                    z += w[j] * functions.get(j).apply(features.get(i), labels.get(y));
                }
                cond_prob[i][y] = Math.exp(z);
            }
        }

        for (int i = 0; i < features.size(); i++) {
            double normalize = 0;
            for (int y = 0; y < labels.size(); y++) {
                normalize += cond_prob[i][y];
            }
            for (int y = 0; y < labels.size(); y++) {
                cond_prob[i][y] /= normalize;
            }
        }

        return cond_prob;
    }


    private void train() {
        for (int k = 0; k < ITERATIONS; k++) {
            for (int i = 0; i < functions.size(); i++) {
                double delta = iis_solve_delta(empirical_expects[i], i);
                w[i] += delta;
            }
            System.out.println("ITERATIONS: " + k + " " + Arrays.toString(w));
        }
    }

    private String classify(InstanceNlu instance) {

        double max = 0;
        String label = "";

        for (String lbl : labels) {
            double sum = 0;
            for (int i = 0; i < functions.size(); i++) {
                sum += Math.exp(w[i] * functions.get(i).apply(instance.getFeatures(), lbl));
            }
            if (sum > max) {
                max = sum;
                label = lbl;
            }
        }
        return label;
    }

    private void calc_empirical_expects() {

        for (InstanceNlu instance : instances) {
            String y = instance.getLabel();
            Bigram[] features = instance.getFeatures();
            for (int i = 0; i < functions.size(); i++) {
                empirical_expects[i] += functions.get(i).apply(features, y);
            }
        }
        for (int i = 0; i < functions.size(); i++) {
            empirical_expects[i] /= 1.0 * N;
        }
        System.out.println(Arrays.toString(empirical_expects));
    }


    private double iis_solve_delta(double empirical_e, int fi) {

        double delta = 0;
        double f_newton, df_newton;
        double p_yx[][] = calc_prob_y_given_x();

        int iters = 0;

        while (iters < 50) {
            f_newton = df_newton = 0;

            for (InstanceNlu instance : instances) {
                Bigram[] feature = instance.getFeatures();
                int index = features.indexOf(feature);
                for (int y = 0; y < labels.size(); y++) {
                    int f_sharp = apply_f_sharp(feature, y);
                    double prod = p_yx[index][y] * functions.get(fi).apply(feature, labels.get(y)) * Math.exp(delta * f_sharp);
                    f_newton += prod;
                    df_newton += prod * f_sharp;
                }
            }

            f_newton = empirical_e - f_newton / N;
            df_newton = -df_newton / N;

            if (Math.abs(f_newton) < 0.0000001)
                return delta;

            double ratio = f_newton / df_newton;

            delta -= ratio;
            if (Math.abs(ratio) < EPSILON) {
                return delta;
            }
            iters++;
        }
        throw new RuntimeException("IIS did not converge");
    }

    private int apply_f_sharp(Bigram[] feature, int y) {

        int sum = 0;
        for (FeatureFunctionNlu function : functions) {
            sum += function.apply(feature, labels.get(y));
        }
        return sum;
    }


    class FeatureFunctionNlu {

        private int index;
        private Bigram value;
        private String label;

        FeatureFunctionNlu(int index, Bigram value, String label) {
            this.index = index;
            this.value = value;
            this.label = label;
        }

        public int apply(Bigram[] sentenceBigrams, String label) {
            if (index > (sentenceBigrams.length - 1)) {
                return 0;
            }

            if (sentenceBigrams[index].equals(value) && label.equals(this.label))
                return 1;
            return 0;
        }
    }
}


