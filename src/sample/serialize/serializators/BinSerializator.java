package sample.serialize.serializators;

import com.Plugin.Plugin;

import sample.classes.Water;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BinSerializator extends Serializator {

    public boolean serializeToFile(File file, ArrayList<Water> waterObjects, Plugin plugin, String key)
            throws IllegalAccessException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        // binary serialize list of objects
        boolean isCorrect = false;

        ObjectOutputStream oos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (fos != null) {
                oos = new ObjectOutputStream(bos);
                for (Water water: waterObjects) {
                    oos.writeObject(water);
                }
                byte[] objects = bos.toByteArray();
                // encrypting objects if choosed to encrypt
                if (plugin != null) {
                    objects = plugin.encrypt(objects, key);
                }
                fos.write(objects);
                isCorrect = true;
            }
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

    public ArrayList<Water> deserializeFromFile(File file, Plugin plugin, String key)
            throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException {
        // init list for deserialized objects
        ArrayList<Water> waters = new ArrayList<>();

        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] objects = new byte[(int) file.length()];
            fis.read(objects);

            // decrypt objects if choosed to decrypt
            if (plugin != null) {
                objects = plugin.decrypt(objects, key);
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(objects);
            if (fis != null) {
                ois = new ObjectInputStream(bis);
                while (true) {
                    try {
                        waters.add((Water) ois.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            }
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
