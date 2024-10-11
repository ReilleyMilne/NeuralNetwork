public class DataPoint
{
    public double[] inputs;
    public double[] expectedOutputs;
    public int label;

    public DataPoint(double[] inputs, int label, int numLabels){
        this.inputs = inputs;
        this.label = label;
        expectedOutputs = createOneHot(label, numLabels);
    }

    public DataPoint(DataPoint other) {
        this.inputs = other.inputs.clone();
        this.expectedOutputs = other.expectedOutputs.clone();
        this.label = other.label;
    }

    public double[] createOneHot(int index, int numLabels){
        double[] oneHot = new double[numLabels];
        oneHot[index] = 1;
        return oneHot;
    }
}
