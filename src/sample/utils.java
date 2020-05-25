package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.classes.Water;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class utils {
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

}
