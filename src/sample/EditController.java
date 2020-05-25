package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.util.Pair;

import sample.classes.*;
import sample.factory.*;

import javafx.fxml.FXML;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class EditController {

    private Stage dialogStage;
    private Water water;
    private boolean okClicked = false;
    ArrayList<Pair<Pair<Water, Field>, TextField>> allTextFields = new ArrayList<>();

    @FXML
    ScrollPane paneForFields;

    @FXML
    private void initialize() { }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return this.okClicked;
    }

    public void setWater(Water water) throws InstantiationException, IllegalAccessException {
        this.water = water;
        this.createFielsForEdit(paneForFields, water);
    }

    @FXML
    public void btnOk_onClick() throws IllegalAccessException {
        if (this.isCorrectFields()) {
            for (Pair<Pair<Water, Field>, TextField> allTextField : this.allTextFields) {
                Pair<Water, Field> fieldWithClass = allTextField.getKey();
                Water classOjb = fieldWithClass.getKey();
                Field field = fieldWithClass.getValue();

                TextField text = allTextField.getValue();
                String textValue = text.getText();
                Class fieldType = field.getType();

                if (fieldType == boolean.class) {
                    boolean obj = Boolean.parseBoolean(textValue);
                    field.setBoolean(classOjb, obj);
                } else if (fieldType == char.class) {
                    char obj = textValue.charAt(0);
                    field.setChar(classOjb, obj);
                } else if (fieldType == byte.class) {
                    byte obj = Byte.parseByte(textValue);
                    field.setByte(classOjb, obj);
                } else if (fieldType == short.class) {
                    short obj = Short.parseShort(textValue);
                    field.setShort(classOjb, obj);
                } else if (fieldType == int.class) {
                    int obj = Integer.parseInt(textValue);
                    field.setInt(classOjb, obj);
                } else if (fieldType == long.class) {
                    long obj = Long.parseLong(textValue);
                    field.setLong(classOjb, obj);
                } else if (fieldType == float.class) {
                    float obj = Float.parseFloat(textValue);
                    field.setFloat(classOjb, obj);
                } else if (fieldType == double.class) {
                    double obj = Double.parseDouble(textValue);
                    field.setDouble(classOjb, obj);
                } else if (fieldType == String.class) {
                    field.set(classOjb, textValue);
                }
            }
            this.allTextFields.clear();
            this.okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void btnCancel_onClick() {
        dialogStage.close();
    }

    public boolean isCorrectFields() {
        return true;
    }

    public void btnEdit_onClick(Water water) {
        boolean okClicked = utils.showWaterEditDialog(water);

    }
    public void createFielsForEdit(ScrollPane parentPane, Water water) throws IllegalAccessException, InstantiationException {
        // Get types of fields and field objects with reflection
        Pair<ArrayList<String>, ArrayList<Field>> fields = utils.getAllDeclaredFields(water.getClass());
        ArrayList<String> fieldsType = fields.getKey();
        ArrayList<Field> fieldsObj = fields.getValue();

        // create pane for space for editing
        VBox pane = new VBox();

        pane.setSpacing(35);
        pane.setPadding(new Insets(10, 0, 10, 10));

        // creating textfield for edit fields of classes
        for (int i = 0; i < fieldsType.size(); i++) {
            HBox row = new HBox();
            row.setSpacing(15);

            //create name for field
            Label name = new Label(fieldsObj.get(i).getName());
            name.setPrefWidth(135);
            row.getChildren().add(name);

            // if not primitive class (and not String) => create TextField, else: create TitledPane ...
            // ... and recursive invoke for creating TextFields of class
            if (utils.isPrimitiveClass(fieldsType.get(i))) {
                TextField text = new TextField();
                text.setPrefSize(255, 30);

                // get the value of field
                text.setText(String.valueOf(fieldsObj.get(i).get(water)));
                this.allTextFields.add(new Pair<Pair<Water, Field>, TextField>(new Pair<Water, Field>(water, fieldsObj.get(i)), text));

                row.getChildren().add(text);

            } else {
                Water tempWater = (Water)fieldsObj.get(i).get(water);
                Button btnEdit = new Button("Edit " + tempWater.getName());
                btnEdit.setOnAction(actionEvent -> {
                        this.btnEdit_onClick(tempWater);
                });
                row.getChildren().add(btnEdit);
            }
            pane.getChildren().add(row);
        }
        parentPane.setContent(pane);
    }

}
