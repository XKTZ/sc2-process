package xktz.sc2.process.builder;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.IntegerStringConverter;
import xktz.sc2.process.ProcessConfiguration;
import xktz.sc2.process.ProcessNode;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.UnaryOperator;

/**
 * ProcessCreateDialog
 */
public class ProcessCreateDialog extends Dialog<ProcessNode> {

    private static final UnaryOperator<TextFormatter.Change> INTEGER_FILTER = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    private static final int MINUTE_TO_SECOND_FACTOR = 60;

    private static final int SECOND_TO_MILLISECOND = 1000;

    private static final String PROCESS_CREATE_DIALOG_TITLE = "Create a process";

    private ProcessCreateDialogPane pane;

    private ButtonType okButtonType;

    public ProcessCreateDialog() {
        super();
        setTitle(PROCESS_CREATE_DIALOG_TITLE);

        okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(okButtonType);

        getDialogPane().setContent(pane = new ProcessCreateDialogPane());

        Platform.runLater(pane.txtContent::requestFocus);
        setResultConverter((buttonType -> {
            if (buttonType == okButtonType) {
                var minute = Integer.parseInt(pane.txtMinute.getText());
                var second = Integer.parseInt(pane.txtSecond.getText());
                var content = pane.txtContent.getText();
                var number = Integer.parseInt(pane.txtNumber.getText());
                var audio = pane.txtAudio.getText();
                return new ProcessNode(
                        ((long) minute * MINUTE_TO_SECOND_FACTOR + second)
                                * SECOND_TO_MILLISECOND, content, number, audio
                );
            }
            return null;
        }));
    }

    public static class ProcessCreateDialogPane extends AnchorPane {

        @FXML
        public TextField txtContent;

        @FXML
        public TextField txtNumber;

        @FXML
        public TextField txtMinute;

        @FXML
        public TextField txtSecond;

        @FXML
        public TextField txtAudio;

        public ProcessCreateDialogPane() {
            // load fxml
            var loader = new FXMLLoader(getClass().getResource("/xktz/sc2/process/builder/ProcessCreateDialogPane.fxml"));
            loader.setController(this);
            loader.setRoot(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            txtMinute.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, INTEGER_FILTER));
            txtSecond.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, INTEGER_FILTER));
            txtNumber.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, INTEGER_FILTER));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
