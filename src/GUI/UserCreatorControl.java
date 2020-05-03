package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;

public class UserCreatorControl extends Controller {

    private ArrayList<String> users = new ArrayList<>();
    @FXML
    private Label errorIn;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private PasswordField passwordAgainInput;
    @FXML
    private Label success;
    @FXML
    private Button goToLogin;

    protected void setUsernames(ArrayList<String> users) {this.users = users;}

    @FXML
    private void goBackToLogin() throws IOException {
        setScreen("LoginScene.fxml", goToLogin);
    }

    @FXML
    private void createNewUser() throws IOException {

        String user = usernameInput.getText();
        String password = passwordInput.getText();
        String password2 = passwordAgainInput.getText();

        if (isValidUser(user) && !password.equals("") && password.equals(password2)) {

            FileWriter writer = new FileWriter("users.csv", true);
            writer.append(user);
            writer.append(",");
            writer.append(password);
            writer.append("\n");
            writer.flush();
            writer.close();
            errorIn.setVisible(false);
            success.setVisible(true);

            usernameInput.clear();
            passwordInput.clear();
            passwordAgainInput.clear();

        } else {
            errorIn.setVisible(true);
        }
    }

    private boolean isValidUser(String username) {
        if (users.contains(username)) {
            errorIn.setText("This username already exists!");
            return false;
        } else if (username.equals("") || username.contains("_")){
            errorIn.setText("Not a valid username!");
            return false;
        } else {
            errorIn.setText("Invalid Input. Please try again!");
            return true;
        }
    }
}
