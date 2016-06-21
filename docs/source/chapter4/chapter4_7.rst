.. _chapter4_7_index:

4.7. 图算法范例
==================================
    
    **提示**
    
        范例源代码下载地址： `PathFindingExamplesTest.java <https://github.com/neo4j/community/blob/1.8/embedded-examples/src/test/java/org/neo4j/examples/PathFindingExamplesTest.java>`_ 

    计算正连个节点之间的最短路径（最少数目的关系）：
    
    .. code-block:: python
        :linenos:
        
        Node startNode = graphDb.createNode(); 
        Node middleNode1 = graphDb.createNode(); 
        Node middleNode2 = graphDb.createNode(); 
        Node middleNode3 = graphDb.createNode(); 
        Node endNode = graphDb.createNode(); 
        createRelationshipsBetween( startNode, middleNode1, endNode ); 
        createRelationshipsBetween( startNode, middleNode2, middleNode3, endNode ); 
          
        // Will find the shortest path between startNode and endNode via 
        // "MY_TYPE" relationships (in OUTGOING direction), like f.ex: 
        // 
        // (startNode)-->(middleNode1)-->(endNode) 
        // 
        PathFinder<Path> finder = GraphAlgoFactory.shortestPath( 
            Traversal.expanderForTypes( ExampleTypes.MY_TYPE, Direction.OUTGOING ), 15 ); 
        Iterable<Path> paths = finder.findAllPaths( startNode, endNode );

    使用 `迪科斯彻（Dijkstra） <http://zh.wikipedia.org/wiki/Dijkstra>`_ 算法解决有向图中任意两个顶点之间的最短路径问题。
    
    .. code-block:: python
        :linenos:
        
        PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra( 
        Traversal.expanderForTypes( ExampleTypes.MY_TYPE, Direction.BOTH ), "cost" ); 
  
        WeightedPath path = finder.findSinglePath( nodeA, nodeB ); 
          
        // Get the weight for the found path 
        path.weight();
        
    使用 **A*** 算法是解决静态路网中求解最短路最有效的方法。

    这儿是我们的范例图：

    .. figure:: ../_static/figs/image4.8.png
    
    .. code-block: python
        :linenos:
        
        Node nodeA = createNode( "name", "A", "x", 0d, "y", 0d ); 
        Node nodeB = createNode( "name", "B", "x", 7d, "y", 0d ); 
        Node nodeC = createNode( "name", "C", "x", 2d, "y", 1d ); 
        Relationship relAB = createRelationship( nodeA, nodeC, "length", 2d ); 
        Relationship relBC = createRelationship( nodeC, nodeB, "length", 3d ); 
        Relationship relAC = createRelationship( nodeA, nodeB, "length", 10d ); 
          
        EstimateEvaluator<Double> estimateEvaluator = new EstimateEvaluator<Double>() { 
            public Double getCost( final Node node, final Node goal ) { 
                double dx = (Double) node.getProperty( "x" ) - (Double) goal.getProperty( "x" ); 
                double dy = (Double) node.getProperty( "y" ) - (Double) goal.getProperty( "y" ); 
                double result = Math.sqrt( Math.pow( dx, 2 ) + Math.pow( dy, 2 ) ); 
                return result; 
            } 
        }; 
        PathFinder<WeightedPath> astar = GraphAlgoFactory.aStar( 
            Traversal.expanderForAllTypes(), 
            CommonEvaluators.doubleCostEvaluator( "length" ), estimateEvaluator ); 
        WeightedPath path = astar.findSinglePath( nodeA, nodeB );



    
