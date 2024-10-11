import java.util.HashSet;
import java.util.Random;

public class DataProcessor
{

    public static DataPoint[][] splitData(DataPoint[] data, int splitPercentage){
        int splitPoint = (int) (data.length * (splitPercentage/100.0));
        DataPoint[] trainData = new DataPoint[splitPoint];
        DataPoint[] testData = new DataPoint[data.length - splitPoint];
        for(int i = 0; i < splitPoint; i++){
            trainData[i] = data[i];
        }
        int count = 0;
        for(int i = splitPoint; i < data.length; i++){
            testData[count] = data[i];
            count++;
        }
        return new DataPoint[][] {trainData, testData};
    }

    private static int[] getRandomIndices(int totalSize, int batchSize) {
        Random random = new Random();

        HashSet<Integer> hashSetIndexes = new HashSet<Integer>();
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
}
