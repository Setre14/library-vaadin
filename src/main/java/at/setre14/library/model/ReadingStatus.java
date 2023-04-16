package at.setre14.library.model;

public enum ReadingStatus {
    NOT_READ,
    COMPLETED,
    READING,
    ON_HOLD;

    public static ReadingStatus get(boolean read) {
        return read ? COMPLETED : NOT_READ;
    }
}
