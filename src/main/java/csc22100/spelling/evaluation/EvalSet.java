package csc22100.spelling.evaluation;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import csc22100.spelling.Utils;
import csc22100.spelling.WordFrequencyCounter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class EvalSet {
    private static final Random RANDOM = new Random(42L);

    private final Set<EvalPair> pairs;

    @JsonCreator
    private EvalSet(@JsonProperty("pairs") Set<EvalPair> pairs) {
        this.pairs = pairs;
    }

    public Set<EvalPair> getPairs() {
        return pairs;
    }

    public static class EvalPair {
        String misspelling;
        String corrected;

        @JsonCreator
        EvalPair(@JsonProperty("misspelling") String misspelling,
                 @JsonProperty("corrected") String corrected) {
            this.misspelling = misspelling;
            this.corrected = corrected;
        }

        public String getCorrected() {
            return corrected;
        }

        public String getMisspelling() {
            return misspelling;
        }
    }

    public static EvalSet load(Path path) throws IOException {
        return Utils.OBJECT_MAPPER.readValue(path.toFile(), EvalSet.class);
    }


    public static void prepare(Path inputPath, Path outputPath, WordFrequencyCounter frequencyCounter) throws IOException {
        List<String> linesFromFile = Utils.getLinesFromFile(inputPath);

        // TODO: Build an `EvalSet` based on the lines in the file, omitting any lines where the correct spelling is not in our corpus
        // (i.e. frequencyCounter.getCount returns 0)

        EvalSet evalSet = null;
        Utils.writeJson(outputPath.resolve("data.json"), evalSet);
    }
}
