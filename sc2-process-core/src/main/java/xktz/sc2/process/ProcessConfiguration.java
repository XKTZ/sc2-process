package xktz.sc2.process;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xktz.sc2.process.json.AudioPathMapToAudioNameMapSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The configuration of the process
 */
@JsonAutoDetect(
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        creatorVisibility = JsonAutoDetect.Visibility.ANY
)
public class ProcessConfiguration {

    public static final String NAME_PROPERTY = "name";

    public static final String AUDIO_MAP_PROPERTY = "audio";

    public static final String PROCESS_NODE_PROPERTY = "process";

    public static final String FILE_NAME_SETTING = "setting.json";

    public static final String EMPTY_SETTING_FORMAT = "{\n" +
            "  \"name\": \"%s\",\n" +
            "  \"process\": [],\n" +
            "  \"audio\": {}\n" +
            "}";

    @JsonIgnore
    private String root;

    @JsonProperty(NAME_PROPERTY)
    private String name;

    @JsonProperty(PROCESS_NODE_PROPERTY)
    private List<ProcessNode> nodes;

    @JsonProperty(AUDIO_MAP_PROPERTY)
    @JsonSerialize(using = AudioPathMapToAudioNameMapSerializer.class)
    private Map<String, Path> audioMap;

    @JsonIgnore
    private Map<String, String> audioNameMap;

    public ProcessConfiguration() {
    }

    @JsonCreator
    public ProcessConfiguration(@JsonProperty(NAME_PROPERTY) String name,
                                @JsonProperty(PROCESS_NODE_PROPERTY) List<ProcessNode> process,
                                @JsonProperty(AUDIO_MAP_PROPERTY) Map<String, String> audioNameMap) {
        setName(name);
        setProcess(process);
        setAudioMap(audioNameMap);
    }

    /**
     * Initialize
     */
    public ProcessConfiguration init() {
        audioMap = new HashMap<>();
        for (var entry: audioNameMap.entrySet()) {
            var path = Path.of(root + File.separator + entry.getValue());
            if (path.toFile().exists()) {
                audioMap.put(entry.getKey(), path);
            } else {
                audioMap.put(entry.getKey(), null);
            }
        }
        return this;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public List<ProcessNode> getProcess() {
        return nodes;
    }

    private void setProcess(List<ProcessNode> nodes) {
        this.nodes = nodes;
    }

    private void setAudioMap(Map<String, String> audioMap) {
        this.audioNameMap = audioMap;
    }

    public Map<String, Path> getAudioMap() {
        return audioMap;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public String toString() {
        try {
            ObjectWriter writer = new ObjectMapper()
                    .setDefaultPrettyPrinter(new DefaultPrettyPrinter()).writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get a configuration form directory
     *
     * @param pathDir path of directory
     * @return the configuration
     * @throws IOException
     */
    public static ProcessConfiguration of(Path pathDir) throws IOException {
        if (!pathDir.toFile().isDirectory()) {
            throw new NotDirectoryException(pathDir.toString());
        }
        ProcessConfiguration configuration = new ObjectMapper().readValue(Files.newInputStream(
                        Path.of(pathDir.toFile().getAbsolutePath() + File.separator + FILE_NAME_SETTING)
                ),
                ProcessConfiguration.class);
        configuration.root = pathDir.toAbsolutePath().toString();
        configuration.init();
        return configuration;
    }
}
