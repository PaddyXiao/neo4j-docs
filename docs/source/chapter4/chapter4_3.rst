.. _chapter4_3_index:

4.3. 带索引的用户数据库
==================================================

    你有一个用户数据库，希望通过名称查找到用户。首先，下面这是我们想创建的数据库结构：

    `图 4.2. 用户节点空间预览`
    
    .. figure:: _static/figs/image4.2.png
    
    其中，参考节点连接了一个用户参考节点，而真实的所有用户都连接在用户参考节点上面。
 
    提示
    	
        范例中的源代码下载地址： `EmbeddedNeo4jWithIndexing.java <https://github.com/neo4j/community/blob/1.8/embedded-examples/src/main/java/org/neo4j/examples/EmbeddedNeo4jWithIndexing.java>`_

    首先，我们定义要用到的关系类型：

    .. code-block:: python
        :linenos:
        
        private static enum RelTypes implements RelationshipType { 
            USERS_REFERENCE, 
            USER 
        }

    然后，我们创建了两个辅助方法来处理用户名称以及往数据库新增用户：

    .. code-block:: python
        :linenos:
         
        private static String idToUserName( final int id ) { 
            return "user" + id + "@neo4j.org"; 
        } 
          
        private static Node createAndIndexUser( final String username ) { 
            Node node = graphDb.createNode(); 
            node.setProperty( USERNAME_KEY, username ); 
            nodeIndex.add( node, USERNAME_KEY, username ); 
            return node; 
        }
        
    下一步我们将启动数据库:

    .. code-block:: python
        :linenos:
 
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH ); 
        nodeIndex = graphDb.index().forNodes( "nodes" ); 
        registerShutdownHook();
        
    是时候新增用户了：

    .. code-block:: python
        :linenos:
        
        Transaction tx = graphDb.beginTx(); 
        try { 
            // Create users sub reference node 
            Node usersReferenceNode = graphDb.createNode(); 
            graphDb.getReferenceNode().createRelationshipTo( 
                usersReferenceNode, RelTypes.USERS_REFERENCE ); 
            // Create some users and index their names with the IndexService 
            for ( int id = 0; id < 100; id++ ) 
            { 
                Node userNode = createAndIndexUser( idToUserName( id ) ); 
                usersReferenceNode.createRelationshipTo( userNode, 
                    RelTypes.USER ); 
            }
        }
        
    通过Id查找用户:

    .. code-block:: python
        :linenos:
        
        int idToFind = 45; 
        Node foundUser = nodeIndex.get( USERNAME_KEY, idToUserName( idToFind ) ).getSingle(); 
        System.out.println( "The username of user " + idToFind + " is " + foundUser.getProperty( USERNAME_KEY ) );

    
    