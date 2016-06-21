.. _chapter4_1_index:

4.1. 将Neo4j引入到你的项目工程中
===============================================================

    在选择了适合你的平台的editions,edition后，只需要引入Neo4j的jars 文件到你的工程的构造路径中，你就可以在你的工程中使用Neo4j数据库了。下面的章节将展示如何完成引入，要么通过直接改变构造路径，要么使用包依赖管理。
    
4.1.1. 增加Neo4j的库文件到构造路径中
---------------------------------------------------------------------------

    可以通过下面任意一种方式得到需要的jar文件:
    
    - 解压 Neo4j 下载的压缩包，我们需要使用的jars文件都包括在lib目录中。 
    - 直接使用Maven中心仓库的jars文件。
    
    将jars引入到你的项目工程中:
    
    **JDK tools**
    
        增加到 -classpath 中
        
    **Eclipse**
    
    - 右键点击工程然后选择 Build Path → Configure Build Path 。在对话框中选择 Add External JARs ，浏览到Neo4j的'lib/'目录并选择所有的jar文件。 
    - 另外一种方式是使用 User Libraries。
    
    **IntelliJ IDEA**
    
        看 Libraries, Global Libraries, and the Configure Library dialog了解详情。
        
    **NetBeans**
    
    - 在工程的 Libraries 点击鼠标右键，选择 Add JAR/Folder ，浏览到Neo4j的'lib/'目录选择里面的所有jar文件。 
    - 你也可以从工程节点来管理库文件。详细情况请查看管理一个工程的classpath。

4.1.2. 将Neo4j作为一个依赖添加
----------------------------------------------------------

    想总览一下主要的Neo4j构件，请查看editions。列在里面的构件都是包含实际Neo4j实现的顶级构件。
    
    你既可以使用顶级构件也可以直接引入单个的组件。在这的范例使用的是顶级构件的方式。
    
    **Maven**
    
        `Maven dependency.`
        
    .. literalinclude:: maven.xml
        :language: xml
        :linenos:
        
    参数 ``artifactId`` 可以在 `editions` 找到。

    **Eclipse and Maven**
    
        在Eclipse中开发，推荐安装插件 `m2e plugin` 让Maven管理classpath来代替上面的方案。
        
        这样的话，你既可以通过Maven命令行来编译你的工程，也可以通过Maven命令自动生成一个Eclipse工作环境以便进行开发。

    **Ivy**

        确保能解决来自Maven Central的依赖问题，比如我们在你的 `ivysettings.xml` 文件中使用下面的配置选项：
        
        .. literalinclude:: ivysettings.xml
            :language: xml
            :linenos:
            
        有了这个，你就可以通过增加下面这些内容到你的 `ivy.xml` 中来引入Neo4j：
        
        .. literalinclude:: ivy.xml
            :language: xml
            :linenos:
    
        参数 ``name`` 可以在`editions`找到。
    
    **Gradle**

        下面的范例演示了用Gradle生成一个脚本来引入Neo4j库文件。
        
        .. literalinclude:: gradle.java
            :language: javascript
            :linenos:

        参数 ``coordinates`` (在范例中的 ``org.neo4j:neo4j`` ) 可以在 `editions` 找到。
        
        
4.1.3. 启动和停止
-----------------------------------

    为了创建一个新的数据库或者打开一个已经存在的，你需要实例化一个 `EmbeddedGraphDatabase` 对象：
    
    .. literalinclude:: EmbeddedGraphDatabase.java
        :language: javascript
        :linenos:
            
    `EmbeddedGraphDatabase` 实例可以在多个线程中共享。然而你不能创建多个实例来指向同一个数据库。

    为了停止数据库，你需要调用方法 `shutdown()` ：
    
    .. code-block:: javascript
        :linenos:
        
        graphDb.shutdown();
        
    为了确保Neo4j被正确关闭，你可以为它增加一个关闭钩子方法：
    
    .. code-block:: javascript
        :linenos:
        
        private static void registerShutdownHook( final GraphDatabaseService graphDb ) { 
            // Registers a shutdown hook for the Neo4j instance so that it 
            // shuts down nicely when the VM exits (even if you "Ctrl-C" the 
            // running example before it's completed) 
            Runtime.getRuntime().addShutdownHook( new Thread() 
            { 
                @Override
                public void run() 
                { 
                    graphDb.shutdown(); 
                } 
            } ); 
        }

    如果你只想通过 只读方式 浏览数据库，请使用 `EmbeddedReadOnlyGraphDatabase` 。

    想通过配置设置来启动Neo4j，一个Neo4j属性文件可以像下面这样加载：
    
    .. code-block:: javascript
        :linenos:
        
        GraphDatabaseService graphDb = new GraphDatabaseFactory(). 
            newEmbeddedDatabaseBuilder( "target/database/location" ). 
            loadPropertiesFromFile( pathToConfig + "neo4j.properties" ). 
            newGraphDatabase();
        
    或者你可以编程创建你自己的 `Map<String, String>` 来代替。

    想了解更多配置设置的细节，请参考: `embedded-configuration` 。





