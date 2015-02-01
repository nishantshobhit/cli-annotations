# cli-annotations
Annotation driven command line argument parser

Simply describe your options using field annotations in a class and use them directly in code without worrying about how to parse them

<pre><code>
  class Options {
 
    @Option(opt="host", shortOpt="h", desc="database host")
    private String host;
    
    @Option(opt="post", shortOpt="p", desc="database port")
    private int port;
    
    @Option(opt="dbname", shortOpt="db", desc="database")
    private String dbName;
    
    @Option(opt="session", shortOpt="s", desc="database session to use", required=false)
    private String session;
    
  }
  
  ...
  class Driver {
    public static void main(String[] args) {
      AnnotatedOptionParser parser = new AnnotatedOptionParser();
      Options opt = parser.parse(Options.class, args);
      ...
    }
  }
</code></pre>
