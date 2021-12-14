package xktz.sc2.process.builder.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import xktz.sc2.process.ProcessNode;

import java.util.Comparator;

public class ProcessRow implements Comparable<ProcessRow> {

    private static final int SECOND_TO_MILLISECOND = 1000;

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty content;
    private final SimpleIntegerProperty number;
    private final SimpleLongProperty time;
    private final SimpleStringProperty audio;

    private final ProcessNode bind;

    public ProcessRow(ProcessNode bind, int idVal) {
        this.bind = bind;

        id = new SimpleIntegerProperty(idVal);
        content = new SimpleStringProperty(""){
            @Override
            public void set(String s) {
                super.set(s);
                bind.setContent(s);
            }
        };
        number = new SimpleIntegerProperty(0){
            @Override
            public void set(int i) {
                super.set(i);
                bind.setNumber(i);
            }
        };
        time = new SimpleLongProperty(0){
            @Override
            public void set(long l) {
                super.set(l);
                bind.setTime(l * SECOND_TO_MILLISECOND);
            }

            @Override
            public long get() {
                return super.get();
            }
        };
        audio = new SimpleStringProperty(""){
            @Override
            public void set(String s) {
                super.set(s);
                bind.setAudio(s);
            }
        };

        setContent(bind.getContent());
        setNumber(bind.getNumber());
        setTime(bind.getTime() / SECOND_TO_MILLISECOND);
        setAudio(bind.getAudio());
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getContent() {
        return content.get();
    }

    public SimpleStringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public long getTime() {
        return time.get();
    }

    public SimpleLongProperty timeProperty() {
        return time;
    }

    public void setTime(long time) {
        this.time.set(time);
    }

    public String getAudio() {
        return audio.get();
    }

    public SimpleStringProperty audioProperty() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio.set(audio);
    }

    public ProcessNode getBind() {
        return bind;
    }

    @Override
    public int compareTo(ProcessRow o) {
        return Long.compare(getTime(), o.getTime());
    }
}
