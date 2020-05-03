package GUI;

import CalendarSystem.*;
import CalendarSystem.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertMenuControl extends Controller{
    @FXML private TableView<AlertSystemData> upcomingTableView;
    @FXML private TableView<Alert> allTableView;
    @FXML private TableColumn<Alert, String> allNameColumn;
    @FXML private TableColumn<Alert, String> allDataColumn;
    @FXML private TableColumn<AlertSystemData, String> nameColumn;
    @FXML private TableColumn<AlertSystemData, String> messageColumn;
    @FXML private TableColumn<AlertSystemData, LocalDateTime> timeColumn;
    @FXML private TableColumn<AlertSystemData, String> typeColumn;
    @FXML private Button returnToMenu;

    /**
     * initialize the tableview in the scene
     */
    @Override
    protected void initScreen() {
        //Populate table
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ObservableList<AlertSystemData> upcomingAlertTableItems = FXCollections.observableArrayList();

        //add by running through alert system
        Map<LocalDateTime, List<Alert>> dateAlertsMap = getCalendar().getMyAlerts().getDateAlertsMap();
        Set<LocalDateTime> alertTimes = getCalendar().getMyAlerts().getCurrAlertTime();
        for(LocalDateTime d: alertTimes){
            List<Alert> alerts = dateAlertsMap.get(d);
            for(Alert a: alerts){
                upcomingAlertTableItems.add(new AlertSystemData(d, a.getName(), a.getMessage(), a.getAlertType()));
            }
        }

        //Populate all alerts table
        allNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allDataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        ObservableList<Alert> allAlertTableItems = FXCollections.observableArrayList();
        allAlertTableItems.addAll(getCalendar().getAllAlerts());

        allTableView.setItems(allAlertTableItems);
        upcomingTableView.setItems(upcomingAlertTableItems);
    }

    /**
     * create a new individual alert
     */
    @FXML private void createIndividualAlert(){
        try{
            openWindowAndGetLoader("Create Individual Alert",  "IAlertCreator.fxml");
            refreshTables();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * create a new frequent alert
     */
    @FXML private void createFrequentAlert(){
        try{
            openWindowAndGetLoader("Create Frequent Alert", "FAlertCreator.fxml");
            refreshTables();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * edit the selected alert
     */
    @FXML private void editAlert(){
        Alert selected = allTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = allTableView.getSelectionModel().getSelectedItem();

            //Make New pop up window
            Stage alertEditWindow = new Stage();
            alertEditWindow.setTitle("Edit Alert");
            alertEditWindow.initModality(Modality.APPLICATION_MODAL);
            alertEditWindow.setResizable(false);

            try {
                if (alert instanceof IndividualAlert) {
                    FXMLLoader loader = setNewWindowAndGetLoader("IAlertCreator.fxml",
                            alertEditWindow, 600, 350);
                    IAlertCreatorControl alertEditor = loader.getController();
                    alertEditor.setAlert((IndividualAlert) alert);
                    alertEditor.setEdit_Mode(true);
                    alertEditWindow.showAndWait();
                }
                if (alert instanceof FrequentAlert) {
                    FXMLLoader loader = setNewWindowAndGetLoader("FAlertCreator.fxml",
                            alertEditWindow, 600, 350);
                    FAlertCreatorControl alertEditor = loader.getController();
                    alertEditor.setAlert((FrequentAlert) alert);
                    alertEditor.setEdit_Mode(true);
                    alertEditWindow.showAndWait();
                }
                refreshTables();
                getCalendarManager().saveToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * delete the selected alert
     * @throws IOException if not saved properly
     */
    @FXML private void deleteAlert() throws IOException{
        Alert selected = allTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            getCalendar().deleteAlert(selected);
            refreshTables();
            getCalendarManager().saveToFile();
        }
    }

    /**
     * return to event menu scene
     * @throws IOException if fails to return to menu
     */
    @FXML private void setReturnToMenu() throws IOException{
        setScreen("MainMenuScene.fxml", returnToMenu);
    }

    /**
     * refresh the tables
     */
    private void refreshTables(){
        initScreen();
        allTableView.refresh();
        upcomingTableView.refresh();
    }
}
