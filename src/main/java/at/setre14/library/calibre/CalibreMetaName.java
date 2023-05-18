package at.setre14.library.calibre;

public enum CalibreMetaName {
    SERIES("calibre:series"),
    SERIES_INDEX("calibre:series_index"),
    RATING_SIMON("calibre:user_metadata:#bewertung_s"),
    READ_SIMON("calibre:user_metadata:#read_s"),
    RATING_TAMARA("calibre:user_metadata:#bewertung_t"),
    READ_TAMARA("calibre:user_metadata:#read_t");


    String name;

    CalibreMetaName(String name) {
        this.name = name;
    }

    static CalibreMetaName get(String name) {
        for (CalibreMetaName metaName : CalibreMetaName.values()) {
            if (metaName.name.equals(name)) {
                return metaName;
            }
        }

        return null;
    }
}
