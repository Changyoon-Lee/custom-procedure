package connectome.schema;

public final class Properties {

    private Properties() { throw new IllegalAccessError("Utility class");}

    public static final String IP = "ip";
    public static final String BORN = "born";//date
    public static final String CATEGORY = "category";
    public static final String RECENT = "recent";//date

    public static final String NEW_CONN = "new_conn";//True False
    public static final String DATE = "date";
    public static final String SBYTES = "sbytes";
    public static final String DBYTES = "dbytes";
    public static final String SPKTS = "spkts";
    public static final String DPKTS = "dpkts";
    public static final String DURATION = "duration";
    public static final String PCR = "pcr";
    public static final String CNT = "cnt";
    public static final String BIN = "bin";
}
