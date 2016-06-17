2.2. 比较各种数据库模型
=========================================================

    图数据库通过在一张图上存储节点和关系来持久化我们的数据结构。比起其他持久化数据模型如何呢？因为图是一种常规数据结构，让我们与其他的进行一下比较试试看。

2.2.1. 从图数据库转换成 RDBMS
-------------------------------------------------------------------

    将所有的数据用竖立的堆栈表示，并且保持他们直接的关系，你可以看到下面一张图。一个 RDBMS 被优化用于聚合数据，而Neo4j擅长于高度关联的数据。
    图 2.1. RDBMS
    
    .. image:: https://github.com/PaddyXiao/neo4j-docs/blob/master/docs/images/image2.1.png?raw=true
    
    图 2.2. 用图实现RDBMS模型
    
    .. image:: https://github.com/PaddyXiao/neo4j-docs/blob/master/docs/images/image2.2.png?raw=true
    

2.2.2. 从图数据库转换成Key-Value数据库
------------------------------------------------------------------------

    Key-Value模型适合用于简单的数据或者列表。当数据之间不断交互关联时，你更需要一张图模型。Neo4j让你能惊醒制作简单的数据结构到复杂，互相连接的数据。
    图 2.3. Key-Value 存储模型
    
    .. image:: https://github.com/PaddyXiao/neo4j-docs/blob/master/docs/images/image2.3.png?raw=true
    
    K* 代表一个键，V* 代表一个值。请注意，某些键指向其他键以及普通值。
    图 2.4. 用图实现 Key-Value 模型
    
    .. image:: https://github.com/PaddyXiao/neo4j-docs/blob/master/docs/images/image2.4.png?raw=true
    
2.2.3. 从图数据库转换成列数据库
------------------------------------------------------------------------

    列式（大表）数据库是 Key-Value模型的升级，用 “”来允许行数据增加。如果存储一张图，这个表将是分层的，关系也是非常明确的。

2.2.4. 从图数据库转换成文档型数据库
------------------------------------------------------------------------

    文档型数据库用文档进行层次划分，而自由的数据规划也很容易被表示成一颗树。成长为一张图的话，文档之间的关联你需要更有代表性的数据结构来存储，而在Neo4j中，这些关系是非常容易处理的。
    图 2.5. 文档型数据库
    
    .. image:: https://github.com/PaddyXiao/neo4j-docs/blob/master/docs/images/image2.5.png?raw=true
    
    D=文档, S=子文档, V=值, D2/S2 = 关联到（其他）文档的索引。
    
    图 2.6. 从图数据库转换成文档型数据库
    
    .. image:: https://github.com/PaddyXiao/neo4j-docs/blob/master/docs/images/image2.6.png?raw=true 

