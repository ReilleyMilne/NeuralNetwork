import java.io.Serializable;

public class Network implements Serializable
{
    NetworkLayer[] layers;

    CostI costFunction;

    public Network(int[] layerLengths, CostI costFunction, ActivationI activationFunction){
        layers = new NetworkLayer[layerLengths.length - 1];
        for(int layer = 0; layer < layerLengths.length - 1; layer++) {
            layers[layer] = new NetworkLayer(layerLengths[layer], layerLengths[layer + 1], costFunction, activationFunction);
        }

        this.costFunction = costFunction;
    }

    // output_1 = safe, output_2 = poisonous
    public int classify(double[] inputs){
        double[] outputs = calculateOutputs(inputs);
        int maxOutputIndex = -1;
        double maxOutputValue = Double.NEGATIVE_INFINITY;
        //System.out.println("Safe: " + outputs[0] + " Poison: " + outputs[1]);
        for(int output = 0; output < outputs.length; output++){
            if(outputs[output] > maxOutputValue) {
                maxOutputValue = outputs[output];
                maxOutputIndex = output;
            }
        }
        return maxOutputIndex;
    }

    public void learn(DataPoint[] trainingBatch, double learningRate){
        for (NetworkLayer layer : layers) {
            layer.resetGradients();
        }

        for(DataPoint dataPoint : trainingBatch){
            updateAllGradients(dataPoint);
        }

        double batchLearningRate = learningRate / trainingBatch.length;
        for (NetworkLayer layer : layers) {
            layer.applyGradients(batchLearningRate);
        }
    }

    public double[] calculateOutputs(double[] inputs){
        for(int layer = 0; layer < layers.length; layer++) {
            inputs = layers[layer].calculateOutputs(inputs);
        }
        return inputs; // This will be the final layer of nodes, also known as the output layer.
    }

    public void updateAllGradients(DataPoint dataPoint){
        double[] currInputs = calculateOutputs(dataPoint.inputs);
        NetworkLayer outputLayer = layers[layers.length - 1];

        double[] nodeValues = outputLayer.calculateOutputLayerValues(currInputs, dataPoint.expectedOutputs);
        outputLayer.updateGradients(nodeValues);

        for(int hiddenLayerIndex = layers.length - 2; hiddenLayerIndex >= 0; hiddenLayerIndex--){
            NetworkLayer hiddenLayer = layers[hiddenLayerIndex];
            nodeValues = hiddenLayer.calculateHiddenLayerValues(layers[hiddenLayerIndex + 1], nodeValues);
            hiddenLayer.updateGradients(nodeValues);
        }
    }

    public double getTotalCost(DataPoint[] dataPoints) {
        double totalCost = 0;
        for (DataPoint dataPoint : dataPoints) {
            double[] predictedOutputs = calculateOutputs(dataPoint.inputs);
            totalCost += costFunction.getCostValue(predictedOutputs, dataPoint.expectedOutputs);
        }
        return totalCost / dataPoints.length; // return the average cost
    }
}
