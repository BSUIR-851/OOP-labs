package sample.serialize.serializators;

import com.Plugin.Plugin;
import javafx.util.Pair;
import sample.classes.Water;
import sample.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CustomSerializator extends Serializator {
    public boolean serializeToFile(File file, ArrayList<Water> waterObjects, Plugin plugin, String key)
            throws IllegalAccessException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        // serialize list of objects in textfile
        boolean isCorrect = false;
        FileOutputStream fos = new FileOutputStream(file);
        String serObjects = "";
        if (fos != null) {
            for (Water water: waterObjects) {
                Pair<ArrayList<String>, ArrayList<Field>> fields = utils.getAllDeclaredFields(water.getClass());
                ArrayList<String> fieldsType = fields.getKey();
                ArrayList<Field> fieldsObj = fields.getValue();

                serObjects += ";" + this.getStringFields(fieldsObj, water) + ";";
            }
            byte[] objects = serObjects.getBytes();

            // encrypt object if choosed to encrypt
            if (plugin != null) {
                objects = plugin.encrypt(objects, key);
            }
            fos.write(objects);
            isCorrect = true;
            fos.close();
        }
        return isCorrect;
    }

    public String getStringFields(ArrayList<Field> fields, Water water) throws IllegalAccessException {
        // get all fields as a string in format (fieldName=fieldValue)
        String strFields = "";
        strFields += ":(class=" + water.getClass().getName() + "):";
        for (Field field: fields) {
            strFields += "|(";
            // if not aggregated object
            if (utils.isPrimitiveClass(field.getType().getSimpleName())) {
                strFields += field.getName() + "=" + field.get(water).toString();
            } else {
                // if aggregated - write with symbols '[' and ']'
                Pair<ArrayList<String>, ArrayList<Field>> agrClassNameAndFields = utils.getAllDeclaredFields(field.getType());
                strFields += field.getName() + "=[" + this.getStringFields(agrClassNameAndFields.getValue(), (Water) field.get(water)) + "]";
            }
            strFields += ")|";
        }
        return strFields;
    }

    public ArrayList<Water> deserializeFromFile(File file, Plugin plugin, String key)
            throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
            IllegalAccessException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException {
        // init list for deserialized objects
        ArrayList<Water> waters = new ArrayList<>();
        FileInputStream fis = new FileInputStream(file);
        if (fis != null) {
            // read all serialized data from file
            byte[] data = new byte[(int) file.length()];
            fis.read(data);

            // decrypt objects if choosed to decrypt
            if (plugin != null) {
                data = plugin.decrypt(data, key);
            }
            String serObjects = new String(data, StandardCharsets.UTF_8);

            // start deserializing
            StringTokenizer st = new StringTokenizer(serObjects, ";;");
            Pair<Water, Integer> tempWaterAndInd;
            while (st.hasMoreTokens()) {
                String obj = st.nextToken();
                tempWaterAndInd = this.getObjectFromStr(obj, 0);
                waters.add(tempWaterAndInd.getKey());
            }
            fis.close();
        }
        return waters;
    }

    private Pair<Water, Integer> getObjectFromStr(String strObj, int startInd) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        // get class name, create class and water object with this name
        Pair<String, String> classAndClassName = this.getValueByDelims(strObj.substring(startInd), ":(", "):");
        String className = classAndClassName.getValue();
        Class<?> waterClass = Class.forName(className);
        Water water = (Water) waterClass.newInstance();

        // get current index to parse
        int currIndex = startInd + ":(class=".length() + className.length() + "):".length();

        // get and set fields to water object
        String fieldName, fieldValue;
        char currChar;
        for (int i = currIndex + 2; i < strObj.length(); i++) {

            // init strings for field name and value
            fieldName = "";
            fieldValue = "";

            // get name of field
            while ((currChar = strObj.toCharArray()[i]) != '=') {
                fieldName += currChar;
                i++;
            }

            // if aggregated obj (symbol '[') - recursively get this object
            i++;
            if (strObj.toCharArray()[i] == '[') {
                Pair<Water, Integer> agrWaterAndBracket = this.getObjectFromStr(strObj, i + 1);

                // set object received recursively
                Water agrWater = agrWaterAndBracket.getKey();
                i = agrWaterAndBracket.getValue() + 1;
                Field waterField = water.getClass().getDeclaredField(fieldName);
                waterField.setAccessible(true);
                waterField.set(water, agrWater);

            } else {
                // if not aggregated - get value of field
                while ((currChar = strObj.toCharArray()[i]) != ')') {
                    fieldValue += currChar;
                    i++;
                }

                // set field value to water object
                water = this.setFieldValue(water, fieldName, fieldValue);
            }

            // check if string is end - stop parsing
            i += 2;
            if (i >= strObj.length()) {
                break;
            }

            // if end of aggregated object (symbol ']') - recursively return with aggregated object
            if (strObj.toCharArray()[i] == ']') {
                return new Pair<Water, Integer>(water, i);
            } else {
                // else - continue parsing
                i++;
            }
        }
        // return deserialized object
        return new Pair<Water, Integer>(water, 0);
    }

    private Water setFieldValue(Water water, String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = utils.getFieldByName(water, fieldName);
        Class fieldType = field.getType();
        water = utils.setFieldValue(water, field, fieldValue);
        return water;
    }

    private Pair<String, String> getValueByDelims(String str, String delim1, String delim2) {
        // get value from string with format delim1 fieldName=fieldValue delim2
        int starts = str.indexOf(delim1);
        int equalInd = str.indexOf("=");
        int ends = str.indexOf(delim2);
        String fieldName = str.substring(starts + delim1.length(), equalInd);
        String fieldValue = str.substring(equalInd + 1, ends);
        return new Pair<String, String>(fieldName, fieldValue);
    }

}
