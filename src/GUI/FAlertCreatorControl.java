package GUI;

import CalendarSystem.Calendar;
import CalendarSystem.Event;
import CalendarSystem.FrequentAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;

public class FAlertCreatorControl extends Controller {
    private boolean edit_Mode = false;
    private FrequentAlert alert = null;
    private boolean edited = false;
    @FXML private Label eventNameLabel;
    @FXML private TextField eventName;
    @FXML private Label frequencyLabel;
    @FXML private Label everyLabel;
    @FXML private TextField frequency;
    @FXML private ChoiceBox<String> unit;
    @FXML private Label messageLabel;
    @FXML private TextField message;
    @FXML private Label eventErrorMsg;
    @FXML private Label frequencyErrorMsg;
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
     * Initialize the creator control scene with edit mode (default pre-defined values for the alert)
     */
    @FXML private void initEditMode(){
        if (edit_Mode){
            eventName.setText(alert.getName());
            eventName.setEditable(false);

            String frequencyFormat = alert.durationToString(alert.getFrequency());
            frequency.setText(frequencyFormat.substring(0, frequencyFormat.length()-1));
            if(frequencyFormat.charAt(frequencyFormat.length()-1) == 'D')
                unit.setValue("day(s)");
            else
                unit.setValue("hour(s)");
            message.setText(alert.getMessage());
        }
    }


    /**
     * Process and submit the alerts
     */
    @FXML private void submit() throws IOException {
        if (!hasformaterrors()) {
            //process all the user inputs
            Calendar calendar = getCalendar();
            String eventNameVal = eventName.getText();
            Event event = calendar.getEvent(eventNameVal);
            String messageVal = message.getText();
            Duration durationVal = processDurationInfo();

            //check if a frequent alert can be created
            if(!FrequentAlert.isValidFrequentAlert(event.getStartTime(), calendar.getLocalDateTime(), durationVal)){
                frequencyLabel.setTextFill(Paint.valueOf("red"));
                frequencyErrorMsg.setVisible(true);
            } else {
                //valid alert inputs!!!
                if (edit_Mode) {
                    calendar.getMyAlerts().editFrequentAlert(alert, messageVal, durationVal);
                    edited = true;
                }
                else
                    calendar.addFrequentAlert(calendar.getEvent(eventNameVal), messageVal, durationVal);

                getCalendarManager().saveToFile();

                // close the window
                Stage stage = (Stage) submit.getScene().getWindow();
                stage.close();
            }
        }
    }


//helper methods
    /**
     * Check if the user inputs has errors meeting the format, returns true if error exists
     * @return true if there are errors, false otherwise
     */
    private boolean hasformaterrors(){
        Calendar calendar = getCalendar();
        resetError();
        boolean result = false;
        if(!calendar.getEventNames().contains(eventName.getText())){
            eventNameLabel.setTextFill(Paint.valueOf("red"));
            eventErrorMsg.setVisible(true);
            result = true;
        } if (message.getText().equals("")) {
            messageLabel.setTextFill(Paint.valueOf("red"));
            result = true;
        } try {
            Integer.parseInt(frequency.getText());
        } catch (NumberFormatException e){
            frequencyLabel.setTextFill(Paint.valueOf("red"));
            result = true;
        }

        return result;

    }


    /**
     * process user text input into duration
     * @return duration
     */
    private Duration processDurationInfo() {
        int frequencyVal = Integer.parseInt(frequency.getText());
        String unitVal = unit.getValue();
        Duration durationVal;
        if (unitVal.equals("day(s)"))
            durationVal = Duration.ofDays(frequencyVal);
        else
            durationVal = Duration.ofHours(frequencyVal);
        return durationVal;
    }

    /**
     * set to edit_mode for alert and initialize the scene with different controls
     * @param edit_Mode mode to set to
     */
    public void setEdit_Mode(boolean edit_Mode) {
        this.edit_Mode = edit_Mode;
        initEditMode();
    }

    /**
     * set alert to edit
     * @param alert to edit
     */
    public void setAlert(FrequentAlert alert) {
        this.alert = alert;
    }

    /**
     * return if alert is edited
     * @return true if edited, false otherwise
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * reset all the error messages
     */
    private void resetError(){
        String style = eventNameLabel.getStyle();
        if (style.equals("Light.css")){
            eventNameLabel.setTextFill(Paint.valueOf("black"));
            frequencyLabel.setTextFill(Paint.valueOf("black"));
            messageLabel.setTextFill(Paint.valueOf("black"));
        } else{
            eventNameLabel.setTextFill(Paint.valueOf("white"));
            frequencyLabel.setTextFill(Paint.valueOf("white"));
            messageLabel.setTextFill(Paint.valueOf("white"));
        }
        eventErrorMsg.setVisible(false);
        frequencyErrorMsg.setVisible(false);
    }


}
