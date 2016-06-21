4.1. 将Neo4j引入到你的项目工程中
===============================================================

    在选择了适合你的平台的editions,edition后，只需要引入Neo4j的jars 文件到你的工程的构造路径中，你就可以在你的工程中使用Neo4j数据库了。下面的章节将展示如何完成引入，要么通过直接改变构造路径，要么使用包依赖管理。
    
4.1.1. 增加Neo4j的库文件到构造路径中
---------------------------------------------------------------------------

    可以通过下面任意一种方式得到需要的jar文件:
    
    - 解压 Neo4j 下载的压缩包，我们需要使用的jars文件都包括在lib目录中。 
    - 直接使用Maven中心仓库的jars文件。
    
    将jars引入到你的项目工程中:
    
    JDK tools
    
        增加到 -classpath 中
        
    Eclipse
    
    - 右键点击工程然后选择 Build Path → Configure Build Path 。在对话框中选择 Add External JARs ，浏览到Neo4j的'lib/'目录并选择所有的jar文件。 
    - 另外一种方式是使用 User Libraries。
    
    IntelliJ IDEA
    
        看 Libraries, Global Libraries, and the Configure Library dialog了解详情。
        
    NetBeans
    
    - 在工程的 Libraries 点击鼠标右键，选择 Add JAR/Folder ，浏览到Neo4j的'lib/'目录选择里面的所有jar文件。 
    - 你也可以从工程节点来管理库文件。详细情况请查看管理一个工程的classpath。

4.1.2. 将Neo4j作为一个依赖添加
----------------------------------------------------------

    想总览一下主要的Neo4j构件，请查看editions。列在里面的构件都是包含实际Neo4j实现的顶级构件。
    
    你既可以使用顶级构件也可以直接引入单个的组件。在这的范例使用的是顶级构件的方式。
    
    Maven
    
        ``Maven dependency.``
        
    .. literalinclude:: maven.xml
        :language: xml
        :linenos:
        
    参数 ``artifactId`` 可以在 `editions` 找到。

    
    
    
    