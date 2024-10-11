import java.io.Serializable;

public interface NormalizerI extends Serializable
{
    double getNormalizedValue(double value, double min, double max);

}
