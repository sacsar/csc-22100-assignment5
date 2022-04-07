package csc22100.spelling;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private static final Pattern WORD_PATTERN = Pattern.compile("\\w+");

    public List<String> getTokens(String s) {
        Preconditions.checkNotNull(s);
        Matcher matcher = WORD_PATTERN.matcher(s.trim());
        List<String> result = new ArrayList<>();
        while(matcher.find()) {
            String token = matcher.group().toLowerCase(Locale.ROOT);
            result.add(token);
        }
        return result;
    }
}
