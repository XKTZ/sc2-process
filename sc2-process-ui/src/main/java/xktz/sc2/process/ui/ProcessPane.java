package xktz.sc2.process.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.IOException;

public class ProcessPane extends AnchorPane {

    private static final Border BORDER_HIGHLIGHT = new Border(
            new BorderStroke(Paint.valueOf("000000"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                    new BorderWidths(2))
    );

    private static final Font FONT = new Font(20);

    private static final String TIME_FORMAT = "%02d:%02d";

    @FXML
    public Label lblContent;

    @FXML
    public Label lblNumber;

    @FXML
    public Label lblTime;

    private String content;

    private int number;

    private long time;

    public ProcessPane(String content, int number, long time) {
        // load fxml
        var loader = new FXMLLoader(getClass().getResource("/xktz/sc2/process/ui/ProcessPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.content = content;
        this.number = number;
        this.time = time;

        lblContent.setText(content);
        lblNumber.setText("×" + number);
        lblTime.setText(String.format(TIME_FORMAT, (time / 1000) / 60, (time / 1000) % 60));
    }

    public void highlight() {
        this.setBorder(BORDER_HIGHLIGHT);
    }

    public long getTime() {
        return time;
    }
}
