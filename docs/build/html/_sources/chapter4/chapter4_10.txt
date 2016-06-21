.. _chapter4_10_index:

4.10. 在Java中执行Cypher查询
==================================
    
    **提示**
    
        源代码下载地址： `JavaQuery.java <https://github.com/neo4j/community/blob/1.8/cypher/src/test/java/org/neo4j/cypher/javacompat/JavaQuery.java>`_ 

    在Java中，你能使用cypher-query-lang,Cypher查询语言像下面这样：
    
    .. code-block:: python
        :linenos:
        
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH ); 
        // add some data first 
        Transaction tx = db.beginTx(); 
        try
        { 
            Node refNode = db.getReferenceNode(); 
            refNode.setProperty( "name", "reference node" ); 
            tx.success(); 
        } 
        finally
        { 
            tx.finish(); 
        } 
          
        // let's execute a query now 
        ExecutionEngine engine = new ExecutionEngine( db ); 
        ExecutionResult result = engine.execute( "start n=node(0) return n, n.name" ); 
        System.out.println( result );

    输出结果：
    
    .. code-block:: python
        :linenos:
        
        +---------------------------------------------------+ 
        | n                              | n.name           | 
        +---------------------------------------------------+ 
        | Node[0]{name:"reference node"} | "reference node" | 
        +---------------------------------------------------+ 
        1 row 
        0 ms
        
    注意：在这使用的类来自于 ``org.neo4j.cypher.javacompat`` 包，而不是 ``org.neo4j.cypher`` ，通过下面的链接查看Java API。

    你可以在结果中获取列的一个列表：

    .. code-block:: python
        :linenos:
        
        List<String> columns = result.columns(); 
        System.out.println( columns );

    输出结果：

    .. code-block:: python
        :linenos:
        
        [n, n.name]
        
    在单列中获取结果数据集，像下面这样：

    .. code-block:: python
        :linenos:
        
        Iterator<Node> n_column = result.columnAs( "n" ); 
        for ( Node node : IteratorUtil.asIterable( n_column ) ) { 
            // note: we're grabbing the name property from the node, 
            // not from the n.name in this case. 
            nodeResult = node + ": " + node.getProperty( "name" ); 
            System.out.println( nodeResult ); 
        }
        
    在这种情况下结果中只有一个几个记录：

    .. code-block:: python
        :linenos:
        
        Node[0]: reference node

    要获取所有的列，用下面的代替：

    .. code-block:: python
        :linenos:
        
        for ( Map<String, Object> row : result ) { 
            for ( Entry<String, Object> column : row.entrySet() ) 
            { 
                rows += column.getKey() + ": " + column.getValue() + "; "; 
            } 
            rows += "\n"; 
        } 
        System.out.println( rows );
        
    输出结果：

    .. code-block:: python
        :linenos:
        
        n.name: reference node; n: Node[0];
        
    要获取Java接口中关于Cypher的更多信息，请参考：`Java API <http://components.neo4j.org/neo4j-cypher/1.8/apidocs/index.html>`_。

    要获取更多关于Cypher的范例的信息，请参考： cypher-query-lang 和 data-modeling-examples。

        
        