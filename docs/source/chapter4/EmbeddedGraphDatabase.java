graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH ); 
registerShutdownHook( graphDb );
