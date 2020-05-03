package GUI;

import CalendarSystem.Memo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDateTime;

import CalendarSystem.Event;
import javafx.scene.paint.Paint;
import javafx.util.StringConverter;

public class EventCreatorControl extends Controller {

    private TableView<Event> eventTable;
    @FXML private ComboBox<String> memoOptions;
    @FXML private TextField eventName;
    @FXML private Label eventNameLab;
    @FXML private DatePicker eventStartDate;
    @FXML private TextField eventStartTime;
    @FXML private Label startDateLab;
    @FXML private Label startTimeLab;
    @FXML private Label successMsg;
    @FXML private Label errorMsg;
    @FXML private Button creationToggler;
    private String eventType;

    //Single event specific
    @FXML private DatePicker eventEndDate;
    @FXML private TextField eventEndTime;
    @FXML private TextField eventTag;
    @FXML private Label endDateLab;
    @FXML private Label endTimeLab;
    @FXML private Label eventTagLab;

    //Series Event specific
    @FXML private TextField eventDuration;
    @FXML private TextField eventFrequency;
    @FXML private TextField numEvents;
    @FXML private Label eventDurLabel;
    @FXML private Label eventFreLabel;
    @FXML private Label eventNumLabel;

    @Override
    protected void initScreen() {
        //Setup date pickers to follow specified pattern
        StringConverter<LocalDate> converter = getDateConverter();
        eventStartDate.setConverter(converter);
        eventEndDate.setConverter(converter);
        //Display all this calendar's memos
        for (Memo m : getCalendar().getMyMemos().getMemos()) {
            memoOptions.getItems().add(m.getNote());
        }
        //Initialize to single event creation
        eventType = "SINGLE";
    }

    protected void setTableToModify(TableView<Event> eventTable) {
        this.eventTable = eventTable;
    }

    @FXML private void toggleEventCreationType() {
        reset();
        Node[] singleEventNodes = {eventEndDate, eventEndTime, endDateLab, endTimeLab, eventTagLab, eventTag};
        Node[] seriesEventNodes = {eventDuration, eventFrequency, numEvents, eventDurLabel,
                eventFreLabel, eventNumLabel};
        Node[] nonIndefintieNodes = {eventStartDate, eventStartTime, startTimeLab, startDateLab};

        for (Node item: seriesEventNodes) { item.setVisible(false); }
        for (Node item: singleEventNodes) { item.setVisible(false); }
        for (Node item: nonIndefintieNodes) { item.setVisible(true); }

        if (eventType.equals("MULTIPLE")) {
            eventType = "INDEFINITE";
            for (Node item: nonIndefintieNodes) { item.setVisible(false); }
            eventNameLab.setText("Event Name: *");
            creationToggler.setText("Set as Single Event");
        } else if (eventType.equals("SINGLE")) {
            eventType = "MULTIPLE";
            for (Node item: seriesEventNodes) { item.setVisible(true); }
            eventNameLab.setText("Series Name: *");
            creationToggler.setText("Set as Indefinite Event");
        } else {
            eventType = "SINGLE";
            for (Node item: singleEventNodes) { item.setVisible(true); }
            eventNameLab.setText("Event Name: *");
            creationToggler.setText("Set as Series Event");
        }
    }

    @FXML private void createEvent() {
        resetErrorMessages();
        LocalDateTime start = null;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if (!eventType.equals("INDEFINITE")) {
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
        }
        //Create either single, series or indefinite event
        if (eventName.getText().equals("")) {
            eventNameLab.setTextFill(Paint.valueOf("red"));
            errorMsg.setText("No Name Specified!");
            errorMsg.setVisible(true);
        } else if (eventType.equals("INDEFINITE")) {
            Event newEvent = new Event(eventName.getText());
            addSingleEvent(newEvent);
        }else if (eventType.equals("MULTIPLE") && start != null) {
            createSeriesEvent(start, eventName.getText());
        } else if (start != null){
            createSingleEvent(start, eventName.getText(), dateFormat);
        }
    }

