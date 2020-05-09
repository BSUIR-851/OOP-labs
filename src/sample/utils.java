package sample;

import javafx.util.Pair;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class utils {
    public static Boolean isPrimitiveClass(String className) {
        switch (className) {
            case "boolean":
            case "char":
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "String":
                return true;
            default:
                return false;
        }

    }

    public static Pair<ArrayList<String>, ArrayList<Field>> getAllDeclaredFields(Class curr) {
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> fieldsType = new ArrayList<>();
        for (Field field: curr.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
            fieldsType.add(field.getType().getSimpleName());
        }
        while ((curr = curr.getSuperclass()) != null) {
            for (Field field: curr.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
                fieldsType.add(field.getType().getSimpleName());
            }
        }
        return new Pair<ArrayList<String>, ArrayList<Field>>(fieldsType, fields);
    }

    public static ArrayList<String[]> editNameOfFieldsType(ArrayList<String> fields) {
        ArrayList<String[]> editedFields = new ArrayList<>(fields.size());
        String[] subName;
        for (String field: fields) {
            subName = field.split(" ");
            editedFields.add(subName);
        }

        return editedFields;
    }

}
