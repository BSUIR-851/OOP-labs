package sample.serialize.serializators;

import javafx.util.Pair;
import sample.classes.Water;
import sample.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CustomSerializator extends Serializator {
    public boolean serializeToFile(File file, ArrayList<Water> waterObjects) throws IllegalAccessException {
        boolean isCorrect = false;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            String serObject;
            if (fos != null) {
                for (Water water: waterObjects) {
                    Pair<ArrayList<String>, ArrayList<Field>> fields = utils.getAllDeclaredFields(water.getClass());
                    ArrayList<String> fieldsType = fields.getKey();
                    ArrayList<Field> fieldsObj = fields.getValue();

                    serObject = ";" + this.getStringFields(fieldsObj, water) + ";";
                    fos.write(serObject.getBytes());
                }
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

    public String getStringFields(ArrayList<Field> fields, Water water) throws IllegalAccessException {
        String strFields = "";
        strFields += ":(class=" + water.getClass().getName() + "):";
        for (Field field: fields) {
            strFields += "|(";
            if (utils.isPrimitiveClass(field.getType().getSimpleName())) {
                strFields += field.getName() + "=" + field.get(water).toString();
            } else {
                Pair<ArrayList<String>, ArrayList<Field>> agrClassNameAndFields = utils.getAllDeclaredFields(field.getType());
                strFields += field.getName() + "=[" + this.getStringFields(agrClassNameAndFields.getValue(), (Water) field.get(water)) + "]";
            }
            strFields += ")|";
        }
        return strFields;
    }

    public ArrayList<Water> deserializeFromFile(File file) {
        ArrayList<Water> waters = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(file);
            if (fis != null) {
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                String serObjects = new String(data, StandardCharsets.UTF_8);
                StringTokenizer st = new StringTokenizer(serObjects, ";;");
                StringTokenizer fieldsToken;
                while (st.hasMoreTokens()) {
                    String obj = st.nextToken();
                    System.out.println(obj);
                    int classStartInd = 8;
                    int classEndInd = obj.indexOf("):");
                    Pair<String, String> fieldValue = this.getValueByDelims(obj, ":(", "):");

                    String className = fieldValue.getValue();
                    
                    fieldsToken = new StringTokenizer(obj, "[]");
                    //System.out.println(fieldsToken.nextToken());
                    Class<?> waterClass = Class.forName(className);
                    Water water = (Water) waterClass.newInstance();
                    getValueByDelims(obj, ":(", "):");
                    waters.add(water);
                }
                fis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return waters;
    }

    public Pair<String, String> getValueByDelims(String str, String delim1, String delim2) {
        int starts = str.indexOf(delim1);
        int equalInd = str.indexOf("=");
        int ends = str.indexOf(delim2);
        String fieldName = str.substring(starts + delim1.length(), equalInd);
        String fieldValue = str.substring(equalInd + 1, ends);
        return new Pair<String, String>(fieldName, fieldValue);
    }

}
