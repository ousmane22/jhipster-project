package com.groupeisi.ms2;

import com.groupeisi.ms2.config.AsyncSyncConfiguration;
import com.groupeisi.ms2.config.EmbeddedSQL;
import com.groupeisi.ms2.config.JacksonConfiguration;
import com.groupeisi.ms2.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Ms2App.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
