def neo4jVersion = "1.8"
apply plugin: 'java'
repositories { 
   mavenCentral() 
} 
dependencies { 
   compile "org.neo4j:neo4j:${neo4jVersion}"
}
