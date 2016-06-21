.. _chapter4_2_index:

4.2. 你好，世界
==================================
    
    正这里可以学习如何创建和访问节点和关系。关于建立工程环境的信息，请参考：:ref:`第 4.1 节 将Neo4j引入到你的项目工程中 <chapter4_1_index>`。

    从:ref:`第 2.1 节 什么是图数据库 <chapter2_1_index>` 中，我们还记得，一个Neo4j图数据库由以下几部分组成：
    
    - 相互关联的节点 
    - 有一定的关系存在 
    - 在节点和关系上面有一些属性。
    
    所有的关系都有一个类型。比如，如果一个图数据库实例表示一个社网络，那么一个关系类型可能叫 KNOWS 。

    如果一个类型叫 KNOWS 的关系连接了两个节点，那么这可能表示这两个人呼吸认识。一个图数据库中大量的语义都被编码成关系的类型来使用。虽然关系是直接相连的，但他们也可以不用考虑他们遍历的方向而互相遍历对方。
 
    **提示**
    
        范例源代码下载地址：`EmbeddedNeo4j.java <https://github.com/neo4j/community/blob/1.8/embedded-examples/src/main/java/org/neo4j/examples/EmbeddedNeo4j.java>`_

