package csc22100.spelling;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SpellChecker {
    LanguageModel languageModel;

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellChecker.class);
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    public SpellChecker(LanguageModel languageModel) {
        this.languageModel = languageModel;
    }

    /**
     * Generate a spelling suggestion for the string s.
     */
    public Suggestion check(String s) {
        // WARNING: make sure you lowercase s
        throw new NotImplementedException();
    }

    /** The set of candidates of s is defined as:
     * The original word, if it is known; otherwise
     * The list of known words at edit distance one away, if there are any; otherwise
     * The list of known words at edit distance two away, if there are any; otherwise
     * The original word, even though it is not known.
     */
    public Set<String> getCandidates(String s) {
        throw new NotImplementedException();
    }

    /**
     * Generate the set of strings at Levenshtein distance 1 from S.
     */
    @VisibleForTesting
    public Set<String> levenshteinDistanceOnce(String s) {
        throw new NotImplementedException();
    }

    /**
     * A class to hold a spelling suggestion and its score.
     */
    public static class Suggestion {
        String suggestion;
        double score;

        Suggestion(String suggestion, double score){
            this.suggestion = suggestion;
            this.score = score;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public double getScore() {
            return score;
        }
    }
}
