package connectome.utils;

import connectome.results.MapResult;

import java.util.Map;

public class Exceptions {
    public static final MapResult IP_NOT_FOUND = new MapResult(Map.of("Error","ip not Found"));
    public static final MapResult DATA_LOAD_FAILED = new MapResult(Map.of("Error", "data load failed"));
    public static final MapResult DATA_LOAD_FAILED1 = new MapResult(Map.of("Error1", "data load failed"));
    public static final MapResult DATA_LOAD_FAILED2 = new MapResult(Map.of("Error2", "data load failed"));
    public static final MapResult DELETE_OLD_DATA = new MapResult(Map.of("Success", "delete old data"));
}
