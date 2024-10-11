public class CostFunction
{

    static class MSE implements CostI
    {
        public double getCost(double predictedOutput, double expectedOutput){
            return Math.pow((predictedOutput - expectedOutput), 2);
        }

        public double getCostDerivative(double predictedOutput, double expectedOutput){
            return 2 * (predictedOutput - expectedOutput);
        }

        public double getCostValue(double[] predictedOutputs, double[] expectedOutputs){
            double totalCost = 0;
            for(int i = 0; i < predictedOutputs.length; i++){
                totalCost += getCost(predictedOutputs[i], expectedOutputs[i]);
            }
            return totalCost / predictedOutputs.length;
        }
    }

    static class CEE implements CostI
    {
        public double getCost(double predictedOutput, double expectedOutput)
        {
            return -(expectedOutput * Math.log(predictedOutput) +
                    (1 - expectedOutput) * Math.log(1 - predictedOutput));
        }

        public double getCostDerivative(double predictedOutput, double expectedOutput)
        {
            return (predictedOutput - expectedOutput) / (predictedOutput * (1 - predictedOutput));
        }

        public double getCostValue(double[] predictedOutputs, double[] expectedOutputs){
            double totalCost = 0;
            for(int i = 0; i < predictedOutputs.length; i++){
                totalCost += getCost(predictedOutputs[i], expectedOutputs[i]);
            }
            return totalCost / predictedOutputs.length;
        }
    }
}
