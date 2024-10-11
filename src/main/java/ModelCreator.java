import java.io.*;

public class ModelCreator
{
    public static void saveModel(Network network, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(network);
            System.out.println("Model saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Network loadModel(String filePath) {
        Network network = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            network = (Network) ois.readObject();
            System.out.println("Model loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return network;
    }
}