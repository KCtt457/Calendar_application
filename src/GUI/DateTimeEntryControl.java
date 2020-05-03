package GUI;

import CalendarSystem.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeEntryControl extends Controller {
    private TableView<Event> eventTable;
    private Event event;
    @FXML private DatePicker eventStartDate;
    @FXML private TextField eventStartTime;
    @FXML private Label startDateLab;
    @FXML private Label startTimeLab;
    @FXML private DatePicker eventEndDate;
    @FXML private TextField eventEndTime;
    @FXML private Label endDateLab;
    @FXML private Label endTimeLab;
    @FXML private Label successMsg;
    @FXML private Label errorMsg;


    @Override
    protected void initScreen() {
        //Setup date pickers to follow specified pattern
        StringConverter<LocalDate> converter = getDateConverter();
        eventStartDate.setConverter(converter);
        eventEndDate.setConverter(converter);
    }

    protected void setTableToModify(TableView<Event> eventTable) {
        this.eventTable = eventTable;
    }

    protected void setEventToModify(Event event) {
        this.event = event;
    }

    @FXML private void duplicateEvent() throws IOException {
        resetErrorMessages();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime start = null;

        //Parse start time
        String startTime = eventStartDate.getEditor().getText() + " " + eventStartTime.getText();
        try {
            start = LocalDateTime.parse(startTime, dateFormat);
        } catch (DateTimeParseException e) {
            startTimeLab.setTextFill(Paint.valueOf("red"));
            startDateLab.setTextFill(Paint.valueOf("red"));
            errorMsg.setText("Invalid Start Date/Time!");
            errorMsg.setVisible(true);
        }
        createDuplicatedEvent(start, dateFormat);
    }

    private void createDuplicatedEvent(LocalDateTime start, DateTimeFormatter dateFormat) {
        String endTime = eventEndDate.getEditor().getText() + " " + eventEndTime.getText();
        try {
            LocalDateTime end = LocalDateTime.parse(endTime, dateFormat);
            if (start.isAfter(end)) {
                for (Label l : new Label[]{startDateLab, startTimeLab, endTimeLab, endDateLab}) {
                    l.setTextFill(Paint.valueOf("red"));
                }
                errorMsg.setText("Event ends before it starts!");
                errorMsg.setVisible(true);
            } else {
                Event newEvent = getCalendar().duplicateEvent(event, start, end);
                eventTable.getItems().add(newEvent);
                successMsg.setVisible(true);
                reset();
            }
        } catch (DateTimeParseException e) {
            endTimeLab.setTextFill(Paint.valueOf("red"));
            endDateLab.setTextFill(Paint.valueOf("red"));
            errorMsg.setText("Invalid End Date/Time!");
            errorMsg.setVisible(true);
        }
    }

    private void resetErrorMessages() {
        successMsg.setVisible(false);
        errorMsg.setText("Invalid Input");
        errorMsg.setVisible(false);
        for (Label errorLabel: new Label[]{startDateLab, startTimeLab, endDateLab, endTimeLab}) {
            if (getTheme().equals("GUI/Dark.css")) {
                errorLabel.setTextFill(Paint.valueOf("white"));
            } else {
                errorLabel.setTextFill(Paint.valueOf("black"));
            }
        }
    }

    private void reset() {
        resetErrorMessages();
        for (TextField field: new TextField[]{eventStartTime, eventEndTime}) {
            field.clear();
        }
        eventStartDate.getEditor().clear();
        eventEndDate.getEditor().clear();
    }

}
