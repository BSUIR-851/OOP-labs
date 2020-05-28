package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.util.Pair;

import sample.classes.*;

import javafx.fxml.FXML;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;


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

    public boolean isOkClicked() { return this.okClicked; }

    public void setWater(Water water) throws InstantiationException, IllegalAccessException {
        // create fields for edit water objects on edit form
        this.water = water;
        this.createFielsForEdit(paneForFields, water);
    }

    @FXML
    public void btnOk_onClick() throws IllegalAccessException {
        // save water object fields and then close edit window
        if (this.isCorrectFields()) {
            for (Pair<Pair<Water, Field>, TextField> allTextField : this.allTextFields) {
                Pair<Water, Field> fieldWithClass = allTextField.getKey();
                Water classOjb = fieldWithClass.getKey();
                Field field = fieldWithClass.getValue();

                TextField text = allTextField.getValue();
                String textValue = text.getText();

                classOjb = utils.setFieldValue(classOjb, field, textValue);

            }
            this.allTextFields.clear();
            this.okClicked = true;
            dialogStage.close();

        } else {
            utils.alert("Edit error", "Incorrect input!", "Please, chech your input data", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void btnCancel_onClick() {
        dialogStage.close();
    }

    public boolean isCorrectFields() {
        // check if all fields is correct
        boolean result = true;
        for (Pair<Pair<Water, Field>, TextField> allTextField : this.allTextFields) {
            TextField text = allTextField.getValue();
            String textValue = text.getText();
            if ((textValue == null) || textValue.length() == 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void btnEdit_onClick(Water water) {
        // open edit aggregated object
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
