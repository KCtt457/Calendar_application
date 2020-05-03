package GUI;

import CalendarSystem.Event;
import CalendarSystem.Memo;
import CalendarSystem.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;

public class EventViewControl extends Controller {

    @FXML private Label eventNameLab;
    @FXML private Label eventStartLab;
    @FXML private Label eventEndLab;
    @FXML private Label eventTag;
    @FXML private FlowPane associatedSeries;
    @FXML private Button clearMemos;
    @FXML private ListView<Memo> memos;


    protected void displayEvent(Event event) {
        eventNameLab.setText(event.getEventName());
        eventStartLab.setText(event.getStartTime().toString());
        eventEndLab.setText(event.getEndTime().toString());

        if (!event.getTag().equals("")){
            eventTag.setText(event.getTag());
        } else {eventTag.setText("N/A");}

        displaySeries(event);
        displayMemos(event);
        clearMemos.setOnAction(e -> {
            getCalendar().deleteAllMemosforEvent(event);
            displayMemos(event);
        });
    }

    private void displaySeries(Event event) {
        for (Series s : getCalendar().getAssociatedSeries(event)) {
            Label assocSeries = new Label(s.getName());
            assocSeries.getStyleClass().add("calendar-event");
            associatedSeries.getChildren().add(assocSeries);
        }
        if (associatedSeries.getChildren().isEmpty()) {
            associatedSeries.getChildren().add(new Label("N/A"));
        }
    }

    private void displayMemos(Event event) {
        ObservableList<Memo> memoNotes = FXCollections.observableArrayList();
        memoNotes.addAll(event.getMemos());
        memos.setItems(memoNotes);
        memos.setPlaceholder(new Label("No Memos"));
    }
}
