package csc22100.spelling;

import com.google.common.base.Stopwatch;
import csc22100.spelling.evaluation.EvalSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name="spell", mixinStandardHelpOptions = true)
public class SpellCheckerMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckerMain.class);
    private static final URI CORPUS = URI.create("https://norvig.com/big.txt");
    private static final URI DEV_SET = URI.create("https://norvig.com/spell-testset1.txt");

    @CommandLine.Command(mixinStandardHelpOptions = true)
    void download(@CommandLine.Option(names={"--output-dir"}) Path outputDir) throws IOException, InterruptedException {
        // download the training corpus and the error data
        LOGGER.info("Downloading {}", CORPUS);
        Utils.download(CORPUS, outputDir.resolve("big.txt"));
        LOGGER.info("Downloading {}", DEV_SET);
        Utils.download(DEV_SET, outputDir.resolve("dev.txt"));
    }

    @CommandLine.Command(mixinStandardHelpOptions = true)
    void prepare(@CommandLine.Option(names={"--working-dir"}) Path workingDir) throws IOException {
        prepareData(workingDir, workingDir);
    }


    @CommandLine.Command(mixinStandardHelpOptions = true)
    void train(@CommandLine.Option(names={"--working-dir"}) Path workingDir) throws IOException {
        WordFrequencyCounter wordFrequencyCounter = WordFrequencyCounter.load(workingDir.resolve("counts.json"));
        LanguageModel languageModel = LanguageModel.train(wordFrequencyCounter);
        languageModel.save(workingDir.resolve("model.json"));
    }

    @CommandLine.Command(mixinStandardHelpOptions = true)
    void evaluate(@CommandLine.Option(names={"--working-dir"}) Path workingDir) throws IOException {
        runEval(workingDir, workingDir.resolve("data.json"));
    }

    private void runEval(Path workingDir, Path testData) throws IOException {
        LanguageModel languageModel = LanguageModel.load(workingDir.resolve("model.json"));
        SpellChecker spellChecker = new SpellChecker(languageModel);
        EvalSet evalSet = EvalSet.load(testData);
        int correct = 0;
        int incorrect = 0;
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (EvalSet.EvalPair pair : evalSet.getPairs()) {
            SpellChecker.Suggestion suggestion = spellChecker.check(pair.getMisspelling());
            if (suggestion.suggestion.equals(pair.getCorrected())) {
                correct++;
            } else {
                incorrect++;
            }
        }
        stopwatch.stop();
        System.out.printf("Correct: %d%n", correct);
        System.out.printf("Incorrect: %d%n", incorrect);
        System.out.printf("Accuracy: %s%n", correct * 1.0 / (correct + incorrect));
        System.out.printf("Words per second: %d%n", (correct + incorrect) / stopwatch.elapsed(TimeUnit.SECONDS));
    }

    @CommandLine.Command(mixinStandardHelpOptions = true)
    void grade(@CommandLine.Option(names={"--data-dir"}) Path dataDir,
               @CommandLine.Option(names={"--working-dir"}) Path workingDir) throws IOException {
        prepareData(dataDir, workingDir);
        train(workingDir);
        runEval(workingDir, dataDir.resolve("test.json"));
    }

    private void prepareData(Path dataDir, Path workingDir) throws IOException {
        LOGGER.info("Preprocessing corpus...");
        List<String> lines = Utils.getLinesFromFile(dataDir.resolve("big.txt"));
        WordFrequencyCounter wordFrequencyCounter = new WordFrequencyCounter();
        wordFrequencyCounter.countFrequencies(lines);
        wordFrequencyCounter.save(workingDir.resolve("counts.json"));
        LOGGER.info("Preprocessing dev examples...");
        EvalSet.prepare(dataDir.resolve("dev.txt"), workingDir, wordFrequencyCounter);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SpellCheckerMain()).execute(args);
        System.exit(exitCode);
    }
}
