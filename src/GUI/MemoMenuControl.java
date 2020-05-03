package GUI;

import CalendarSystem.Event;
import CalendarSystem.Memo;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoMenuControl extends Controller {

    @FXML
    private Button returnToMenu;
    @FXML
    private TableView<Memo> memoTable;
    @FXML
    private MenuItem editMemo;
    @FXML
    private MenuItem deleteMemo;
    @FXML
    private MenuItem viewEvents;
    @FXML
    private TableColumn<Memo, String> memoContent;
    @FXML
    private ComboBox<Event> events;
    @FXML
    private TextField newMemoNote;
    @FXML
    private Label successMsg;
    @FXML
    private Label errorMsg;
    @FXML
    private Label createEventLabel;
    @FXML
    private Label newMemoLabel;


    @FXML
    private void goBackToMenu() throws IOException {
        setScreen("MainMenuScene.fxml", returnToMenu);
    }

    @Override
    protected void initScreen() {
        //Populate memoTable
        memoContent.setCellValueFactory(new PropertyValueFactory<>("note"));
        ObservableList<Memo> memoTableItems = FXCollections.observableArrayList();
        memoTableItems.addAll(getCalendar().getMyMemos().getMemos());
        memoTable.setItems(memoTableItems);
        for (Event e : getCalendar().getMyEvents()) {
            events.getItems().add(e);
        }
        initSelectionSettings();
    }

    private void initSelectionSettings() {

        memoTable.setPlaceholder(new Label("No Memos Found"));
        memoTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        memoTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Memo>) change -> {
            if (change.getList().size() == 1) {
                deleteMemo.setVisible(true);
                editMemo.setVisible(true);
                viewEvents.setVisible(true);
            } else if (change.getList().size() > 1) {
                deleteMemo.setVisible(true);
                editMemo.setVisible(false);
                viewEvents.setVisible(false);
            } else {
                deleteMemo.setVisible(false);
                editMemo.setVisible(false);
                viewEvents.setVisible(false);
            }
        });

    }

    @FXML
    private void editMemo() throws IOException {

        Memo memo = memoTable.getSelectionModel().getSelectedItem();

        Label instructions = new Label("Memo note:");
        TextField note = new TextField(memo.getNote());
        Button save = new Button("Save");
        PopUp editMemo = new PopUp("Edit Memo", getTheme(), 9.0, 40, 50, 10, 20);
        editMemo.getContent().addAll(instructions, note, save);
        save.setOnAction(e -> {
            try {
                if (!note.getText().equals("")) {
                    memo.setNote(note.getText());
                }
            } catch (NullPointerException nullp) {
            }
            editMemo.exit();
        });
        getCalendarManager().saveToFile();
        editMemo.display();
    }

    @FXML
    private void deleteMemos() throws IOException {
        ArrayList<Memo> items = new ArrayList<>(memoTable.getSelectionModel().getSelectedItems());
        for (Memo m : items) {
            getCalendar().deleteMemo(m);
            memoTable.getItems().remove(m);
        }
        getCalendarManager().saveToFile();
    }

    @FXML
    private void createNewMemo() throws IOException {
        resetErrorMessages();

        if (newMemoNote.getText().equals("")) {
            errorMsg.setText("Memo cannot be empty!");
            errorMsg.setTextFill(Paint.valueOf("red"));
            errorMsg.setVisible(true);
            newMemoLabel.setTextFill(Paint.valueOf("red"));
        }

        if (events.getValue() == null) {
            errorMsg.setText("Please select an event!");
            errorMsg.setTextFill(Paint.valueOf("red"));
            createEventLabel.setTextFill(Paint.valueOf("red"));
            errorMsg.setVisible(true);
        }

        if (!newMemoNote.getText().equals("") && events.getValue() != null) {
            String note = newMemoNote.getText();
            List<Event> l = new ArrayList<>();
            l.add(events.getValue());
            successMsg.setVisible(true);
            getCalendar().getMyMemos().createMemo(l, note);
            memoTable.getItems().clear();
            memoTable.getItems().addAll(getCalendar().getMyMemos().getMemos());
        }

        getCalendarManager().saveToFile();


    }

    @FXML private void viewRelatedEvents() {
        Memo selectedMemo = memoTable.getSelectionModel().getSelectedItem();
        Label eventsForMemo = new Label("Events for this memo:");
        ListView<Event> relatedEvents = new ListView<>();
        relatedEvents.getItems().addAll(getCalendar().findEvent(selectedMemo));
        relatedEvents.setOnMouseClicked(e -> {
            ObservableList<Event> eventsToView = relatedEvents.getSelectionModel().getSelectedItems();
            if (eventsToView.size() == 1 ) {
                try {
                    createEventView(eventsToView.get(0));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        PopUp eventViewer = new PopUp("Event Viewer", getTheme(), 6.0, 10, 10, 15, 15);
        eventViewer.getContent().addAll(eventsForMemo, relatedEvents);
        eventViewer.display();
    }

    private void createEventView(Event event) throws IOException {
        Stage window = new Stage();
        window.setTitle(event.getEventName());
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        //Create new scene to display
        FXMLLoader loader = setNewWindowAndGetLoader("EventViewScene.fxml", window, 600, 350);
        EventViewControl evc = loader.getController();
        evc.displayEvent(event);
        window.showAndWait();
    }

    private void resetErrorMessages() {
        successMsg.setVisible(false);
        errorMsg.setText("Invalid Input");
        errorMsg.setVisible(false);
        for (Label errorLabel : new Label[]{newMemoLabel, createEventLabel, errorMsg, successMsg}) {
            if (getTheme().equals("GUI/Dark.css")) {
                errorLabel.setTextFill(Paint.valueOf("white"));
            } else {
                errorLabel.setTextFill(Paint.valueOf("black"));
            }
        }
    }
}