    private void createSeriesEvent(LocalDateTime start, String seriesName) {
        try {
            Long dur = parseNumberInput(eventDuration, eventDurLabel);
            Long fre = parseNumberInput(eventFrequency, eventFreLabel);
            Long num = parseNumberInput(numEvents, eventNumLabel);
            assert num != null;
            assert fre != null;
            getCalendar().addSeries(seriesName, Duration.ofMinutes(dur),
                    Period.ofDays(fre.intValue()), num.intValue(), start);
            ArrayList<Event> newSeries = new ArrayList<>(getCalendar().findEventsBySeries(seriesName));
            eventTable.getItems().addAll(newSeries);
            setEventMemo(newSeries);
            successMsg.setVisible(true);
            reset();
        } catch (NullPointerException n) { errorMsg.setVisible(true);}
    }

    //Helper for createSeriesEvent
    private Long parseNumberInput(TextField numText, Label assocLabel) {
        String userNumber = numText.getText();
        try {
            return Long.parseLong(userNumber);
        } catch (NumberFormatException n) {
            assocLabel.setTextFill(Paint.valueOf("red"));
            errorMsg.setText("Invalid Number Entered!");
            errorMsg.setVisible(true);
            return null;
        }
    }

    private void createSingleEvent(LocalDateTime start, String evntName, DateTimeFormatter dateFormat) {
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
                Event newEvent = new Event(evntName, start, end);
                addSingleEvent(newEvent);
            }
        } catch (DateTimeParseException e) {
            endTimeLab.setTextFill(Paint.valueOf("red"));
            endDateLab.setTextFill(Paint.valueOf("red"));
            errorMsg.setText("Invalid End Date/Time!");
            errorMsg.setVisible(true);
        }
    }

    private void addSingleEvent(Event newEvent) {
        setEventMemo(new ArrayList<>(Collections.singletonList(newEvent)));
        if (!eventTag.getText().equals("")) {
            newEvent.setTag(eventTag.getText());
        }
        eventTable.getItems().add(newEvent);
        getCalendar().addEvent(newEvent);
        reset();
        successMsg.setVisible(true);
    }

    private void setEventMemo(ArrayList<Event> eventsToAdd) {
        String selectedMemo = memoOptions.getValue();
        if (selectedMemo == null) {selectedMemo = "";}
        if (!selectedMemo.equals("") &&
                !memoOptions.getItems().contains(selectedMemo)) {
            getCalendar().createMemo(eventsToAdd, selectedMemo);
            memoOptions.getItems().add(selectedMemo);
        } else if (memoOptions.getItems().contains(selectedMemo)) {
            for (Memo m : getCalendar().getMyMemos().getMemos()) {
                if (m.getNote().equals(selectedMemo)) {
                    for (Event e : eventsToAdd) { e.getMemos().add(m); }
                    break;
                }
            }
        }
    }

    private void resetErrorMessages() {
        successMsg.setVisible(false);
        errorMsg.setText("Invalid Input");
        errorMsg.setVisible(false);
        for (Label errorLabel: new Label[]{eventNameLab, startDateLab, startTimeLab, endDateLab,
                endTimeLab, eventDurLabel, eventFreLabel, eventNumLabel}) {
            if (getTheme().equals("GUI/Dark.css")) {
                errorLabel.setTextFill(Paint.valueOf("white"));
            } else {
                errorLabel.setTextFill(Paint.valueOf("black"));
            }
        }
    }

    private void reset() {
        resetErrorMessages();
        for (TextField field: new TextField[]{eventName, eventStartTime, eventEndTime,
                eventDuration, eventFrequency, numEvents, eventTag}) {
            field.clear();
        }
        eventStartDate.getEditor().clear();
        eventEndDate.getEditor().clear();
        memoOptions.getEditor().setText("");
    }

}
