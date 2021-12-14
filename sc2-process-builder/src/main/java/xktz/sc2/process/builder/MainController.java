package xktz.sc2.process.builder;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import xktz.sc2.process.ProcessConfiguration;
import xktz.sc2.process.ProcessNode;
import xktz.sc2.process.builder.domain.AudioRow;
import xktz.sc2.process.builder.domain.ProcessRow;
import xktz.sc2.process.json.AudioPathMapToAudioNameMapSerializer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static xktz.sc2.process.ProcessConfiguration.*;


public class MainController implements Initializable {

    private static final String WAVE_FILE_EXTENSION = ".wav";

    private String name;

    private Map<String, Path> audioMap;

    private List<ProcessNode> processes;

    private Path root;

    @FXML
    public TableView<ProcessRow> tblProcess;

    @FXML
    public TableView<AudioRow> tblAudio;

    @FXML
    public Button btnNewProcess;

    @FXML
    public Button btnNewAudio;

    @FXML
    public Button btnRemoveProcess;

    @FXML
    public Button btnRefresh;

    @FXML
    public MenuItem menuNew;

    @FXML
    public MenuItem menuOpen;

    @FXML
    public MenuItem menuClose;

    @FXML
    public TableColumn<ProcessRow, String> colProcessContent;

    @FXML
    public TableColumn<ProcessRow, Integer> colProcessNumber;

    @FXML
    public TableColumn<ProcessRow, Long> colProcessTime;

    @FXML
    public TableColumn<ProcessRow, String> colProcessAudio;

    // the id process is on
    private int idProcessOn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProcessNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colProcessTime.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    }

    @FXML
    public void newProject(ActionEvent actionEvent) throws IOException {
        var nameOptional = new TextInputDialog("Input process name").showAndWait();
        if (nameOptional.isEmpty()) {
            return;
        }
        var name = nameOptional.get();
        var path = Path.of(new DirectoryChooser().showDialog(null).getAbsolutePath());
        // clear the directory
        if (path.toFile().exists() && path.toFile().isDirectory()) {
            FileUtils.deleteDirectory(path.toFile());
        }
        FileUtils.forceMkdir(path.toFile());
        // create the setting
        Files.writeString(Path.of(path.toAbsolutePath() + File.separator + ProcessConfiguration.FILE_NAME_SETTING),
                String.format(EMPTY_SETTING_FORMAT, name));

        open(path);
    }

    @FXML
    public void openProject(ActionEvent actionEvent) throws IOException {
        var path = Path.of(new DirectoryChooser().showDialog(null).getAbsolutePath());
        if (!path.toFile().exists()) {
            System.out.println("Directory not exists: " + path.toAbsolutePath());
            return;
        }
        open(path);
    }

    @FXML
    public void saveProject(ActionEvent actionEvent) throws IOException {
        save();
    }

    private void open(Path path) throws IOException {
        idProcessOn = 0;
        tblProcess.getItems().clear();
        tblAudio.getItems().clear();

        root = path;
        var conf = ProcessConfiguration.of(path);
        audioMap = conf.getAudioMap();
        processes = conf.getProcess();
        name = conf.getName();

        for (var process : processes) {
            tblProcess.getItems().add(new ProcessRow(process, idProcessOn));
            idProcessOn++;
        }

        for (var audio : audioMap.entrySet()) {
            tblAudio.getItems().add(new AudioRow(audio.getKey(),
                    audio.getValue().getFileName().toString(), audioMap, root));
        }
    }

    private void save() throws IOException {
        var configNew = new ProcessConfiguration(name, processes,
                AudioPathMapToAudioNameMapSerializer.toAudioNameMap(audioMap)) {{
            setRoot(root.toFile().getCanonicalPath());
            init();
        }};
        Files.writeString(
                Path.of(root.toAbsolutePath() + File.separator + FILE_NAME_SETTING),
                configNew.toString(),
                StandardCharsets.UTF_8
        );
    }

    @FXML
    public void newProcess(ActionEvent actionEvent) throws IOException {
        var processOptional = new ProcessCreateDialog().showAndWait();
        if (processOptional.isEmpty()) {
            return;
        }
        var process = processOptional.get();
        addProcess(process);
    }

    @FXML
    public void removeProcess(ActionEvent actionEvent) {
        var processRemoveOptional = new TextInputDialog() {{
            setContentText("Input process id you want to remove");
        }}.showAndWait();
        if (processRemoveOptional.isEmpty() || !StringUtils.isNumeric(processRemoveOptional.get())) {
            return;
        }
        int start = Integer.parseInt(processRemoveOptional.get());
        // find the process
        removeProcessById(start);
    }

    @FXML
    public void newAudio(ActionEvent actionEvent) {
        var idOptional = new TextInputDialog("Input audio name").showAndWait();
        if (idOptional.isEmpty()) {
            return;
        }
        var nameOptional = new TextInputDialog("Input your WAV file name").showAndWait();
        if (nameOptional.isEmpty()) {
            return;
        }
        var name = nameOptional.get();
        var path = Path.of(root + File.separator + name + WAVE_FILE_EXTENSION);
        new AudioRecordDialog(path).showAndWait();
        addAudio(name, path);
    }

    @FXML
    public void refresh(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            tblProcess.getItems().sort(Comparator.comparingLong(ProcessRow::getTime));
        });
    }

    private synchronized void addProcess(ProcessNode node) {
        // sort the process
        processes.add(node);
        processes.sort(Comparator.comparingLong(ProcessNode::getTime));
        // add into table, sor the table
        ProcessRow row = new ProcessRow(node, idProcessOn++);
        Platform.runLater(() -> {
            tblProcess.getItems().add(row);
            tblProcess.getItems().sort(Comparator.comparingLong(ProcessRow::getTime));
        });
    }

    private synchronized void removeProcessById(int id) {
        var rowOptional = tblProcess.getItems().stream().filter(row -> row.getId() == id).findFirst();
        if (rowOptional.isEmpty()) {
            return;
        }
        var row = rowOptional.get();
        Platform.runLater(() -> {
            tblProcess.getItems().remove(row);
        });
        processes.remove(row.getBind());
        processes.sort(Comparator.comparingLong(ProcessNode::getTime));
    }

    private synchronized void addAudio(String name, Path path) {
        audioMap.put(name, path);
        var row = new AudioRow(name, path.getFileName().toString(), audioMap, root);
        Platform.runLater(() -> {
            tblAudio.getItems().add(row);
        });
    }
}

