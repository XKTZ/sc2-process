package xktz.sc2.process.builder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/xktz/sc2/process/builder/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setResizable(false);
        primaryStage.setTitle("SC2 Process Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
