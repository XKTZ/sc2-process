package xktz.sc2.process.builder;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import xktz.sc2.process.builder.audio.Recorder;

import java.io.IOException;
import java.nio.file.Path;

public class AudioRecordDialog extends Dialog<Void> {

    Recorder recorder;

    public AudioRecordDialog(Path path) {
        super();
        recorder = new Recorder();

        getDialogPane().getButtonTypes().add(ButtonType.OK);
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, (e) -> {
            recorder.stop();
        });
        getDialogPane().setContent(new AudioRecordDialogPane(recorder, path));

        setResultConverter((buttonType) -> null);
    }

    public class AudioRecordDialogPane extends AnchorPane {

        @FXML
        public Button btnStart;

        /**
         * Path of the file
         */
        private Path path;

        private Recorder recorder;

        public AudioRecordDialogPane(Recorder recorder, Path path) {
            var loader = new FXMLLoader(getClass().getResource("/xktz/sc2/process/builder/AudioRecordDialogPane.fxml"));
            loader.setController(this);
            loader.setRoot(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            this.path = path;
            this.recorder = recorder;
        }

        @FXML
        public void startRecord(ActionEvent actionEvent) {
            recorder.start(path);
            btnStart.setDisable(true);
        }
    }
}
