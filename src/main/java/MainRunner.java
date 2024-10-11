import java.util.HashSet;
import java.util.Random;

public class MainRunner {

    public static int[] layerLengths = {2, 8, 2};

    // Data
    public static int minDataValue = 0;
    public static int maxDataValue = 50;
    public static int dataSize = 1000;

    // Hyperparameters
    public static int epochs = 10000;
    public static int batchSize = 64;
    public static double learnRate = 0.6;
    public static int splitPercentage = 70;

    // Data for graph creation
    public static double[] costForEpoch = new double[epochs];
    public static double[] trainAccuracy = new double[epochs];
    public static double[] testAccuracy = new double[epochs];

    public static void main(String[] args)
    {
        Network nw = new Network(layerLengths, new CostFunction.MSE(), new ActivationFunction.SF());

        DataPoint[] data = generateData(dataSize, new Normalizer.MM());

        DataPoint[][] splitData = DataProcessor.splitData(data, splitPercentage);
        DataPoint[] trainData = splitData[0];
        DataPoint[] testData = splitData[1];

        long startTime = System.nanoTime();

        double currCost = 0;
        double totalCost = 0;
        double iteration = 1;
        int completedEpochs = 0;
        for(int i = 0; i < epochs; i++)
        {
            int[] batchIndexes = getRandomIndices(trainData.length, batchSize);
            DataPoint[] batchData = getBatch(trainData, batchIndexes);

            nw.learn(batchData, learnRate);

            // Cost tracker
            currCost = nw.getTotalCost(batchData);
            totalCost += currCost;
            costForEpoch[i] = currCost;
            System.out.println("Epoch " + i + ", Total Cost: " + currCost);

            // Train and Test accuracy tracker
            trainAccuracy[i] = getAccuracy(nw, trainData);
            testAccuracy[i] = getAccuracy(nw, testData);

            if(totalCost / (iteration + 1) < 0.001){
                System.out.println("break");
                break;
            }
            if(i % 50000 == 0){
                totalCost = 0;
                iteration = 1;
            } else{
                iteration++;
            }
            completedEpochs++;
        }

        long endTime = System.nanoTime();

        long durationInNanoseconds = endTime - startTime;
        double durationInSeconds = durationInNanoseconds / 1_000_000_000.0;
        System.out.printf("Duration: %.6f seconds%n", durationInSeconds);

        String filePath = "C:\\Users\\Reilley\\IdeaProjects\\NeuralNetworkScratch\\models\\basic_model.ser";
        //ModelCreator.saveModel(nw, filePath);

        //Network loadedNetwork = ModelCreator.loadModel(filePath);

        printAccuracy(nw, trainData, testData, currCost);
        Test.createGraph(completedEpochs, costForEpoch);
        Test.addSeries(completedEpochs, trainAccuracy, "Training Data Accuracy");
        Test.addSeries(completedEpochs, testAccuracy, "Test Data Accuracy");
        Test.createScatterPlot(data);
    }

    public static void printAccuracy(Network nw, DataPoint[] trainData, DataPoint[] testData, double totalCost){
        int totalCorrect = 0;
        for(int i = 0; i < testData.length; i++){
            int result = nw.classify(testData[i].inputs);
            if(result == isSafe(testData[i].inputs)){
                totalCorrect++;
            }
        }
        System.out.println(totalCost);
        System.out.println("Test Data Correct: " + totalCorrect + " / " + testData.length);
        System.out.println("Training Accuracy: " + trainAccuracy[trainAccuracy.length-1]);
        System.out.println("Test Accuracy: " + testAccuracy[testAccuracy.length-1]);
    }

    public static double getAccuracy(Network nw, DataPoint[] data){
        int totalCorrect = 0;
        for(int i = 0; i < data.length; i++){
            int result = nw.classify(data[i].inputs);
            if(result == isSafe(data[i].inputs)){
                totalCorrect++;
            }
        }
        return (double) totalCorrect / data.length;
    }

    public static DataPoint[] generateData(int dataSize, NormalizerI normalizer){
        Random rand = new Random();

        DataPoint[] data = new DataPoint[dataSize];
        for(int i = 0; i < data.length; i++){
            double xData = rand.nextInt(maxDataValue - minDataValue + 1) + minDataValue;
            double yData = rand.nextInt(maxDataValue - minDataValue + 1) + minDataValue;
            double xDataNormalized = normalizer.getNormalizedValue(xData, minDataValue, maxDataValue);
            double yDataNormalized = normalizer.getNormalizedValue(yData, minDataValue, maxDataValue);
            double[] dataPoint = new double[]{xDataNormalized, yDataNormalized};
            data[i] = new DataPoint(dataPoint, isSafe(dataPoint), 2);
        }
        return data;
    }

    public static int isSafe(double[] data){
        double normalizedThresholdX = (double) (33 - minDataValue) / (maxDataValue - minDataValue);
        double normalizedThresholdY = (double) (23 - minDataValue) / (maxDataValue - minDataValue);

        if (data[0] <= normalizedThresholdX && data[1] <= normalizedThresholdY) {
            return 1; // Safe
        } else {
            return 0; // Not safe
        }
     }

    private static int[] getRandomIndices(int totalSize, int batchSize) {
        Random random = new Random();

        HashSet<Integer> hashSetIndexes = new HashSet<>();
        int[] indexes = new int[batchSize];
        for(int i = 0; i < batchSize; i++){
            int currIndex = random.nextInt(totalSize);
            while(hashSetIndexes.contains(currIndex)){
                currIndex = random.nextInt(totalSize);
            }
            hashSetIndexes.add(currIndex);
            indexes[i] = currIndex;
        }

        return indexes;
    }

    private static DataPoint[] getBatch(DataPoint[] data, int[] indexes) {
        DataPoint[] batchData = new DataPoint[indexes.length];

        for(int i = 0; i < indexes.length; i++){
            batchData[i] = new DataPoint(data[indexes[i]]);
        }
        return batchData;
    }
}
