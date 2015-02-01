package in.jedimaster.cli;

import org.apache.commons.cli.*;
import java.lang.reflect.Field;

public class AnnotatedOptionParser {

  private Parser parser;

  public AnnotatedOptionParser() {
    this(new PosixParser());
  }

  public AnnotatedOptionParser(Parser parser) {
    this.parser = parser;
  }

  public <T> T parse(Class<T> clazz, String[] args) throws Exception {

    // build options first
    Options opts = new Options();
    opts.addOption("h", "help", false, "Show this help message and exit");
    Field[] fields = clazz.getDeclaredFields();
    for (Field f : fields) {
      if (f.isAnnotationPresent(Option.class)) {
        Option anno = f.getAnnotation(Option.class);
        // use long option if short ones are not available
        String shortOpt = (anno.shortOpt() == null) ? anno.opt() : anno.shortOpt();
        opts.addOption(shortOpt, anno.opt(), anno.required(), anno.desc());
      }
    }

    // parse
    CommandLine cmd = parser.parse(opts, args);
    if (cmd.hasOption('h')) {
      HelpFormatter h = new HelpFormatter();
      h.printHelp("help", opts);
      System.exit(-1);
    }

    // set properties
    T confObject = clazz.newInstance();
    for (Field f : fields) {
      if (f.isAnnotationPresent(Option.class)) {
        Option anno = f.getAnnotation(Option.class);
        String strVal = cmd.getOptionValue(anno.opt());
        if (strVal == null) {
          // if none is available and the annotation is mandatory, error out
          if (anno.required()) {
            throw new IllegalArgumentException("required argument " + anno.opt() + " not supplied");
          }
        } else {
          // otherwise attempt to coerce it in the appropriate type
          Object val;
          if (f.getType().equals(String.class)) {
            val = strVal;
          } else {
            val = coerceToType(f.getType().getName(), strVal);
          }
          f.set(confObject, val);
        }
      }
    }
    return confObject;
  }

  protected Object coerceToType(String type, String strVal) {
    switch (type) {
      case "boolean":
        return Boolean.parseBoolean(strVal);
      case "char":
        return strVal.charAt(0);
      case "int":
        return Integer.parseInt(strVal);
      case "short":
        return Short.parseShort(strVal);
      case "long":
        return Long.parseLong(strVal);
      case "float":
        return Float.parseFloat(strVal);
      case "double":
        return Double.parseDouble(strVal);
      default:
        return null;
    }
  }
}
