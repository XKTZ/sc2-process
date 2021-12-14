package xktz.sc2.process.builder;

import xktz.sc2.process.builder.audio.Recorder;

import javax.sound.sampled.AudioSystem;
import java.nio.file.Path;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Recorder recorder = new Recorder();
        new Scanner(System.in).nextLine();
        recorder.start(Path.of("d://test.wav"));
        new Scanner(System.in).nextLine();
        recorder.stop();
    }
}
