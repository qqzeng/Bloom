package com.qqzeng.bloom.server.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by qqzeng.
 * </p>
 * Annotation of API exported by RPC server.
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BloomService {

    /**
     * Service full name.
     * @return
     */
    Class<?> value();

    /**
     * Service version.
     * @return
     */
    String version() default "";
}
