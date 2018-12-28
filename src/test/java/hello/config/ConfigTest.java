package hello.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class ConfigTest {
    @Test
    public void verifyWebfluxConfig() throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(WebfluxConfig.class);
        applicationContext.refresh();
        assertThat(applicationContext.getBean(WebfluxConfig.class),notNullValue());
        assertThat(applicationContext.getBean(WebfluxConfig.class).getValidator(),nullValue());
        assertThat(applicationContext.getBean(WebfluxConfig.class).getMessageCodesResolver(),nullValue());
    }

    @Test
    public void verifySwaggerConfig() throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(WebFluxAutoConfiguration.WebFluxConfig.class);
        applicationContext.register(SwaggerConfig.class);
        applicationContext.refresh();
        assertThat(applicationContext.getBean(SwaggerConfig.class),notNullValue());
    }
}
