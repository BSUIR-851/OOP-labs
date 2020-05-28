package sample;

import sample.classes.Water;
import sample.serialize.factories.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import sample.serialize.serializators.Serializator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class utils {
    // declare list for serializers
    public static ArrayList<SerializatorFactory> serFactories = new ArrayList<>(3);

    public static boolean isPrimitiveClass(String className) {
        // check if class is primitive
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
        // get declared fields of class curr
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> fieldsType = new ArrayList<>();
        for (Field field: curr.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
            fieldsType.add(field.getType().getSimpleName());
        }

        // get parent's declared fields of class curr
        while ((curr = curr.getSuperclass()) != null) {
            for (Field field: curr.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
                fieldsType.add(field.getType().getSimpleName());
            }
        }
        return new Pair<ArrayList<String>, ArrayList<Field>>(fieldsType, fields);
    }

    public static Field getFieldByName(Object object, String name) {
        Pair<ArrayList<String>, ArrayList<Field>> typesAndFields = getAllDeclaredFields(object.getClass());
        ArrayList<Field> fields = typesAndFields.getValue();
        for (Field field: fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public static Water setFieldValue(Water water, Field field, String fieldValue) throws IllegalAccessException {
        // set in field field value fieldValue

        Class fieldType = field.getType();
        if (fieldType == boolean.class) {
            boolean obj = Boolean.parseBoolean(fieldValue);
            field.setBoolean(water, obj);
        } else if (fieldType == char.class) {
            char obj = fieldValue.charAt(0);
            field.setChar(water, obj);
        } else if (fieldType == byte.class) {
            byte obj = Byte.parseByte(fieldValue);
            field.setByte(water, obj);
        } else if (fieldType == short.class) {
            short obj = Short.parseShort(fieldValue);
            field.setShort(water, obj);
        } else if (fieldType == int.class) {
            int obj = Integer.parseInt(fieldValue);
            field.setInt(water, obj);
        } else if (fieldType == long.class) {
            long obj = Long.parseLong(fieldValue);
            field.setLong(water, obj);
        } else if (fieldType == float.class) {
            float obj = Float.parseFloat(fieldValue);
            field.setFloat(water, obj);
        } else if (fieldType == double.class) {
            double obj = Double.parseDouble(fieldValue);
            field.setDouble(water, obj);
        } else if (fieldType == String.class) {
            field.set(water, fieldValue);
        }
        return water;
    }

    public static boolean showWaterEditDialog(Water water) {
        // show dialog for edit object
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("WaterEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit " + water.getName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWater(water);

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void alert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static FileChooser getFileChooser(String act) {
        // create file chooser dialog with .bin, .json, .txt
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(act);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Binary (*.bin)", "*.bin"),
                new FileChooser.ExtensionFilter("JSON (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("Self (*.txt)", "*.txt")
        );
        return fileChooser;
    }

    public static ArrayList<Water> loadObjects() {
        // load objects from file (deserialize)

        // init list for deserialized objects
        ArrayList<Water> waters = new ArrayList<>();

        // get file chooser dialog
        FileChooser fileChooser = getFileChooser("Open file");
        File file = fileChooser.showOpenDialog(new Stage());

        // get extension to correctly start deserializing
        if (file != null) {
            String extension = getFileExtension(file);
            int serIndex = -1;
            switch (extension) {
                case "bin":
                    serIndex = 0;
                    break;
                case "json":
                    serIndex = 1;
                    break;
                case "txt":
                    serIndex = 2;
                    break;
            }

            if (serIndex >= 0) {
                // get certain serializer from factory
                Serializator serializator = serFactories.get(serIndex).Create();
                // start deserializing
                waters = serializator.deserializeFromFile(file);
            } else {
                alert("Open error", "Incorrect file path", "Choose correct file!", Alert.AlertType.ERROR);
            }
        }
        return waters;
    }

    public static boolean saveObjects(ArrayList<Water> waterObjects) {
        // save objects to file (serialize)

        boolean isCorrect = false;

        // get file chooser dialog
        FileChooser fileChooser = getFileChooser("Save file");
        File file = fileChooser.showSaveDialog(new Stage());

        // get extension to correctly start serializing
        if (file != null) {
            String extension = getFileExtension(file);
            int serIndex = -1;
            switch (extension) {
                case "bin":
                    serIndex = 0;
                    break;
                case "json":
                    serIndex = 1;
                    break;
                case "txt":
                    serIndex = 2;
                    break;
            }
            if (serIndex >= 0) {
                // get certain serializer from factory
                Serializator serializator = serFactories.get(serIndex).Create();
                // start serializing
                try {
                    isCorrect = serializator.serializeToFile(file, waterObjects);
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                    alert("Save error", "Something gone wrong", e.toString(), Alert.AlertType.ERROR);
                }
            } else {
                alert("Save error", "Incorrect file path", "Choose correct file!", Alert.AlertType.ERROR);
            }
        }
        return isCorrect;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        String extension = "";
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
    }

}
























