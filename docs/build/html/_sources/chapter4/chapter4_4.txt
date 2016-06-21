.. _chapter4_4_index:

4.4. 基本的单元测试
----------------------------------------

    Neo4j的单元测试的基本模式通过下面的范例来阐释。
    
    要访问Neo4j测试功能，你应该把neo4j-kernel 'tests.jar'新增到你的类路径中。你可以从Maven Central: `org.neo4j:neo4j-kernel <http://search.maven.org/>`_下载到需要的jars。
    
    使用Maven作为一个依赖管理，你通常会正pom.xml中增加依赖配置：
    
    .. code-block:: xml
        :linenos:
        
        <project> 
        ... 
            <dependencies> 
             <dependency> 
              <groupId>org.neo4j</groupId> 
              <artifactId>neo4j-kernel</artifactId> 
              <version>${neo4j-version}</version> 
              <type>test-jar</type> 
              <scope>test</scope> 
             </dependency> 
             ... 
            </dependencies> 
        ... 
        </project>
        
    ``${neo4j-version}`` 是Neo4j的版本号。

    到此，我们已经准备好进行单元测试编码了。
 
    提示
    
    	范例源代码下载地址： `Neo4jBasicTest.java <https://github.com/neo4j/community/blob/1.8/embedded-examples/src/test/java/org/neo4j/examples/Neo4jBasicTest.java>`_ 

    每一次开始单元测试之前，请创建一个干净的数据库：

    .. code-block:: python
        :linenos:
        
        @Before
        public void prepareTestDatabase() { 
            graphDb = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase(); 
        }

    在测试完成之后，请关闭数据库：

    .. code-block:: python
        :linenos:
        
        @After
        public void destroyTestDatabase() { 
            graphDb.shutdown(); 
        }
        
    在测试期间，创建节点并检查它们是否存在，并在一个事务中结束写操作。

    .. code-block:: python
        :linenos:
        
        Transaction tx = graphDb.beginTx(); 
  
        Node n = null; 
        try
        { 
            n = graphDb.createNode(); 
            n.setProperty( "name", "Nancy" ); 
            tx.success(); 
        } 
        catch ( Exception e ) 
        { 
            tx.failure(); 
        } 
        finally
        { 
            tx.finish(); 
        } 
          
        // The node should have an id greater than 0, which is the id of the 
        // reference node. 
        assertThat( n.getId(), is( greaterThan( 0l ) ) ); 
          
        // Retrieve a node by using the id of the created node. The id's and 
        // property should match. 
        Node foundNode = graphDb.getNodeById( n.getId() ); 
        assertThat( foundNode.getId(), is( n.getId() ) ); 
        assertThat( (String) foundNode.getProperty( "name" ), is( "Nancy" ) );
        
    如果你想查看创建数据库的参数配置，你可以这样：

    .. code-block:: python
        :linenos:
        
        Map<String, String> config = new HashMap<String, String>(); 
        config.put( "neostore.nodestore.db.mapped_memory", "10M" ); 
        config.put( "string_block_size", "60" ); 
        config.put( "array_block_size", "300" ); 
        GraphDatabaseService db = new ImpermanentGraphDatabase( config );

