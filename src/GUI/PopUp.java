package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;

public class PopUp {

    private ObservableList<Node> content;
    private String title;
    private double spacing;
    private Stage pop;
    private int tPad;
    private int bPad;
    private int rPad;
    private int lPad;
    private String theme;

    public PopUp(String title, String theme) {
        this.content = FXCollections.observableList(new ArrayList<>());
        this.title = title;
        this.spacing = 9.0;
        this.tPad = 30;
        this.bPad = 50;
        this.lPad = 10;
        this.rPad = 20;
        this.theme = theme;
        this.pop = new Stage();
    }

    public PopUp(String title, String theme, double spacing, int tPad, int bPad, int lPad, int rPad) {
        this.content = FXCollections.observableList(new ArrayList<>());
        this.title = title;
        this.spacing = spacing;
        this.tPad = tPad;
        this.bPad = bPad;
        this.lPad = lPad;
        this.rPad = rPad;
        this.theme = theme;
        this.pop = new Stage();
    }

    protected ObservableList<Node> getContent() {
        return content;
    }

    protected void display() {
        //Make New pop up window
        pop.setTitle(title);
        pop.initModality(Modality.APPLICATION_MODAL);
        pop.setResizable(false);

        //Create new scene to display
        VBox newScene = new VBox();
        newScene.setSpacing(spacing);
        newScene.setPadding(new Insets(tPad, bPad, rPad, lPad));
        newScene.getChildren().addAll(content);

        //Set and display scene to new stage
        Scene popUpScene = new Scene(newScene, 300, 200);
        popUpScene.getStylesheets().add(theme);

        pop.setScene(popUpScene);
        pop.showAndWait();
    }

    protected void exit() {
        pop.close();
    }
}
