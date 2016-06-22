.. _chapter5_2_index:

5.2. 在Java中如何使用REST API
======================================================================
	
    
    
5.2.1. 通过 REST API 创建一个图数据库
----------------------------------------------------------------------------------------------
	
    REST API使用 `HTTP` 协议和 `JSON` 数据格式，以至于它能用于多种语言和平台。当准备开始使用的时候，看一些可以被重用的模式也是非常有帮助的。在这个简短的概述中，我们将为你展示如何使用 REST API 创建和维护一个简单的图数据库并且如何从中查询数据。

    对于这些范例，我们选择了 `Jersey <http://jersey.java.net/>`_ 客户端组件，这个组件之前我们已经通过 `Maven` 下载了。
    
    
5.2.2. 启动图数据库服务器
--------------------------------------------------------------------
	
    在我们对服务器做任何操作之前，我们需要启动它。了解服务器安装的详细信息，请参考：**server-installation** 。
    
    .. code-block:: python
        :linenos:
        
        WebResource resource = Client.create() 
            .resource( SERVER_ROOT_URI ); 
        ClientResponse response = resource.get( ClientResponse.class ); 
          
        System.out.println( String.format( "GET on [%s], status code [%d]", 
                SERVER_ROOT_URI, response.getStatus() ) ); 
        response.close();
        
    如果返回状态码是 ``200 OK`` ，那我们知道服务器已经运行良好而我们也能继续了。如果连接到服务器失败，请参考：server。

    注意：如果你得到任何大于 ``200 OK``（特别是 ``4xx`` 和 ``5xx`` ）的返回码，那么请检查你的配置并且查看在目录 `'data/log'` 的日志文件。
    
5.2.3. 创建一个节点
--------------------------------------------------
	
    REST API使用+POST+方式创建节点。在Java中使用Jersey客户端封装是很简单的：
    
    .. code-block:: python
        :linenos:
        
        final String nodeEntryPointUri = SERVER_ROOT_URI + "node"; 
        // http://localhost:7474/db/data/node 
          
        WebResource resource = Client.create() 
                .resource( nodeEntryPointUri ); 
        // POST {} to the node entry point URI 
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ) 
                .type( MediaType.APPLICATION_JSON ) 
                .entity( "{}" ) 
                .post( ClientResponse.class ); 
          
        final URI location = response.getLocation(); 
        System.out.println( String.format( 
                "POST to [%s], status code [%d], location header [%s]", 
                nodeEntryPointUri, response.getStatus(), location.toString() ) ); 
        response.close(); 
          
        return location;
        
    如果请求成功完成，它会在后台发送一个包括 `JSON` 格式的数据的 `HTTP` 请求到图数据库服务器。这服务器将会在数据库中创建一个新的节点并且返回一个状态码 ``201 Created`` 和一个包含新节点地址的 ``Location`` 头信息。
    
    在我们的范例中，我们将调用两次这个功能以便在我们的数据库中创建两个节点。
    
5.2.4. 增加属性
--------------------------------------
	
    一旦我们在我们的数据库中有了节点，我们能用他们存储有用的数据。在这种情况下，我们将在我们的数据库中存储关于音乐的信息。让我们先看看我们创建节点和增加属性的代码。这儿我们已经增加了一个节点用来表示 `Joe Strummer` 和一个乐队 `The Clash` 。
    
    .. code-block:: python
        :linenos:
        
        URI firstNode = createNode(); 
        addProperty( firstNode, "name", "Joe Strummer" ); 
        URI secondNode = createNode(); 
        addProperty( secondNode, "band", "The Clash" );
        
    在 +addProperty+方法内部我们确定了表示节点属性的资源以及这个属性的名称。我们然后 +PUT+那个属性的值到服务器。
    
    .. code-block:: python
        :linenos:
        
        String propertyUri = nodeUri.toString() + "/properties/" + propertyName; 
        // http://localhost:7474/db/data/node/{node_id}/properties/{property_name} 
          
        WebResource resource = Client.create() 
                .resource( propertyUri ); 
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ) 
                .type( MediaType.APPLICATION_JSON ) 
                .entity( "\"" + propertyValue + "\"" ) 
                .put( ClientResponse.class ); 
          
        System.out.println( String.format( "PUT to [%s], status code [%d]", 
                propertyUri, response.getStatus() ) ); 
        response.close();
        
    如果一切运行正常，我们将得到一个 ``204 No Content`` 的返回码表示服务器已经处理了我们的情况但并不会回显属性的值。

