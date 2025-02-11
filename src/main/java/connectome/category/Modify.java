package connectome.category;

import connectome.schema.Labels;
import connectome.results.MapResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import static connectome.schema.Properties.CATEGORY;
import static connectome.schema.Properties.IP;
import static connectome.utils.Converter.convertIP2Long;
import static connectome.utils.Exceptions.IP_NOT_FOUND;

public class Modify {
    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/neo4j.log`
    @Context
    public Log log;

    private static final int TIMEOUT = 1;

    @Procedure(name = "connectome.category.modify", mode = Mode.WRITE)
    @Description("CALL connectome.category.modify(from, to, category)")
    public Stream<MapResult> modify(@Name(value = "from", defaultValue = "") String from,
                                    @Name(value = "to", defaultValue = "") String to,
                                    @Name(value = "category", defaultValue = "tbd") String category) {
        ArrayList<Map<String, Object>> results = new ArrayList<>();
        ArrayList<Object> res = new ArrayList<>();
        long fromL = convertIP2Long(from);
        long toL = convertIP2Long(to);

        try (Transaction tx = db.beginTx(); ResourceIterator<Node> ip_iterator = tx.findNodes(Labels.IP_address))  {

            if (!ip_iterator.hasNext()) {
                return Stream.of(IP_NOT_FOUND);
            }
            while (ip_iterator.hasNext()) {
                Node node = ip_iterator.next();
                String string_ip = node.getProperty(IP).toString();
                long long_ip = convertIP2Long(string_ip);
                if (long_ip >= fromL && long_ip <= toL) {
                    node.setProperty(CATEGORY, category);
                    res.add(string_ip);
                }
            }
            results.add(Map.of("changed ip list", res));
            tx.commit();
        }
        return results.stream().limit(1000).map(MapResult::new);

    }
}
