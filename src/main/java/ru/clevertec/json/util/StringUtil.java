package ru.clevertec.json.util;

import ru.clevertec.json.pattern.Patterns;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public final class StringUtil {
    private static StringUtil INSTANCE;
    public boolean isArrayValue(String str) {
        return str.startsWith(Patterns.LEFT_BRACKET) && str.endsWith(Patterns.RIGHT_BRACKET);
    }

    public String getName(String s) {
        StringBuilder builder = new StringBuilder();
        int quotes = 0;
        for (char dataSymbol : s.toCharArray()) {
            if (quotes == 2) break;
            if (dataSymbol == '"') quotes++;
            if (quotes > 0) builder.append(dataSymbol);
        }
        return builder.toString();
    }

    public String deleteQuotes(String s) {
        int quotes = 0;
        for (char c : s.toCharArray()) {
            if (c == '\"') quotes++;
            if (c != '\"') break;
        }
        s = s.trim().substring(quotes, s.length() - quotes);
        return s;
    }

    public boolean isObject(String s) {
        return (s.startsWith(Patterns.LEFT_FIGURE_BRACKET) && s.endsWith(Patterns.RIGHT_FIGURE_BRACKET));
    }

    public boolean isBoolean(String s) {
        return "false".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s);
    }

    public boolean isNumber(String s) {
        return Pattern.compile(Patterns.NUMBER_FORMAT).matcher(s).find();
    }

    public boolean isDecimal(String s) {
        return Pattern.compile(Patterns.DOUBLE_FORMAT).matcher(s).find();
    }

    public String getNumberValue(String s) {
        s = s.substring(0, s.indexOf(",")).trim();
        return Pattern.compile(Patterns.NUMBER_FORMAT).matcher(s)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElse("null");
    }

    public String getBooleanValue(String s) {
        if (s.contains(",")) {
            s = s.substring(0, s.indexOf(",")).trim();
        } else {
            s = s.trim();
        }
        return Pattern.compile(Patterns.BOOLEAN_FORMAT).matcher(s)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElse("null");
    }

    public String getArrayValue(String s) {
        StringBuilder builder = new StringBuilder();
        int brackets = 0;
        for (char c : s.toCharArray()) {
            builder.append(c);
            if (c == '[') brackets++;
            if (c == ']') brackets--;
            if (brackets == 0) break;
        }

        if (builder.toString().trim().startsWith(Patterns.LEFT_BRACKET) && builder.toString().trim().endsWith(Patterns.RIGHT_BRACKET)) {
            return builder.toString().trim();
        } else return "null";
    }

    public String cutBrackets(String s, String leftBracket, String rightBracket) {
        if (s.startsWith(leftBracket) && s.endsWith(rightBracket)) {       // to get content in {} brackets
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }

    private StringUtil() {
    }

    public static StringUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StringUtil();
        }
        return INSTANCE;
    }
}
