package com.runningmanstudios.sinforgiver.commandUtil;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandBuilder {
    String name();
    String description();
    String usage() default "";
    String[] aliases() default {""};
}
