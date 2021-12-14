package xktz.sc2.process.ui;

import com.sun.jdi.Bootstrap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/xktz/sc2/process/ui/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 600);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setResizable(false);
        primaryStage.setTitle("My SC2 Process Helper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
