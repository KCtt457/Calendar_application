package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import CalendarSystem.CalendarManager;
import javafx.stage.Stage;

public class LoginControl extends Controller {

    @FXML
    private Label errorIn;
    @FXML
    private TextField userInfo;
    @FXML
    private PasswordField passInfo;
    @FXML
    private Button createNewUser;

    @Override
    protected void initScreen() {
        setCalendarManager(null);
        setTheme("GUI/Light.css");
        setSceneTheme(userInfo.getScene());
    }

    @FXML
    private void newUser() throws IOException {
        FXMLLoader signUpLoader = setNewWindowAndGetLoader("UserCreatorScene.fxml",
                (Stage) createNewUser.getScene().getWindow(), 900, 600);
        UserCreatorControl signUpControl = signUpLoader.getController();
        signUpControl.setUsernames(new ArrayList<>(getUsers().keySet()));
    }

    @FXML
    private void login() throws ClassNotFoundException, IOException {
        String user = userInfo.getText();
        String pswd = passInfo.getText();
        HashMap<String, String> users = getUsers();
        if (users.containsKey(user)) {
            if (users.get(user).equals(pswd)) {
                CalendarManager cm = new CalendarManager("./user_data/" + user + "_");
                setCalendarManager(cm);
                cm.saveToFile();
                setScreen("MainMenuScene.fxml", userInfo);
            } else {
                errorIn.setVisible(true);
            }
        } else {
            errorIn.setVisible(true);
        }
    }

    private HashMap<String, String> getUsers() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("users.csv"));
        String[] login;
        HashMap<String, String> users = new HashMap<>();
        while (scanner.hasNextLine()) {
            login = scanner.nextLine().split(",");
            users.put(login[0], login[1]);
        }
        return users;
    }

}
