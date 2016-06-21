.. _chapter4_6_index:

4.6. 领域实体
==================================
    
    这个地方演示了当使用Neo4j时控制领域实体的一个方法。使用的原则是将实体封装到节点上（这个方法也可以用在关系上）。
 
    **提示**
        
        范例源代码下载地址： `Person.java <https://github.com/neo4j/community/blob/1.8/embedded-examples/src/main/java/org/neo4j/examples/socnet/Person.java>`_ 

    马上，保存节点并且让它在包里可以被访问：
    
    .. code-block:: python
        :linenos:
        
        private final Node underlyingNode; 
        Person( Node personNode ) { 
            this.underlyingNode = personNode; 
        } 
        protected Node getUnderlyingNode() { 
            return underlyingNode; 
        }
        
    分配属性给节点：
    
    .. code-block:: python
        :linenos:
        
        public String getName() { 
            return (String)underlyingNode.getProperty( NAME ); 
        }
        
    确保重载这些方法：
    
    .. code-block:: python
        :linenos:
        
        @Override
        public int hashCode() { 
            return underlyingNode.hashCode(); 
        } 
          
        @Override
        public boolean equals( Object o ) { 
            return o instanceof Person && 
                    underlyingNode.equals( ( (Person)o ).getUnderlyingNode() ); 
        } 
          
        @Override
        public String toString() { 
            return "Person[" + getName() + "]"; 
        }

    


