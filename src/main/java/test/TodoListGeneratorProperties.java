package test;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "list.generator")
@Data
@Getter
public class TodoListGeneratorProperties {
    private int min = 5;
    private int max = 30;
    private int count = 3000;
}
