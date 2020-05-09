package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import jdk.internal.dynalink.support.TypeConverterFactory;
import sample.classes.*;
import sample.factory.*;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Controller {
    ArrayList<WaterFactory> factories = new ArrayList<>(4);
    ArrayList<Water> waterObjects = new ArrayList<>();

    ArrayList<Pair<Pair<Water, Field>, TextField>> allTextFields = new ArrayList<>();

    private Boolean isEditing = false;
    private Water currWater;

    @FXML
    private Button btnDelete,
                   btnChangeSave;
    @FXML
    private MenuButton btnAdd;

    @FXML
    private ListView lvObjects;

    @FXML
    private ScrollPane paneForFields;

    @FXML
    private TitledPane titledPaneForFields;

    @FXML
    public void initialize() {
        // creating all needed factories
        this.factories.add(new SeaFactory());
        this.factories.add(new RiverFactory());
        this.factories.add(new InterIslandSeaFactory());
        this.factories.add(new InlandSeaFactory());
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
            name.setPrefWidth(70);

            // if not primitive class (and not String) => create TextField, else: create TitledPane ...
            // ... and recursive invoke for creating TextFields of class
            if (utils.isPrimitiveClass(fieldsType.get(i))) {
                TextField text = new TextField();
                text.setPrefSize(180, 30);

                // get the value of field
                text.setText(String.valueOf(fieldsObj.get(i).get(water)));
                this.allTextFields.add(new Pair<Pair<Water, Field>, TextField>(new Pair<Water, Field>(water, fieldsObj.get(i)), text));

                row.getChildren().add(name);
                row.getChildren().add(text);

            } else {
                ScrollPane newClassPane = new ScrollPane();

                TitledPane newTitleClassPane = new TitledPane();
                newTitleClassPane.setText(fieldsObj.get(i).getName());
                newTitleClassPane.setPrefWidth(300);

                this.createFielsForEdit(newClassPane, (Water)fieldsObj.get(i).get(water));

                newTitleClassPane.setContent(newClassPane);

                row.getChildren().add(newTitleClassPane);
            }
            pane.getChildren().add(row);
        }
        parentPane.setContent(pane);
    }

    public void addTolvObjects(Water water) {
        // add created object to ListView
        this.lvObjects.getItems().add(water.getName());
    }

    public void createWaterObject(int index) {
        // create selected object
        Water waterObj = this.factories.get(index).Create();
        this.waterObjects.add(waterObj);
        this.addTolvObjects(waterObj);
    }

    @FXML // create sea
    public void addSea_onClick() { this.createWaterObject(0); }

    @FXML // create river
    public void addRiver_onClick() { this.createWaterObject(1); }

    @FXML // create InterIsland Sea
    public void addInterIslandSea_onClick() { this.createWaterObject(2); }

    @FXML // create Inland Sea
    public void addInlandSea_onClick() { this.createWaterObject(3); }

    @FXML
    public void btnDelete_onClick() {
        // deleting object selected from ListView
        int index = this.lvObjects.getSelectionModel().getSelectedIndex();
        this.waterObjects.remove(index);
        this.redrawListView();
    }

    public void changeButtonsVisibility() {
        // change visibility of "Add" button, "Delete" button ...
        // ... for edit object selected from ListView
        this.isEditing = !this.isEditing;
        this.btnChangeSave.setText(this.isEditing ? "Save" : "Change");
        this.btnAdd.setDisable(!this.btnAdd.isDisabled());
        this.btnDelete.setDisable(!this.btnDelete.isDisabled());
        this.lvObjects.setDisable(!this.lvObjects.isDisabled());
        if (!this.isEditing) {
            this.paneForFields.setContent(null);
            this.titledPaneForFields.setText("");
        }
    }

    public void redrawListView() {
        this.lvObjects.getItems().clear();
        for (Water water : this.waterObjects) {
            this.lvObjects.getItems().add(water.getName());
        }
    }

    @FXML
    public void btnChangeSave_onClick() throws InstantiationException, IllegalAccessException {
        // change object selected from ListView
        int index = this.lvObjects.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            this.changeButtonsVisibility();
            this.currWater = this.waterObjects.get(index);
            if (this.isEditing) {
                this.titledPaneForFields.setText(this.currWater.getName());
                this.createFielsForEdit(this.paneForFields, this.currWater);

            } else if (!this.isEditing) {
                for (int i = 0; i < this.allTextFields.size(); i++) {
                    Pair<Water, Field> fieldWithClass = this.allTextFields.get(i).getKey();
                    Water classOjb = fieldWithClass.getKey();
                    Field field = fieldWithClass.getValue();

                    TextField text = this.allTextFields.get(i).getValue();
                    String textValue = text.getText();
                    String fieldType = field.getType().getSimpleName();

                    switch (fieldType) {
                        case "boolean": {
                            boolean obj = Boolean.parseBoolean(textValue);
                            field.setBoolean(classOjb, obj);
                            break;
                        }
                        case "char": {
                            char obj = textValue.charAt(0);
                            field.setChar(classOjb, obj);
                            break;
                        }
                        case "byte": {
                            byte obj = Byte.parseByte(textValue);
                            field.setByte(classOjb, obj);
                            break;
                        }
                        case "short": {
                            short obj = Short.parseShort(textValue);
                            field.setShort(classOjb, obj);
                            break;
                        }
                        case "int": {
                            int obj = Integer.parseInt(textValue);
                            field.setInt(classOjb, obj);
                            break;
                        }
                        case "long": {
                            long obj = Long.parseLong(textValue);
                            field.setLong(classOjb, obj);
                            break;
                        }
                        case "float": {
                            float obj = Float.parseFloat(textValue);
                            field.setFloat(classOjb, obj);
                            break;
                        }
                        case "double": {
                            double obj = Double.parseDouble(textValue);
                            field.setDouble(classOjb, obj);
                            break;
                        }
                        case "String":
                            field.set(classOjb, textValue);
                            break;
                    }

                }
                this.allTextFields.clear();
                this.redrawListView();
                this.currWater = null;
            }
        }
    }


}
