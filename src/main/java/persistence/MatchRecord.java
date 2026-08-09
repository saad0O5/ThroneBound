package persistence;

import java.time.LocalDateTime;

/**
 * MatchRecord
 * Owner: Member C (GUI & Persistence)
 */
public class MatchRecord {
    private final String opponent;
    private final String result;
    private final String date;

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
