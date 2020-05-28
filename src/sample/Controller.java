package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.util.Pair;

import sample.classes.*;
import sample.factory.*;
import sample.serialize.factories.*;

import javafx.fxml.FXML;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Controller {
    private ArrayList<WaterFactory> factories = new ArrayList<>(4);
    private ArrayList<Water> waterObjects = new ArrayList<>();

    @FXML
    private Button btnDelete,
                   btnEdit;
    @FXML
    private MenuButton btnAdd;

    @FXML
    private ListView<String> lvObjects;

    @FXML
    private ScrollPane paneForFields;

    @FXML
    private TitledPane titledPaneForFields;

    @FXML
    private MenuItem mnOpen,
                     mnSave;

    @FXML
    public void initialize() {
        // creating all needed water factories
        this.factories.add(new SeaFactory());
        this.factories.add(new RiverFactory());
        this.factories.add(new InterIslandSeaFactory());
        this.factories.add(new InlandSeaFactory());

        // created all needed serializer factories
        utils.serFactories.add(new BinSerializatorFactory());
        utils.serFactories.add(new JsonSerializatorFactory());
        utils.serFactories.add(new CustomSerializatorFactory());

        // set listener for changing selected index of listview with created objects
        this.lvObjects.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                try {
                    this.paneForFields.setContent(null);
                    this.titledPaneForFields.setText("");
                    int index = this.lvObjects.getSelectionModel().getSelectedIndex();
                    if (index >= 0) {
                        Water tempWater = this.waterObjects.get(index);
                        this.titledPaneForFields.setText(tempWater.getName());
                        this.showFieldsInfo(this.paneForFields, tempWater);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        );
    }

    @FXML
    public void miSave_onClick()  {
        // save objects to file (serialize)
        utils.saveObjects(this.waterObjects);
    }

    @FXML
    public void miOpen_onClick() {
        // load objects from file (deserialize)
        ArrayList<Water> waters = utils.loadObjects();
        if (waters.size() >= 1) {
            this.waterObjects = waters;
            this.redrawListView();
        }
    }

    public void showFieldsInfo(ScrollPane parentPane, Water water) throws IllegalAccessException {
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

            // if not primitive class (and not String) => create Label, else: create TitledPane ...
            // ... and recursive invoke for creating Label of class
            if (utils.isPrimitiveClass(fieldsType.get(i))) {
                Label text = new Label(String.valueOf(fieldsObj.get(i).get(water)));
                text.setPrefSize(180, 30);

                row.getChildren().add(name);
                row.getChildren().add(text);

            } else {
                ScrollPane newClassPane = new ScrollPane();

                Water tempWater = (Water) fieldsObj.get(i).get(water);

                TitledPane newTitleClassPane = new TitledPane();
                newTitleClassPane.setText(fieldsObj.get(i).getName());
                newTitleClassPane.setPrefWidth(parentPane.getWidth() - 50);

                this.showFieldsInfo(newClassPane, tempWater);

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
        this.lvObjects.getSelectionModel().select(this.lvObjects.getItems().size() - 1);
        this.btnEdit_onClick();
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
        if (index >= 0) {
            this.waterObjects.remove(index);
            this.redrawListView();
        } else {
            utils.alert("No Selection", "No water object Selected", "Please select a water object in the table.", Alert.AlertType.WARNING);
        }
    }

    public void changeButtonsVisibility() {
        // change visibility of "Add" button, "Delete" button ...
        // ... for edit object selected from ListView
        this.btnAdd.setDisable(!this.btnAdd.isDisabled());
        this.btnDelete.setDisable(!this.btnDelete.isDisabled());
        this.btnEdit.setDisable(!this.btnEdit.isDisabled());
        this.lvObjects.setDisable(!this.lvObjects.isDisabled());
    }

    public void redrawListView() {
        this.lvObjects.getItems().clear();
        for (Water water : this.waterObjects) {
            this.addTolvObjects(water);
        }
    }

    @FXML
    public void btnEdit_onClick() {
        // edit object selected from ListView
        int index = this.lvObjects.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            this.changeButtonsVisibility();
            Water tempWater = this.waterObjects.get(index);
            boolean okClicked = utils.showWaterEditDialog(tempWater);
            if (okClicked) {
                this.paneForFields.setContent(null);
                this.redrawListView();
                this.lvObjects.getSelectionModel().select(index);
            }
            this.changeButtonsVisibility();

        } else {
            utils.alert("No Selection", "No water object Selected", "Please select a water object in the table.", Alert.AlertType.WARNING);
        }
    }


}
