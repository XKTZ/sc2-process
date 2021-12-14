package xktz.sc2.process.builder.audio;

import javax.sound.sampled.*;
import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An audio recorder
 */
public class Recorder {

    private static final float SAMPLE_RATE = 8000.0F;

    private static final int SAMPLE_SIZE = 16;

    private static final int CHANNEL = 2;

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(
            SAMPLE_RATE, SAMPLE_SIZE, CHANNEL, true, true);

    private static final AudioFileFormat.Type AUDIO_TYPE = AudioFileFormat.Type.WAVE;

    private TargetDataLine microphone;

    public Recorder() {
    }

    /**
     * Start recording
     *
     * @param path the path
     */
    public void start(Path path) {
        new Thread(() -> {
            try {
                microphone = AudioSystem.getTargetDataLine(AUDIO_FORMAT);
                microphone.open();

                microphone.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
            }
            AudioInputStream ais = new AudioInputStream(microphone);
            try {
                AudioSystem.write(ais, AUDIO_TYPE, path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        if (microphone == null) return;
        microphone.stop();
        microphone.close();
    }
}
