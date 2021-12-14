package xktz.sc2.process.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AudioPathMapToAudioNameMapSerializer extends JsonSerializer<Map<String, Path>> {

    @Override
    public void serialize(Map<String, Path> stringPathMap,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(toAudioNameMap(stringPathMap));
    }

    public static Map<String, String> toAudioNameMap(Map<String, Path> map) {
        Map<String, String> audioMap = new HashMap<>();
        for (var entry : map.entrySet()) {
            audioMap.put(entry.getKey(), entry.getValue().getFileName().toString());
        }
        return audioMap;
    }
}
