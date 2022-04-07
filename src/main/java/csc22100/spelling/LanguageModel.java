package csc22100.spelling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class LanguageModel {

    private final Map<String, Double> probabilities;

    public Map<String,Double> getProbabilities() {
        return probabilities;
    }

    public Double getProbability(String w) {
        return probabilities.getOrDefault(w, 0.0);
    }

    @JsonCreator
    private LanguageModel(@JsonProperty("probabilities") Map<String, Double> probabilities) {
        this.probabilities = probabilities;
    }

    public void save(Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(path.toFile(), this);
    }

    public static LanguageModel load(Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(path.toFile(), LanguageModel.class);
    }

    public static LanguageModel train(WordFrequencyCounter wordFrequencies) {
        throw new NotImplementedException();
    }
}