5.2.5. 增加关系
--------------------------------------
	
    现在我们有了表示 `Joe Strummer` 和 `The Clash` 的节点，我们将给他们建立关系。 REST API支持通过一个 **POST** 请求来为节点间建立关系。在Java中与此相对应，我们 **POST** 一些JSON数据到表示 `Joe Strummer` 的节点的地址上面，来确定一个该节点和表示 `The Clash` 的节点之前的关系。
    
    .. code-block:: python
        :linenos:
        
        URI relationshipUri = addRelationship( firstNode, secondNode, "singer", 
            "{ \"from\" : \"1976\", \"until\" : \"1986\" }" );

    在 ``addRelationship()`` 方法内部，我们确定了节点 `Joe Strummer` 的关系的URI，然后 **POST** 了一个 `JSON` 数据到服务器。这个 `JSON` 数据包括目标节点，关系类型以及其他任何属性。
    
    .. code-block:: python
        :linenos:
        
        private static URI addRelationship( URI startNode, URI endNode, 
                String relationshipType, String jsonAttributes ) 
                throws URISyntaxException { 
            URI fromUri = new URI( startNode.toString() + "/relationships" ); 
            String relationshipJson = generateJsonRelationship( endNode, 
                    relationshipType, jsonAttributes ); 
          
            WebResource resource = Client.create() 
                    .resource( fromUri ); 
            // POST JSON to the relationships URI 
            ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ) 
                    .type( MediaType.APPLICATION_JSON ) 
                    .entity( relationshipJson ) 
                    .post( ClientResponse.class ); 
          
            final URI location = response.getLocation(); 
            System.out.println( String.format( 
                    "POST to [%s], status code [%d], location header [%s]", 
                    fromUri, response.getStatus(), location.toString() ) ); 
          
            response.close(); 
            return location; 
        }
        
    如果一切运行正常，我们将收到一个状态码 ``201 Created`` 并且一个我们刚创建的关系的URI在 `HTTP` 头里面的 ``Location`` 。

5.2.6. 给关系增加属性
--------------------------------------------------------
	
    像节点一样，关系也可以有属性。因为我们是 `Joe Strummer` 和 `the Clash` 的超级大粉丝，我们将增加一个评价属性到关系上面以至于其他人能看到这个乐队的5星级歌手。
    
    .. code-block:: python
        :linenos:
        
        addMetadataToProperty( relationshipUri, "stars", "5" );
        
    在 ``addMetadataToProperty()`` 方法内部，我们确定关系的属性的URI，并且 **PUT** 我们的新值到服务器（因为它是 **PUT** 所以它总是会覆盖已经存在的值，所以一定要小心）。
    
    .. code-block:: python
        :linenos:
        
        private static void addMetadataToProperty( URI relationshipUri, 
                String name, String value ) throws URISyntaxException 
        { 
            URI propertyUri = new URI( relationshipUri.toString() + "/properties" ); 
            String entity = toJsonNameValuePairCollection( name, value ); 
            WebResource resource = Client.create() 
                    .resource( propertyUri ); 
            ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ) 
                    .type( MediaType.APPLICATION_JSON ) 
                    .entity( entity ) 
                    .put( ClientResponse.class ); 
          
            System.out.println( String.format( 
                    "PUT [%s] to [%s], status code [%d]", entity, propertyUri, 
                    response.getStatus() ) ); 
            response.close(); 
        }

    假设一切运行正常，我们将得到一个 ``200 OK`` 返回码（我们也可以调用 ``ClientResponse.getStatus()`` 来获取）而且我们现在可以确定我们已经可以从一个小型图数据库中查询数据了。

