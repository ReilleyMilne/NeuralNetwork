public class ActivationFunction
{

    static class SF implements ActivationI
    {
        public double getActivationValue(double output){
            return 1.0 / (1.0 + Math.exp(-output));
        }

        public double getActivationDerivative(double output){
            double activationValue = getActivationValue(output);
            return activationValue * (1 - activationValue);
        }
    }
}