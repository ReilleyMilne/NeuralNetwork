import java.io.Serializable;

public interface CostI extends Serializable
{
    double getCost(double predictedOutput, double expectedOutput);

    double getCostDerivative(double predictedOutput, double expectedOutput);

    double getCostValue(double[] predictedOutputs, double[] expectedOutputs);
}
