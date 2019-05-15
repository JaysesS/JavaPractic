package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    private Parent rootLayout;

    private  Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        initRootLayout();
    }

    private void initRootLayout() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("form.fxml"));

            rootLayout = loader.load();

            scene = new Scene(rootLayout);

            Controller controllerTestForm = loader.getController();

            primaryStage.setScene(scene);

            controllerTestForm.Init();

            primaryStage.setTitle("Second lab DM");

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
