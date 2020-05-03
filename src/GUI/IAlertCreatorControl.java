package GUI;

import CalendarSystem.Calendar;
import CalendarSystem.IndividualAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IAlertCreatorControl extends Controller {
    private boolean edit_Mode = false;
    private IndividualAlert alert = null;
    private boolean edited = false;
    @FXML private Label eventNameLabel;
    @FXML private TextField eventName;
    @FXML private Label dateLabel;
    @FXML private DatePicker date;
    @FXML private TextField time;
    @FXML private Label messageLabel;
    @FXML private TextField message;
    @FXML private Label eventErrorMsg;
    @FXML private Label dateErrorMsg;
    @FXML private Button submit;


    /**
     * initialize screen, setting error messages invisible
     */
    @Override
    protected void initScreen() {
        super.initScreen();
        resetError();
    }


    /**
     * initialize fields with the pre-defined values for edit_mode
     */
    @FXML private void initEditMode(){
        if(edit_Mode){
            eventName.setText(alert.getName());
            eventName.setEditable(false);

            LocalDateTime dateTime = alert.getTime();
            LocalDate dateVal = dateTime.toLocalDate();
            date.setValue(dateVal);
            time.setText(dateTime.toLocalTime().toString());

            message.setText(alert.getMessage());
        }
    }


    /**
     * create the new alert with the user input
     */
    @FXML private void submit() {
        if (!hasFormatErrors()) {
            //process user inputs
            Calendar calendar = getCalendar();
            String eventNameVal = eventName.getText();
            String messageVal = message.getText();
            date.setConverter(getDateConverter());
            String dateVal = (date.getValue()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String timeVal = time.getText();
            LocalDateTime dateTimeVal = LocalDateTime.parse(dateVal + " " + timeVal, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            //create/edit the individual alert
            if (edit_Mode) {
                calendar.getMyAlerts().editIndividualAlert(alert, messageVal, dateTimeVal);
                edited = true;
            } else
                calendar.addIndividualAlert(calendar.getEvent(eventNameVal), messageVal, dateTimeVal);

            try{
                getCalendarManager().saveToFile();

                // close the window
                Stage stage = (Stage) submit.getScene().getWindow();
                stage.close();

            } catch (IOException e) {
                System.out.println("Failed to save Individual Alert to File");
            }
        }
    }

    /**
     * check if user input has format errors, set text to red if related text has error
     * @return true if there is error
     */
    private boolean hasFormatErrors() {
        Calendar calendar = getCalendar();
        resetError();
        boolean result = false;
        if (!calendar.getEventNames().contains(eventName.getText())) {
            eventNameLabel.setTextFill(Paint.valueOf("red"));
            eventErrorMsg.setVisible(true);
            result = true;
        }
        if (message.getText().equals("")) {
            messageLabel.setTextFill(Paint.valueOf("red"));
            result = true;
        }
        if (date.getValue() == null || date.getValue().toString().equals("")) {
            dateLabel.setTextFill(Paint.valueOf("red"));
            dateErrorMsg.setVisible(true);
            result = true;
        }
        if (!isValidTime(time.getText())) {
            dateLabel.setTextFill(Paint.valueOf("red"));
            dateErrorMsg.setVisible(true);
            result = true;
        }
        return result;
    }

    /**
     * helper method to determine if time is inputted correctly
     * @param time user input for time
     * @return if input matches format
     */
    private boolean isValidTime(String time) {
        Pattern timePattern = Pattern.compile("^([01][0-9]|2[0-3]):[0-5][0-9]$");
        Matcher matchTime = timePattern.matcher(time);
        return matchTime.matches();
    }

    /**
     * set the creator scene to edit mode and initialize
     * @param edit_Mode true if set to edit mode
     */
    public void setEdit_Mode(boolean edit_Mode) {
        this.edit_Mode = edit_Mode;
        initEditMode();
    }

    /**
     * set alert to edit
     * @param alert to edit
     */
    public void setAlert(IndividualAlert alert) {
        this.alert = alert;
    }

    /**
     * returns if the alert has been edited
     * @return true if edited
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * reset all error messages
     */
    private void resetError(){
        String style = eventNameLabel.getStyle();
        if (style.equals("Light.css")){
            eventNameLabel.setTextFill(Paint.valueOf("black"));
            dateLabel.setTextFill(Paint.valueOf("black"));
            messageLabel.setTextFill(Paint.valueOf("black"));
        } else{
            eventNameLabel.setTextFill(Paint.valueOf("white"));
            dateLabel.setTextFill(Paint.valueOf("white"));
            messageLabel.setTextFill(Paint.valueOf("white"));
        }
        eventErrorMsg.setVisible(false);
        dateErrorMsg.setVisible(false);
    }
}


