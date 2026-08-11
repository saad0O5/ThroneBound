package persistence;

import java.time.LocalDateTime;

/**
 * MatchRecord
 * Owner: Member C (GUI & Persistence)
 */
public class MatchRecord {
    private String opponent;
    private String result;
    private String date;

    public MatchRecord() {
        // Required for JSON deserialization
    }

    public MatchRecord(String opponent, String result, String date) {
        this.opponent = opponent;
        this.result = result;
        this.date = date;
    }

    /** Convenience constructor — stamps the record with the current time. */
    public MatchRecord(String opponent, String result) {
        this(opponent, result, LocalDateTime.now().toString());
    }

    public String getOpponent() { return opponent; }
    public String getResult() { return result; }
    public String getDate() { return date; }
}
