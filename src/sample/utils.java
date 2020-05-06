package sample;

import javafx.util.Pair;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class utils {

    public static Pair<ArrayList<String[]>, ArrayList<Field>> getAllDeclaredFields(Class curr) {
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> fieldsType = new ArrayList<>();
        for (Field field: curr.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
            fieldsType.add(String.valueOf(field.getType()));
        }
        while ((curr = curr.getSuperclass()) != null) {
            for (Field field: curr.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
                fieldsType.add(String.valueOf(field.getType()));
            }
        }
        return new Pair<ArrayList<String[]>, ArrayList<Field>>(editNameOfFieldsType(fieldsType), fields);
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
