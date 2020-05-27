package sample.serialize.serializators;

import sample.classes.Water;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Serializator {
    public abstract boolean serializeToFile(File file, ArrayList<Water> waterObjects) throws IOException, IllegalAccessException;
    public abstract ArrayList<Water> deserializeFromFile(File file);

}
