package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
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

    private Boolean isEditing = false;

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


    public void createFielsForEdit(ScrollPane parentPane, Class water) throws IllegalAccessException, InstantiationException {
        // Get types of fields and field objects with reflection
        Pair<ArrayList<String[]>, ArrayList<Field>> fields = utils.getAllDeclaredFields(water);
        ArrayList<String[]> fieldsType = fields.getKey();
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

            // if not class (and not String) => create TextField, else: create TitledPane ...
            // ... and recursive invoke for creating TextFields of class
            if ((!fieldsType.get(i)[0].equals("class")) || (fieldsType.get(i)[fieldsType.get(i).length - 1].equals("java.lang.String"))) {
                TextField text = new TextField();
                text.setPrefSize(180, 30);

                // get the value of field
                Object element = water.newInstance();
                text.setText(String.valueOf(fieldsObj.get(i).get(element)));

                row.getChildren().add(name);
                row.getChildren().add(text);

            } else {
                ScrollPane newClassPane = new ScrollPane();

                TitledPane newTitleClassPane = new TitledPane();
                newTitleClassPane.setText(fieldsObj.get(i).getName());
                newTitleClassPane.setPrefWidth(300);

                this.createFielsForEdit(newClassPane, fieldsObj.get(i).getType());

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
        btnDelete.setText("Clicked!");
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

    @FXML
    public void btnChangeSave_onClick() throws InstantiationException, IllegalAccessException {
        // change object selected frov ListView
        this.changeButtonsVisibility();
        int index = this.lvObjects.getSelectionModel().getSelectedIndex();
        if ((index >= 0) && (this.isEditing)) {
            Water waterObj = this.waterObjects.get(index);
            this.titledPaneForFields.setText(waterObj.getName());
            this.createFielsForEdit(this.paneForFields, waterObj.getClass());
        }
    }


}
