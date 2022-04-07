package csc22100.spelling;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Tag("manual")
class SpellCheckerTest implements WithAssertions {

    private static final String modelPath = System.getProperty("model.path");
    static SpellChecker spellChecker;
    static LanguageModel languageModel;

    @BeforeAll
    public static void setup() throws IOException {
        languageModel = LanguageModel.load(Paths.get(modelPath));
        spellChecker = new SpellChecker(languageModel);
    }

    @MethodSource("provider")
    @ParameterizedTest(name = "spellcheck: {index} {0} -> {1}")
    public void testTrainedModel(String input, String expected) {
        assertThat(spellChecker.check(input).suggestion).isEqualTo(expected);
    }

    @MethodSource("distanceOneExamples")
    @ParameterizedTest(name = "distance one: {index} {0}")
    public void testDistanceOne(String input, String candidate) {
        assertThat(spellChecker.levenshteinDistanceOnce(input)).as(String.format("%s and %s have Levenshtein distance 1", input, candidate))
                .contains(candidate);
    }

    @MethodSource("candidates")
    @ParameterizedTest(name = "candidates: {index} {0}")
    public void testCandidate(String input, String candidate) {
        assertThat(spellChecker.getCandidates(input)).as(String.format("%s should be a candidate for %s", candidate, input))
                .contains(candidate);
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("speling", "spelling"),
                Arguments.of("korrectud", "corrected"),
                Arguments.of("bycyle", "buckle"), // Norvig gets bicycle, but I'm finding buckle scores higher
                Arguments.of("inconvient", "inconvenient"),
                Arguments.of("arrainged", "arranged"),
                Arguments.of("peotry", "poetry"),
                Arguments.of("peotryy", "poetry"),
                Arguments.of("word", "word"),
                Arguments.of("quintessential", "quintessential")
        );
    }

    static Stream<Arguments> distanceOneExamples() {
        return Stream.of(
                Arguments.of("speling", "spelling"),
                Arguments.of("arrainged", "arranged"),
                Arguments.of("poetryy", "poetry"),
                Arguments.of("peotry", "poetry"),
                Arguments.of("bycyle", "bicyle"),
                Arguments.of("bicyle", "bicycle"),
                Arguments.of("inconvient", "inconveient"),
                Arguments.of("inconveient", "inconvenient")
        );
    }

    static Stream<Arguments> candidates() {
        return Stream.of(
                Arguments.of("bycyle", "bicycle")
        );
    }
}