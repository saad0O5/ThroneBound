package persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal, purpose-built JSON reader/writer for PlayerProfile persistence.
 * This is NOT a general-purpose JSON library — per the project's "no
 * external libraries" rule, it only supports the flat shapes persistence
 * actually needs: string fields and arrays of strings.
 */
final class JsonUtil {
    private JsonUtil() { }

    static String escape(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);
                switch (next) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    default: sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Returns the raw (still-escaped) value of a top-level "key": field — a string's inner text, or an array's "[...]" text. */
    static String extractRawField(String json, String key) {
        String marker = "\"" + key + "\"";
        int keyIndex = json.indexOf(marker);
        if (keyIndex == -1) return null;
        int colon = json.indexOf(':', keyIndex + marker.length());
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length()) return null;

        char start = json.charAt(i);
        if (start == '"') {
            int end = i + 1;
            StringBuilder sb = new StringBuilder();
            while (end < json.length() && json.charAt(end) != '"') {
                if (json.charAt(end) == '\\') {
                    sb.append(json.charAt(end));
                    end++;
                }
                sb.append(json.charAt(end));
                end++;
            }
            return sb.toString();
        } else if (start == '[') {
            int depth = 0;
            int end = i;
            do {
                if (json.charAt(end) == '[') depth++;
                if (json.charAt(end) == ']') depth--;
                end++;
            } while (depth > 0 && end < json.length());
            return json.substring(i, end);
        }
        return null;
    }

    /** Parses a JSON array of strings, e.g. ["a","b"]. */
    static List<String> parseStringArray(String rawArray) {
        List<String> result = new ArrayList<>();
        if (rawArray == null) return result;
        String inner = rawArray.trim();
        if (inner.startsWith("[")) inner = inner.substring(1);
        if (inner.endsWith("]")) inner = inner.substring(0, inner.length() - 1);
        inner = inner.trim();
        if (inner.isEmpty()) return result;

        int i = 0;
        while (i < inner.length()) {
            while (i < inner.length() && inner.charAt(i) != '"') i++;
            if (i >= inner.length()) break;
            i++;
            StringBuilder sb = new StringBuilder();
            while (i < inner.length() && inner.charAt(i) != '"') {
                if (inner.charAt(i) == '\\' && i + 1 < inner.length()) {
                    sb.append(inner.charAt(i));
                    i++;
                }
                sb.append(inner.charAt(i));
                i++;
            }
            i++;
            result.add(unescape(sb.toString()));
        }
        return result;
    }
}
