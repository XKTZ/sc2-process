package xktz.sc2.process.ui;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import xktz.sc2.process.*;
import xktz.sc2.process.Process;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MainController {

    private static final long HIGHLIGHT_DELAY = 2500;

    private static final long ENDING_DELAY = 3000;

    @FXML
    public AnchorPane mainContainer;

    @FXML
    public Button btnChoose;

    @FXML
    public Button btnStart;

    @FXML
    public FlowPane processPaneContainer;

    @FXML
    public TextField txtDelay;

    private ProcessConfiguration config;

    private List<ProcessPane> processPanes;

    private Map<ProcessNode, ProcessPane> processPaneMap;

    private Map<String, AudioProvider> audioMap;

    private Timer removeTimer;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    public void chooseConfig(ActionEvent actionEvent) {
        directoryChooser.setTitle("Choose your configuration directory");
        try {
            var path = directoryChooser.showDialog(mainContainer.getScene().getWindow());
            if (path != null) {
                config = ProcessConfiguration.of(Path.of(path.getCanonicalPath()));
                btnStart.setDisable(false);
                resetProcess();
            }
        } catch (IOException e) {
            e.printStackTrace();
            config = null;
        }
    }

    @FXML
    public void start(ActionEvent actionEvent) {
        if (config == null) {
            return;
        }

        btnStart.setDisable(true);

        int delay = 0;

        if (StringUtils.isNumeric(txtDelay.getText())) {
            delay = Integer.parseInt(txtDelay.getText()) * 1000;
        }

        Process process = new ParallelProcess(config.getName(), config.getProcess(), ENDING_DELAY, () -> {
            btnStart.setDisable(false);
            resetProcess();
        }) {
            Emitter<ProcessNode> emitter = (node) -> {
                playAudio(node.getAudio());
                var pane = processPaneMap.get(node);
                Platform.runLater(pane::highlight);
                removeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            processPaneContainer.getChildren().remove(pane);
                            processPanes.remove(pane);
                            processPaneMap.remove(node);
                        });
                    }
                }, HIGHLIGHT_DELAY);
            };

            @Override
            public Emitter<ProcessNode> getEmitter(ProcessNode node) {
                return emitter;
            }
        };
        process.start(delay);
    }

    private void resetProcess() {

        // set the title
        var stage = ((Stage) (mainContainer.getScene().getWindow()));
        stage.setTitle(config.getName());

        // set the process panes & maps
        processPanes = Collections.synchronizedList(new ArrayList<>());
        processPaneMap = new ConcurrentHashMap<>();

        // remover timer
        removeTimer = new Timer();

        // add all process panes into the container
        for (var node : config.getProcess()) {
            var processPane = new ProcessPane(node.getContent(), node.getNumber(), node.getTime());
            processPaneMap.put(node, processPane);
            processPanes.add(processPane);
        }

        // sort the process panes
        processPanes.sort(Comparator.comparingLong(ProcessPane::getTime));

        // add the panes into the container
        Platform.runLater(() -> {
            processPaneContainer.getChildren().clear();
            processPaneContainer.getChildren().addAll(processPanes);
        });

        // add the audio mapa
        audioMap = new HashMap<>() {{
            var audioPathMap = config.getAudioMap();
            for (var entry : audioPathMap.entrySet()) {
                put(entry.getKey(), AudioProvider.of(entry.getValue()));
            }
        }};

        Platform.runLater(() -> {
            processPaneContainer.setPrefHeight(70 * config.getProcess().size());
        });
    }

    public void playAudio(String audio) {
        new Thread(() -> {
            System.out.println(audio);
            audioMap.get(audio).acquire().play();
        }).start();
    }
}
