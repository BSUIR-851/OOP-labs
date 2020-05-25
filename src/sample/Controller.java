package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.util.Pair;

import sample.classes.*;
import sample.factory.*;

import javafx.fxml.FXML;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Controller {
    ArrayList<WaterFactory> factories = new ArrayList<>(4);
    ArrayList<Water> waterObjects = new ArrayList<>();

    @FXML
    private Button btnDelete,
                   btnChange;
    @FXML
    private MenuButton btnAdd;

    @FXML
    private ListView<String> lvObjects;

    @FXML
    private ScrollPane paneForFields;

    @FXML
    private TitledPane titledPaneForFields;

    @FXML
    public void initialize() throws IllegalAccessException {
        // creating all needed factories
        this.factories.add(new SeaFactory());
        this.factories.add(new RiverFactory());
        this.factories.add(new InterIslandSeaFactory());
        this.factories.add(new InlandSeaFactory());

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

    private void alert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        //alert.initOwner(this.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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

    public void createWaterObject(int index) throws IllegalAccessException, InstantiationException {
        // create selected object
        Water waterObj = this.factories.get(index).Create();
        this.waterObjects.add(waterObj);
        this.addTolvObjects(waterObj);
        this.lvObjects.getSelectionModel().select(this.lvObjects.getItems().size() - 1);
        this.btnChange_onClick();
    }

    @FXML // create sea
    public void addSea_onClick() throws InstantiationException, IllegalAccessException { this.createWaterObject(0); }

    @FXML // create river
    public void addRiver_onClick() throws InstantiationException, IllegalAccessException { this.createWaterObject(1); }

    @FXML // create InterIsland Sea
    public void addInterIslandSea_onClick() throws InstantiationException, IllegalAccessException { this.createWaterObject(2); }

    @FXML // create Inland Sea
    public void addInlandSea_onClick() throws InstantiationException, IllegalAccessException { this.createWaterObject(3); }

    @FXML
    public void btnDelete_onClick() {
        // deleting object selected from ListView
        int index = this.lvObjects.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            this.waterObjects.remove(index);
            this.redrawListView();
        } else {
            this.alert("No Selection", "No water object Selected", "Please select a water object in the table.", Alert.AlertType.WARNING);
        }
    }

    public void changeButtonsVisibility() {
        // change visibility of "Add" button, "Delete" button ...
        // ... for edit object selected from ListView
        this.btnAdd.setDisable(!this.btnAdd.isDisabled());
        this.btnDelete.setDisable(!this.btnDelete.isDisabled());
        this.btnChange.setDisable(!this.btnChange.isDisabled());
        this.lvObjects.setDisable(!this.lvObjects.isDisabled());
    }

    public void redrawListView() {
        this.lvObjects.getItems().clear();
        for (Water water : this.waterObjects) {
            this.addTolvObjects(water);
        }
    }

    @FXML
    public void btnChange_onClick() throws InstantiationException, IllegalAccessException {
        // change object selected from ListView
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
            this.alert("No Selection", "No water object Selected", "Please select a water object in the table.", Alert.AlertType.WARNING);
        }
    }


}
