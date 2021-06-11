package connectome.update;

import connectome.results.MapResult;
import connectome.results.StringResult;
import connectome.utils.Exceptions;
import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static connectome.utils.DateAdd.AddDate;
import static connectome.utils.Exceptions.*;


public class LoadData {
    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/neo4j.log`
    @Context
    public Log log;

    // input and delete old
    @Procedure(name = "connectome.update.load", mode = Mode.WRITE)
    @Description("CALL connectome.update.load(conn_date)")
    public Stream<MapResult> load(@Name(value = "conn_date", defaultValue = "") String conn_date) {
        ArrayList<Map<String, Object>> results = new ArrayList<>();

        try (Transaction tx = db.beginTx()) {
            String filename = String.format("conn_%s.csv", conn_date);
            String old_date = AddDate(conn_date, 0, 0, -7);

            try (
                Result result = tx.execute(String.join("","CALL apoc.periodic.iterate(\"CALL apoc.load.csv('",filename,"' ,{header:TRUE, sep:'\\t', mapping:{sbytes:{type:'float'}, spkts:{type:'int'}, dbytes:{type:'float'}, dpkts:{type:'int'}, dst_port:{type:'int'}, bin:{type:'int'}, duration:{type:'float'}, count:{type:'int'}, pcr:{type:'float'}, prtc:{array:TRUE, arraySep:','}}}) yield map as row\",",
                        "\"MERGE (a:IP_address{ip:row.src_ip}) ON CREATE SET a.born='",conn_date,"',a.category='tbd'",
                        " ON MATCH SET a.recent='",conn_date,
                        "' MERGE (b:IP_address{ip:row.dst_ip}) ON CREATE SET b.born=",conn_date,", b.category='tbd'",
                        " ON MATCH SET b.recent='",conn_date,
                        "' MERGE (a)-[c:conn_",conn_date,"{sbytes:row.sbytes, dbytes:row.dbytes, spkts:row.spkts, dpkts:row.dpkts, duration:row.duration, port:row.dst_port, bin:row.bin, cnt:row.count, pcr:row.pcr, prct:row.prtc}]->(b)\", {batchSize:10000, iterateList:TRUE, parallet:FALSE}) YIELD operations RETURN operations"))
                ) {
//                    results.add(result.next());
                String res = result.resultAsString();
                results.add(Map.of("res",res));
                } catch (Exception e){return Stream.of(DATA_LOAD_FAILED1);}

            try {
                tx.execute(String.format("MATCH ()-[r:conn_%s]-() DELETE r", old_date));
                results.add(Map.of("success", "delete old data"));
            } catch (Exception e){return Stream.of(DATA_LOAD_FAILED2);}

            tx.commit();
        } catch (Exception e){return Stream.of(DATA_LOAD_FAILED);}
        return results.stream().map(MapResult::new);
    }




}
