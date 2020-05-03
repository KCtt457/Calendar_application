package GUI;

import CalendarSystem.Event;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class MainMenuControl extends Controller {

    @FXML
    private Button logOutButton;
    @FXML
    private Button eventButton;
    @FXML
    private Button memoButton;
    @FXML
    private Button alertButton;
    @FXML
    private Slider themeSwitch;
    @FXML
    private Label sysClock;
    @FXML
    private Label monthYearLabel;
    @FXML
    private GridPane monthlyCalendar;
    @FXML
    private ChoiceBox<String> calendarSelect;

    @FXML
    private void logOut() throws IOException {
        getCalendarManager().saveToFile();
        setScreen("LoginScene.fxml", logOutButton);
    }

    @FXML private void viewEvents() throws IOException {
        setScreen("EventMenuScene.fxml", eventButton);
    }

    @FXML
    private void viewMemos() throws IOException {
        setScreen("MemoMenuScene.fxml", memoButton);
    }

    @FXML
    private void viewAlerts() throws IOException {
        setScreen("AlertMenuScene.fxml", alertButton);
    }
    

    @FXML
    private void changeTheme() {
        if (getTheme().equals("GUI/Light.css")) {
            setTheme("GUI/Dark.css");
            themeSwitch.setValue(themeSwitch.getMax());
        } else {
            setTheme("GUI/Light.css");
            themeSwitch.setValue(themeSwitch.getMin());
        }
        setSceneTheme(themeSwitch.getScene());
    }

    @Override
    protected void initScreen() {
        initClock();
        initMonthlyCalendar();
        initCalendarSelect();
        initThemeSwitch();
        try {
            displayInvites();
            getCalendarManager().saveToFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayInvites() {
        ArrayList<Event> viewed = new ArrayList<>();
        for (Event event : getCalendar().getEventInvites()) {
            if (!getCalendar().getMyEvents().contains(event)) {
                Label newEventMsg = new Label("You've received a new event!\n\n" +
                        event.toString() + "\n\nAdd to calendar?");
                newEventMsg.wrapTextProperty().setValue(true);
                Button accept = new Button("Accept");
                Button decline = new Button("Decline");
                HBox addDialog = new HBox();
                addDialog.setSpacing(20.0);
                addDialog.getChildren().addAll(accept, decline);
                PopUp eventInvite = new PopUp("Event Invite", getTheme());
                accept.setOnAction( e -> {
                    getCalendar().addEvent(event);
                    viewed.add(event);
                    eventInvite.exit();
                });
                decline.setOnAction(e -> {
                    viewed.add(event);
                    eventInvite.exit();
                });
                eventInvite.getContent().addAll(newEventMsg, addDialog);
                eventInvite.display();
            }
        }
        getCalendar().getEventInvites().removeAll(viewed);
    }

    private void initThemeSwitch() {
        if (getTheme().equals("GUI/Dark.css")) {
            themeSwitch.setValue(themeSwitch.getMax());
        } else {
            themeSwitch.setValue(themeSwitch.getMin());
        }
    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            sysClock.setText("Time: \n" + currentTime.getHour() + ":" +
                    currentTime.getMinute() + ":" + currentTime.getSecond());
            monthYearLabel.setText("\t" + currentTime.getMonth().toString() + " " + currentTime.getDayOfMonth()
                    + ", " + currentTime.getYear() + "\t\t");
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void initMonthlyCalendar() {
        int currMonth = LocalDateTime.now().getMonthValue();
        int currYear = LocalDateTime.now().getYear();
        int firstDayOfWeek = LocalDate.of(currYear, currMonth, 1).getDayOfWeek().getValue() % 7;
        LocalDate calCurrDay = LocalDate.of(currYear, currMonth, 1).minusDays(firstDayOfWeek);
        for (int w = 1; w < 7; w++) {
            for (int d = 0; d < 7; d++) {
                VBox day = initMonthlyDayBox(d, w, calCurrDay);
                addEventLabels(day, getCalendar().findEvent(calCurrDay));
                monthlyCalendar.getChildren().add(day);
                calCurrDay = calCurrDay.plusDays(1L);
            }
        }
    }

    private VBox initMonthlyDayBox(int dayOfWeek, int weekNum, LocalDate currDay) {
        VBox day = new VBox();
        day.getStyleClass().add("calendar-grid");
        day.setSpacing(3.0);
        day.setPrefHeight(84.0);
        day.setPrefWidth(100.0);
        GridPane.setConstraints(day, dayOfWeek, weekNum);
        Label dayOfMonth = new Label(" " + Integer.toString(currDay.getDayOfMonth()) + " ");
        if (currDay.equals(LocalDate.now())) {dayOfMonth.setStyle("-fx-background-color: #5FB1FE");}
        day.getChildren().add(dayOfMonth);
        return day;
    }

    private void addEventLabels(VBox dayBox, ArrayList<Event> events) {
        for (Event e : events) {
            Label eventLabel = new Label(e.getEventName());
            eventLabel.getStyleClass().add("calendar-event");
            dayBox.getChildren().add(eventLabel);
        }
    }

    private void initCalendarSelect() {
        calendarSelect.getItems().addAll(getCalendarManager().getUserCalendars());
        calendarSelect.setValue(getCalendarManager().getUserCalendars().get(0));
        calendarSelect.getSelectionModel().selectedItemProperty().addListener(
                (v, oldVal, newVal) -> {
                    try {
                        getCalendarManager().selectCalendar(newVal);
                        initMonthlyCalendar();
                    } catch (ClassNotFoundException |  IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @FXML private void createCalendar() {
        Label invalidNameLabel = new Label("Invalid Calendar Name!");
        invalidNameLabel.getStyleClass().add("label-error");
        invalidNameLabel.setVisible(false);
        Label instructions = new Label("New Calendar Name:");
        Button createCalendarButton = new Button("Create Calendar");
        TextField userInput = new TextField();
        PopUp newCalNameInpt = new PopUp("Create New Calendar", getTheme());
        newCalNameInpt.getContent().addAll(invalidNameLabel, instructions, userInput, createCalendarButton);
        createCalendarButton.setOnAction(e -> {
            if (isValidName(userInput.getText(), invalidNameLabel)) {
                try {
                    getCalendarManager().createCalendar(userInput.getText());
                    calendarSelect.getItems().add(userInput.getText());
                    calendarSelect.setValue(userInput.getText());
                    newCalNameInpt.exit();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        });
        newCalNameInpt.display();
    }

    /*
     * Helper for createCalendar()
     */
    private boolean isValidName(String input, Label errorLabel) {
        if (input == null) {input = "";}
        if (input.equals("") || input.contains(".") || input.contains(" ")) {
            errorLabel.setText("Invalid Calendar Name!");
            errorLabel.setVisible(true);
            return false;
        } else if (getCalendarManager().getUserCalendars().contains(input)){
            errorLabel.setText("This Calendar Already Exists!");
            errorLabel.setVisible(true);
            return false;
        } else {
            return true;
        }
    }

}
