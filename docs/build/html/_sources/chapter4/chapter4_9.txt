.. _chapter4_8_index:

4.9. OSGi配置
==================================
    
    在 `OSGi <http://www.osgi.org/>`_ 关联的上下文比如大量的应用服务器（e.g. `Glassfish <http://glassfish.java.net/>`_ ）和基于 `Eclipse <http://www.eclipse.org>`_ 的系统中，Neo4j能被明确地建立起来而不是通过Java服务加载机制来发现。
    
4.9.1. Simple OSGi Activator 脚本
------------------------------------------------------------------

    如同在下面的范例中看到的一样，为了代替依赖Neo4j内核的类加载，Neo4j Bundle被作为库 bundles，而像 IndexProviders 和 CacheProviders 这样的服务被明确地实例化，配置和注册了秩序。只需要确保必要的jars，所以所有必须的类都被导出并且包括这Activator。
    
    .. code-block:: python
        :linenos:
        
        public class Neo4jActivator implements BundleActivator { 
          
            private static GraphDatabaseService db; 
            private ServiceRegistration serviceRegistration; 
            private ServiceRegistration indexServiceRegistration; 
          
            @Override
            public void start( BundleContext context ) throws Exception 
            { 
                //the cache providers 
                ArrayList<CacheProvider> cacheList = new ArrayList<CacheProvider>(); 
                cacheList.add( new SoftCacheProvider() ); 
          
                //the index providers 
                IndexProvider lucene = new LuceneIndexProvider(); 
                ArrayList<IndexProvider> provs = new ArrayList<IndexProvider>(); 
                provs.add( lucene ); 
                ListIndexIterable providers = new ListIndexIterable(); 
                providers.setIndexProviders( provs ); 
          
                //the database setup 
                GraphDatabaseFactory gdbf = new GraphDatabaseFactory(); 
                gdbf.setIndexProviders( providers ); 
                gdbf.setCacheProviders( cacheList ); 
                db = gdbf.newEmbeddedDatabase( "target/db" ); 
          
                //the OSGi registration 
                serviceRegistration = context.registerService( 
                        GraphDatabaseService.class.getName(), db, new Hashtable<String,String>() ); 
                System.out.println( "registered " + serviceRegistration.getReference() ); 
                indexServiceRegistration = context.registerService( 
                        Index.class.getName(), db.index().forNodes( "nodes" ), 
                        new Hashtable<String,String>() ); 
                Transaction tx = db.beginTx(); 
                try
                { 
                    Node firstNode = db.createNode(); 
                    Node secondNode = db.createNode(); 
                    Relationship relationship = firstNode.createRelationshipTo( 
                            secondNode, DynamicRelationshipType.withName( "KNOWS" ) ); 
          
                    firstNode.setProperty( "message", "Hello, " ); 
                    secondNode.setProperty( "message", "world!" ); 
                    relationship.setProperty( "message", "brave Neo4j " ); 
                    db.index().forNodes( "nodes" ).add( firstNode, "message", "Hello" ); 
                    tx.success(); 
                } 
                catch ( Exception e ) 
                { 
                    e.printStackTrace(); 
                    throw new RuntimeException( e ); 
                } 
                finally
                { 
                    tx.finish(); 
                } 
          
            } 
          
            @Override
            public void stop( BundleContext context ) throws Exception 
            { 
                serviceRegistration.unregister(); 
                indexServiceRegistration.unregister(); 
                db.shutdown(); 
          
            } 
          
        }

    
