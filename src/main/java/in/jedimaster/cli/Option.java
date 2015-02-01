package in.jedimaster.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {

    String opt();

    String shortOpt() default "";

    String desc() default "";

    boolean required() default true;

}
