package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import CalendarSystem.Event;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class EventMenuController extends Controller {

    @FXML
    private Button returnToMenu;
    @FXML
    private TextField searchBar;
    @FXML
    private ChoiceBox<String> eventSort;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private MenuItem linkEventOpt;
    @FXML
    private MenuItem deleteEvent;
    @FXML
    private MenuItem editEvent;
    @FXML
    private MenuItem duplicateEvent;
    @FXML
    private MenuItem viewEvent;
    @FXML
    private MenuItem shareEvent;
    @FXML
    private MenuItem postPoneEvent;
    @FXML
    private TableColumn<Event, String> eventName;
    @FXML
    private TableColumn<Event, LocalDateTime> eventStart;
    @FXML
    private TableColumn<Event, LocalDateTime> eventEnd;

    @FXML
    private void goBackToMenu() throws IOException {
        setScreen("MainMenuScene.fxml", returnToMenu);
    }

    @Override
    protected void initScreen() {
        //Populate eventTable
        eventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        eventEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        ObservableList<Event> eventTableItems = FXCollections.observableArrayList();
        eventTableItems.addAll(getCalendar().getMyEvents());
        eventTable.setItems(eventTableItems);
        initSelectionSettings();

        //Populate eventSort and set it so it activates on a new selection
        eventSort.getItems().addAll("All", "Past", "Current", "Upcoming", "Postponed");
        eventSort.setValue("All");
        eventSort.getSelectionModel().selectedItemProperty().addListener(
                (v, oldVal, newVal) -> sortEventTable(newVal));
    }

    private void initSelectionSettings() {
        eventTable.setPlaceholder(new Label("No Events Found"));
        eventTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        eventTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Event>) change -> {
            for (MenuItem option : new MenuItem[] {linkEventOpt, deleteEvent, editEvent, duplicateEvent, viewEvent,
                    shareEvent, postPoneEvent}) {
                option.setVisible(false);
            }
            if (change.getList().size() > 1) {
                linkEventOpt.setVisible(true);
                deleteEvent.setVisible(true);
            } else if (change.getList().size() == 1) {
                viewEvent.setVisible(true);
                deleteEvent.setVisible(true);
                editEvent.setVisible(true);
                duplicateEvent.setVisible(true);
                shareEvent.setVisible(true);
                postPoneEvent.setVisible(true);
            }
        });
    }

    @FXML private void sortEventTable(String sortBy) {
        eventTable.getItems().clear();
        switch (sortBy) {
            case "Past":
                eventTable.getItems().addAll(getCalendar().getPastEvents());
                break;
            case "Current":
                eventTable.getItems().addAll(getCalendar().getCurrentEvents());
                break;
            case "Upcoming":
                eventTable.getItems().addAll(getCalendar().getFutureEvents());
                break;
            case "Postponed":
                eventTable.getItems().addAll(getCalendar().findEvent(LocalDate.MIN));
                break;
            default:
                eventTable.getItems().addAll(getCalendar().getMyEvents());
        }
    }

    @FXML private void createNewEvent() throws IOException {
        //Make New pop up window
        Stage eventMakerWindow = new Stage();
        eventMakerWindow.setTitle("Create Event");
        eventMakerWindow.initModality(Modality.APPLICATION_MODAL);
        eventMakerWindow.setResizable(false);

        //Create new scene to display
        FXMLLoader loader = setNewWindowAndGetLoader("EventCreatorScene.fxml",
                eventMakerWindow, 600, 350);

        //Pass in additional table data
        EventCreatorControl eventMaker = loader.getController();
        eventMaker.setTableToModify(eventTable);

        eventMakerWindow.showAndWait();
    }

    @FXML private void linkEvents() {
        Label instructions = new Label("Event Series Name:");
        TextField eventsName = new TextField();
        Button createSeries = new Button("Create Series");
        PopUp linkName = new PopUp("Series Name", getTheme(), 9.0, 40, 50, 10, 20);
        linkName.getContent().addAll(instructions, eventsName, createSeries);
        createSeries.setOnAction(e -> {
            try {
                if (!eventsName.getText().equals("")) {
                    ArrayList<Event> toLink = new ArrayList<>(eventTable.getSelectionModel().getSelectedItems());
                    getCalendar().addSeries(eventsName.getText(), toLink);
                }
            } catch (NullPointerException nullp) {}
            linkName.exit();
        });
        linkName.display();
    }

    @FXML private void deleteEvents() throws IOException{
        ArrayList<Event> items = new ArrayList<>(eventTable.getSelectionModel().getSelectedItems());
        for (Event e : items) {
            getCalendar().deleteEvent(e);
            eventTable.getItems().remove(e);
        }
        getCalendarManager().saveToFile();
    }

    @FXML private void postPoneSelectedEvent() {
        Event event = eventTable.getSelectionModel().getSelectedItem();
        eventTable.getItems().remove(event);
        getCalendar().changeEventTime(event, LocalDateTime.MIN, LocalDateTime.MIN);
        eventTable.getItems().add(event);

    }

    // Edit events

    @FXML private void editEvent() throws IOException {

        Event event = eventTable.getSelectionModel().getSelectedItem();

        //Make New pop up window
        Stage eventMakerWindow = new Stage();
        eventMakerWindow.setTitle("Edit Event");
        eventMakerWindow.initModality(Modality.APPLICATION_MODAL);
        eventMakerWindow.setResizable(false);

        //Create new scene to display
        FXMLLoader loader = setNewWindowAndGetLoader("EventEditorScene.fxml",
                eventMakerWindow, 600, 350);

        //Pass in additional table data
        EventEditorControl eventEditor = loader.getController();
        eventEditor.setTableToModify(eventTable);
        eventEditor.setEventToModify(event);

        eventMakerWindow.showAndWait();
    }

    // Duplicate an event
    @FXML private void duplicateEvent () throws IOException {
        Event event = eventTable.getSelectionModel().getSelectedItem();

        //Make New pop up window
        Stage eventMakerWindow = new Stage();
        eventMakerWindow.setTitle("Duplicate Event");
        eventMakerWindow.initModality(Modality.APPLICATION_MODAL);
        eventMakerWindow.setResizable(false);

        //Create new scene to display
        FXMLLoader loader = setNewWindowAndGetLoader("DateTimeEntryScene.fxml",
                eventMakerWindow, 600, 350);

        //Pass in additional table data
        DateTimeEntryControl entry = loader.getController();
        entry.setTableToModify(eventTable);
        entry.setEventToModify(event);

        eventMakerWindow.showAndWait();
    }

    @FXML private void searchForEvent() {
        String userInput = searchBar.getText();
        eventTable.getItems().clear();
        if (!userInput.equals("")) {
            eventTable.getItems().addAll(getCalendar().findEventsBySeries(userInput));
            eventTable.getItems().addAll(getCalendar().findEvent(userInput));
            eventTable.getItems().addAll(getCalendar().findEventByMemoNote(userInput));
            if (getCalendar().getEvent(userInput) != null) {
                eventTable.getItems().add(getCalendar().getEvent(userInput));
            }
            try {
                eventTable.getItems().addAll(getCalendar().findEvent(LocalDate.parse(userInput,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            } catch (DateTimeParseException d) {}
        }
    }

    @FXML private void displayEvent() throws IOException {
        Event eventToDisplay = eventTable.getSelectionModel().getSelectedItem();
        //Make New pop up window
        Stage window = new Stage();
        window.setTitle(eventToDisplay.getEventName());
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);

        //Create new scene to display
        FXMLLoader loader = setNewWindowAndGetLoader("EventViewScene.fxml", window, 600, 350);
        EventViewControl evc = loader.getController();
        evc.displayEvent(eventToDisplay);
        window.showAndWait();
    }

    @FXML private void shareEventWithFriend() {
        Label instructions = new Label("Enter the recipient's username:");
        TextField userInput = new TextField();
        Button share = new Button("Share");
        PopUp sharePopUp = new PopUp("Share Event", getTheme());
        sharePopUp.getContent().addAll(instructions, userInput, share);
        share.setOnAction(e -> {
            String username = userInput.getText();
            Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
            try {
                if (getCalendarManager().shareEvent(username, selectedEvent)) {
                    instructions.setText("Event Shared!\nWaiting on " + username + "'s response");
                    userInput.clear();
                } else { instructions.setText(username + "'s account not found"); }
            } catch (IOException | ClassNotFoundException ex) {
                instructions.setText("An unexpected error has occured");
            }
        });
        sharePopUp.display();
    }

}