5.2.7. 从图数据库中查询数据
--------------------------------------------------------------------------
	
    作为图数据库的嵌入模式，Neo4j服务器使用图遍历来在途中查询数据。当前Neo4j服务器期望一个 `JSON` 数据通过 **POST** 发送过来进行遍历查询（虽然这也可以改变成 **GET** 的方式）。

    要启动这个进程，我们用一个简单的类来封装 `JSON` 数据并通过 **POST** 发送到服务器，在这种情况下我们硬编码遍历查询来查找所有带有输出方向关系 `"singer"` 的所有节点。
    
    .. code-block:: python
        :linenos:
        
        // TraversalDescription turns into JSON to send to the Server 
        TraversalDescription t = new TraversalDescription(); 
        t.setOrder( TraversalDescription.DEPTH_FIRST ); 
        t.setUniqueness( TraversalDescription.NODE ); 
        t.setMaxDepth( 10 ); 
        t.setReturnFilter( TraversalDescription.ALL ); 
        t.setRelationships( new Relationship( "singer", Relationship.OUT ) );

    一旦我们定义了我们的遍历查询所需的参数，我们只需要传递它。我们先确定起始节点的遍历查询的URI，然后 +POST+遍历查询的JSON数据来完成这个需求。
    
    .. code-block:: python
        :linenos:
        
        URI traverserUri = new URI( startNode.toString() + "/traverse/node" ); 
        WebResource resource = Client.create() 
                .resource( traverserUri ); 
        String jsonTraverserPayload = t.toJson(); 
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ) 
                .type( MediaType.APPLICATION_JSON ) 
                .entity( jsonTraverserPayload ) 
                .post( ClientResponse.class ); 
          
        System.out.println( String.format( 
                "POST [%s] to [%s], status code [%d], returned data: "
                        + System.getProperty( "line.separator" ) + "%s", 
                jsonTraverserPayload, traverserUri, response.getStatus(), 
                response.getEntity( String.class ) ) ); 
        response.close();

    一旦请求被完成，我们将得到歌手的数据集以及他们所属的乐队：
    
    .. code-block:: json
        :linenos:
        
        [ { 
            "outgoing_relationships" : "http://localhost:7474/db/data/node/82/relationships/out", 
            "data" : { 
              "band" : "The Clash", 
              "name" : "Joe Strummer"
            }, 
            "traverse" : "http://localhost:7474/db/data/node/82/traverse/{returnType}", 
            "all_typed_relationships" : "http://localhost:7474/db/data/node/82/relationships/all/{-list|&|types}", 
            "property" : "http://localhost:7474/db/data/node/82/properties/{key}", 
            "all_relationships" : "http://localhost:7474/db/data/node/82/relationships/all", 
            "self" : "http://localhost:7474/db/data/node/82", 
            "properties" : "http://localhost:7474/db/data/node/82/properties", 
            "outgoing_typed_relationships" : "http://localhost:7474/db/data/node/82/relationships/out/{-list|&|types}", 
            "incoming_relationships" : "http://localhost:7474/db/data/node/82/relationships/in", 
            "incoming_typed_relationships" : "http://localhost:7474/db/data/node/82/relationships/in/{-list|&|types}", 
            "create_relationship" : "http://localhost:7474/db/data/node/82/relationships"
          }, { 
            "outgoing_relationships" : "http://localhost:7474/db/data/node/83/relationships/out", 
            "data" : { 
            }, 
            "traverse" : "http://localhost:7474/db/data/node/83/traverse/{returnType}", 
            "all_typed_relationships" : "http://localhost:7474/db/data/node/83/relationships/all/{-list|&|types}", 
            "property" : "http://localhost:7474/db/data/node/83/properties/{key}", 
            "all_relationships" : "http://localhost:7474/db/data/node/83/relationships/all", 
            "self" : "http://localhost:7474/db/data/node/83", 
            "properties" : "http://localhost:7474/db/data/node/83/properties", 
            "outgoing_typed_relationships" : "http://localhost:7474/db/data/node/83/relationships/out/{-list|&|types}", 
            "incoming_relationships" : "http://localhost:7474/db/data/node/83/relationships/in", 
            "incoming_typed_relationships" : "http://localhost:7474/db/data/node/83/relationships/in/{-list|&|types}", 
            "create_relationship" : "http://localhost:7474/db/data/node/83/relationships"
          }
        ]

5.2.8. 喔，是这样吗？==
------------------------------------------------------------
	
    那是我们用 **REST API** 做我们的事情的方式。
    
    自然而然的我们提交到服务器的任何 `HTTP` 语义都很容易被封装，包括通过 ``DELETE`` 来移除节点和关系。不过，如果你已经完全掌握了，那么在 `Jersey` 客户端从 ``.delete()`` 切换成 ``.delete()`` 是非常容易的。

5.2.9. 下一步计划是什么呢？
--------------------------------------------------------------------------
	
    **HTTP API** 提供一个客户端库更好的基本实现，它也是优秀的基于 `HTTP` 的 **REST** 接口。
    
    比起提供友好的语言级的开发架构实现，尽管他们能非常简单的绑定来使用嵌入模式的图数据库，我们还是计划在将来让常用语言都提供基于 **REST API** 的客户端绑定实现。要了解当前各种语言的 `Neo4j REST` 客户端实现以及嵌入封装，请参考： http://www.delicious.com/neo4j/drivers 。

5.2.10. 附录：代码
----------------------------------------------
	
    - `CreateSimpleGraph.java <https://github.com/neo4j/community/blob/1.8/server-examples/src/main/java/org/neo4j/examples/server/CreateSimpleGraph.java>`_
    - `Relationship.java <https://github.com/neo4j/community/blob/1.8/server-examples/src/main/java/org/neo4j/examples/server/Relationship.java>`_
    - `TraversalDescription.java <https://github.com/neo4j/community/blob/1.8/server-examples/src/main/java/org/neo4j/examples/server/TraversalDescription.java>`_

