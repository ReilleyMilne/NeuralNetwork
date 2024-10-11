import java.io.Serializable;

public interface ActivationI extends Serializable
{
    double getActivationValue(double output);

    double getActivationDerivative(double output);
}
