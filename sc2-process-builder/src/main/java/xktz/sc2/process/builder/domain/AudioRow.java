package xktz.sc2.process.builder.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class AudioRow {

    private final SimpleStringProperty name;

    private final SimpleStringProperty path;

    private Map<String, Path> audioMap;

    private Path root;

    public AudioRow(String key, String location, Map<String, Path> audioMap, Path root) {
        this.root = root;
        this.audioMap = audioMap;
        this.name = new SimpleStringProperty(key){
            @Override
            public void set(String s) {
                audioMap.remove(get());
                super.set(s);
                audioMap.put(s, Path.of(root + File.separator + path.get()));
            }
        };
        this.path = new SimpleStringProperty(location){
            @Override
            public void set(String s) {
                super.set(s);
                audioMap.put(name.get(), Path.of(root + File.separator + s));
            }
        };
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }
}
