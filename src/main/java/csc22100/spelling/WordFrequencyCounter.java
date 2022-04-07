package csc22100.spelling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class WordFrequencyCounter {

    private static final Tokenizer TOKENIZER = new Tokenizer();

    @JsonIgnore
    private boolean hasCounts;
    private Map<String, Integer> counts;


    public void countFrequencies(List<String> document){
        Preconditions.checkState(!hasCounts, "WordFrequencyCounter has already counted frequencies in a document and cannot be reused");
        // TODO: Implement me.

        counts = null; // TODO: set this to a map of the counts, not null
        hasCounts = true;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }

    public Integer getCount(String s) {
        return counts.getOrDefault(s.toLowerCase(), 0);
    }

    @JsonCreator
    private WordFrequencyCounter(@JsonProperty("counts") Map<String, Integer> counts) {
        this.counts = counts;
        this.hasCounts = true;
    }

    public WordFrequencyCounter() {
        this.counts = null;
        this.hasCounts = false;
    }

    public void save(Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(path.toFile(), this);
    }

    public static WordFrequencyCounter load(Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(path.toFile(), WordFrequencyCounter.class);
    }
}
