package sample.serialize.serializators;

import com.Plugin.Plugin;
import sample.classes.Water;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public abstract class Serializator {
    public abstract boolean serializeToFile(File file, ArrayList<Water> waterObjects, Plugin plugin, String key)
            throws IOException, IllegalAccessException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

    public abstract ArrayList<Water> deserializeFromFile(File file, Plugin plugin, String key)
            throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, NoSuchFieldException,
            InstantiationException, IllegalAccessException;

}
