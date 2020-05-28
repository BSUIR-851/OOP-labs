package sample.serialize.serializators;

import sample.classes.Water;

import java.io.*;
import java.util.ArrayList;

public class BinSerializator extends Serializator {

    public boolean serializeToFile(File file, ArrayList<Water> waterObjects) throws IOException {
        // binary serialize list of objects
        boolean isCorrect = false;

        ObjectOutputStream oos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (fos != null) {
                oos = new ObjectOutputStream(fos);
                for (Water water: waterObjects) {
                    oos.writeObject(water);
                }
                isCorrect = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isCorrect;
    }

    public ArrayList<Water> deserializeFromFile(File file) {
        // init list for deserialized objects
        ArrayList<Water> waters = new ArrayList<>();

        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            if (fis != null) {
                ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        waters.add((Water) ois.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return waters;
    }

}
