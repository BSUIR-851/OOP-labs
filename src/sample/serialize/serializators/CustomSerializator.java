package sample.serialize.serializators;

import sample.classes.Water;

import java.io.File;
import java.util.ArrayList;

public class CustomSerializator extends Serializator {
    public ArrayList<Water> deserializeFromFile(File file) {
        ArrayList<Water> waters = new ArrayList<>();
        return waters;
    }
    public boolean serializeToFile(File file, ArrayList<Water> waterObjects) {
        return true;
    }
}
