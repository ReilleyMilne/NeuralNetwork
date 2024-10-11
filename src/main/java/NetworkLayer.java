import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class NetworkLayer implements Serializable
{
    int numIncomingNodes, numOutgoingNodes;

    double[][] weightsCostGradient;
    double[] biasesCostGradient;
    double[][] weights;
    double[] biases;

    double[] lastWeightedInputs;
    double[] lastInputs;

    CostI costFunction;
    ActivationI activationFunction;

    public NetworkLayer(int numIncomingNodes, int numOutgoingNodes, CostI costFunction, ActivationI activationFunction)
    {
        this.numIncomingNodes = numIncomingNodes;
        this.numOutgoingNodes = numOutgoingNodes;

        this.weightsCostGradient = new double[numIncomingNodes][numOutgoingNodes];
        this.weights = new double[numIncomingNodes][numOutgoingNodes];
        this.biasesCostGradient = new double[numOutgoingNodes];
        this.biases = new double[numOutgoingNodes];

        this.costFunction = costFunction;
        this.activationFunction = activationFunction;

        //setWeights(index);
        populateValues();
    }

    public double[] calculateOutputs(double[] inputs){
        this.lastInputs = inputs;
        lastWeightedInputs = new double[numOutgoingNodes];
        double[] activationOutputs = new double[numOutgoingNodes];
        for(int outgoingNode = 0; outgoingNode < numOutgoingNodes; outgoingNode++) {
            double currOutput = biases[outgoingNode];
            for(int incomingNode = 0; incomingNode < numIncomingNodes; incomingNode++) {
                currOutput += inputs[incomingNode] * weights[incomingNode][outgoingNode];
            }
            lastWeightedInputs[outgoingNode] = currOutput;
            activationOutputs[outgoingNode] = activationFunction.getActivationValue(currOutput);
        }
        return activationOutputs;
    }

    public double[] calculateHiddenLayerValues(NetworkLayer oldLayer, double[] oldValues){
        double[] newValues = new double[numOutgoingNodes];

        for(int newNodeIndex = 0; newNodeIndex < newValues.length; newNodeIndex++){
            double newValue = 0;
            for(int oldNodeIndex = 0; oldNodeIndex < oldValues.length; oldNodeIndex++){
                double weightedInputDerivative = oldLayer.weights[newNodeIndex][oldNodeIndex];
                newValue += weightedInputDerivative * oldValues[oldNodeIndex];
            }
            newValue *= activationFunction.getActivationDerivative(lastWeightedInputs[newNodeIndex]);
            newValues[newNodeIndex] = newValue;
        }
        return newValues;
    }

    public double[] calculateOutputLayerValues(double[] predictedOutputs, double[] expectedOutputs){
        double[] values = new double[numOutgoingNodes];
        for(int i = 0; i < values.length; i++){
            double costDerivative = costFunction.getCostDerivative(predictedOutputs[i], expectedOutputs[i]);
            double activationDerivative = activationFunction.getActivationDerivative(lastWeightedInputs[i]);
            values[i] = costDerivative * activationDerivative;
        }
        return values;
    }

    public void applyGradients(double learnRate){
        for(int outgoingNode = 0; outgoingNode < numOutgoingNodes; outgoingNode++){
            biases[outgoingNode] -= biasesCostGradient[outgoingNode] * learnRate;
            for(int incomingNode = 0; incomingNode < numIncomingNodes; incomingNode++){
                weights[incomingNode][outgoingNode] -= weightsCostGradient[incomingNode][outgoingNode] * learnRate;
            }
        }
    }

    public void resetGradients(){
        for(int i = 0; i < numOutgoingNodes; i++){
            for(int j = 0; j < numIncomingNodes; j++){
                weightsCostGradient[j][i] = 0;
            }
        }
        Arrays.fill(biasesCostGradient, 0);
    }

    public void updateGradients(double[] values){
        for(int outgoingNode = 0; outgoingNode < numOutgoingNodes; outgoingNode++){
            for(int incomingNode = 0; incomingNode < numIncomingNodes; incomingNode++){
                double derivativeCostWeight = lastInputs[incomingNode] * values[outgoingNode];
                weightsCostGradient[incomingNode][outgoingNode] += derivativeCostWeight;
            }
            double derivativeCostBias = values[outgoingNode];
            biasesCostGradient[outgoingNode] += derivativeCostBias;
        }
    }

    private void populateValues() {
        Random random = new Random();
        // Xavier initialization
        for (int i = 0; i < numIncomingNodes; i++) {
            for (int j = 0; j < numOutgoingNodes; j++) {
                double limit = Math.sqrt(6.0 / (numIncomingNodes + numOutgoingNodes));
                weights[i][j] = random.nextDouble() * 2 * limit - limit; // Uniform distribution
            }
        }
        Arrays.fill(biases, 0.0);
    }

    public void setWeights(int index){
        if(index == 0){
            weights = new double[][]{{0.3, 0.25, -0.1}, {-0.3, -0.2, 0.1}};
        }
        else {
            weights = new double[][]{{0.22, 0.06}, {-0.3, 0.15}, {0.29, -0.17}};
        }
        biases = new double[]{0.01, 0.01, 0.01};
    }
}
