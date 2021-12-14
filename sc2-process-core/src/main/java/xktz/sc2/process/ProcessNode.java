package xktz.sc2.process;

public class ProcessNode {

    private long time;

    private String content;

    private int number;

    private String audio;

    public ProcessNode() {

    }

    public ProcessNode(long time, String content, int number, String audio) {
        this.time = time;
        this.content = content;
        this.number = number;
        this.audio = audio;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAudio(String audio) {
        if (audio == null || audio.length() == 0) {
            this.audio = null;
        } else {
            this.audio = audio;
        }
    }

    public String getAudio() {
        return audio;
    }

    public static ProcessNode of(long time, String content, int number, String audio) {
        return new ProcessNode(time, content, number, audio);
    }
}
