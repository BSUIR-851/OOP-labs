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
import javafx.stage.Window;
import javafx.util.Pair;
import sample.serialize.serializators.Serializator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class utils {
    public static ArrayList<SerializatorFactory> serFactories = new ArrayList<>(3);

    public static boolean isPrimitiveClass(String className) {
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

    public static boolean showWaterEditDialog(Water water) {
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
        ArrayList<Water> waters = new ArrayList<>();

        FileChooser fileChooser = getFileChooser("Open file");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Serializator serializator = serFactories.get(1).Create();
            waters = serializator.deserializeFromFile(file);
        }
        return waters;
    }

    public static boolean saveObjects(ArrayList<Water> waterObjects, Window window) throws IOException {
        boolean isCorrect = false;
        FileChooser fileChooser = getFileChooser("Save file");
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            Serializator serializator = serFactories.get(1).Create();
            isCorrect = serializator.serializeToFile(file, waterObjects);
        }
        return isCorrect;
    }

}
























