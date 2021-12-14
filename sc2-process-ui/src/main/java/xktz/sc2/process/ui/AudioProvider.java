package xktz.sc2.process.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The provider of audio, support getting audio in multithread
 */
public class AudioProvider {

    private Path path;

    private MediaPlayer player;

    private Semaphore lock;

    private AudioProvider(Path path) {
        this.path = path;
        try {
            player = new MediaPlayer(new Media(path.toUri().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        lock = new Semaphore(1);
    }

    public synchronized MediaPlayer acquire() {
        player.seek(player.getStartTime());
        return player;
    }

    public static AudioProvider of(Path path) {
        return new AudioProvider(path);
    }
}
