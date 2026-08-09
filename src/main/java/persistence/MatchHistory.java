package persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * MatchHistory
 * Owner: Member C (GUI & Persistence)
 */
public class MatchHistory {
    private final List<MatchRecord> records;

    public MatchHistory() {
        this.records = new ArrayList<>();
    }

    public List<MatchRecord> getRecords() { return records; }

    public void addRecord(MatchRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Cannot add a null MatchRecord");
        }
        records.add(record);
    }
}
