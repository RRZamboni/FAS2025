
import java.time.LocalDateTime;

/**
 * Representa uma entrada de log de backup.
 */
public class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String message;

    public LogEntry(LocalDateTime timestamp, String level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return timestamp.toString() + " - " + level + " - " + message;
    }
}