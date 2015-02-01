package in.jedimaster.cli;

import org.junit.Assert;
import org.junit.Test;

public class AnnotatedParserTest {

  static class OptionSet1 {

    @Option(opt="arg1")
    String arg1;

    @Option(opt="arg2")
    short arg2;

    @Option(opt="arg3")
    long arg3;

    @Option(opt="arg4")
    int arg4;

    @Option(opt="arg5")
    boolean arg5;

    @Option(opt="arg6")
    float arg6;

    @Option(opt="arg7")
    double arg7;

    @Option(opt="arg8")
    char arg8;
  }

  @Test
  public void testDataTypes() throws Exception {

    String[] args = new String[]{"--arg1", "value1", "--arg2", "123", "--arg3", "112312413511", "--arg4", "11",
        "--arg5", "true", "--arg6", "123.4", "--arg7", "123.4", "--arg8", "C"};

    AnnotatedOptionParser parser = new AnnotatedOptionParser();
    OptionSet1 opt = parser.parse(OptionSet1.class, args);

    Assert.assertEquals("value1", opt.arg1);
    Assert.assertEquals(123, opt.arg2);
    Assert.assertEquals(112312413511L, opt.arg3);
    Assert.assertEquals(11, opt.arg4);
    Assert.assertTrue(opt.arg5);
    Assert.assertEquals(123.4, opt.arg6, 0.00001);
    Assert.assertEquals(123.4, opt.arg7, 0.00001);
    Assert.assertEquals('C', opt.arg8);
  }

  static class OptionSet2 {

    @Option(opt="arg1")
    String arg1;

    @Option(opt="arg2")
    String arg2;

  }

  @Test(expected=IllegalArgumentException.class)
  public void testMandatory() throws Exception {
    String[] args = new String[]{"--arg2", "123"};

    AnnotatedOptionParser parser = new AnnotatedOptionParser();
    OptionSet2 opt = parser.parse(OptionSet2.class, args);
  }

}
