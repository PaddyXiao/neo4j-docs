.. _chapter4_8_index:

4.8. 读取一个管理配置属性
=================================================
    
    `EmbeddedGraphDatabase <http://components.neo4j.org/neo4j/1.8/apidocs/org/neo4j/kernel/EmbeddedGraphDatabase.html>`_ 类包括了一个 `方便的方法 <http://components.neo4j.org/neo4j/1.8/apidocs/org/neo4j/kernel/EmbeddedGraphDatabase.html#getManagementBean%28java.lang.Class%29>`_ 来获取Neo4j管理用的beans。

    一般JMX服务也能使用，但比起你自己编码不如使用这概述的方法。
 
    **提示**
    
        范例源代码下载地址： `JmxTest.java <https://github.com/neo4j/community/blob/1.8/embedded-examples/src/test/java/org/neo4j/examples/JmxTest.java>`_ 

    这个范例演示了如何得到一个图数据库的开始时间：

    .. code-block:: python
        :linenos:
        
        private static Date getStartTimeFromManagementBean( GraphDatabaseService graphDbService ) { 
            GraphDatabaseAPI graphDb = (GraphDatabaseAPI) graphDbService; 
            Kernel kernel = graphDb.getSingleManagementBean( Kernel.class ); 
            Date startTime = kernel.getKernelStartTime(); 
            return startTime; 
        }
        
    不同的Neo4j版本，你将使用不同的管理beans设置。
    
        - 了解所有Neo4j版本的信息，请参考： `org.neo4j.jmx <http://components.neo4j.org/neo4j-jmx/1.8/apidocs/org/neo4j/jmx/package-summary.html>`_ 。 
        - 了解Neo4j高级版和企业版的信息，请参考： `org.neo4j.management <http://components.neo4j.org/neo4j-management/1.8/apidocs/org/neo4j/management/package-summary.html>`_ 。
        
    
