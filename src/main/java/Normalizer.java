public class Normalizer
{
    static class MM implements NormalizerI
    {
        public double getNormalizedValue(double value, double min, double max)
        {
            double numerator = value - min;
            double denominator = max - min;
            return numerator / denominator;
        }
    }
}
