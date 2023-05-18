package at.setre14.library.calibre;

public enum CalibreTag {
    AUTHOR("dc:creator"),
    DESCRIPTION("dc:description"),
    LANGUAGE("dc:language"),
    META("meta"),
    TAG("dc:subject"),
    TITLE("dc:title");

    String tag;

    CalibreTag(String tag) {
        this.tag = tag;
    }
}
