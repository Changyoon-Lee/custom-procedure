# environment
- neo4j 4.1.0.8
- apoc 4.1.0.8
- os : wsl2 ubuntu20.04

# stored procedure()
## goal
1. tbd modifying procedure v
2. delete old data procedure v
3. input new data procedure v
4. newly data chk v
5. depth query
6. search query

## worked procedure
### CALL connectome.update.load(conn_date)
- load log data from csv file (dir: $NEO4J_HOME/import/~~.csv)
- param 'conn_date' format : "yyyyMMdd" (ex "20210526")
### CALL connectome.category.modify(from, to, category)
- param 'from','to' format : String ip address (ex "192.168.0.1")
- category : String
- this procedure changes the node property(category) range from "from" to "to" ip band