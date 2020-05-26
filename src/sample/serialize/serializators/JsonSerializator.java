package sample.serialize.serializators;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import sample.classes.Water;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.classes.Waters;

public class JsonSerializator extends Serializator {
    public boolean serializeToFile(File file, ArrayList<Water> waterObjects) {
        boolean isCorrect = false;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            FileOutputStream fos = new FileOutputStream(file);

            if (fos != null) {
                Waters waters = new Waters();
                waters.setWaters(waterObjects);
                objectMapper.writeValue(fos, waters);
                isCorrect = true;
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isCorrect;
    }

    public ArrayList<Water> deserializeFromFile(File file) {
        ArrayList<Water> waters = new ArrayList<>();

        try {
            InputStream fis = new FileInputStream(file);
            if (fis != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enableDefaultTyping();
                Waters tempWaters = objectMapper.readValue(fis, Waters.class);
                fis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return waters;
    }
}
