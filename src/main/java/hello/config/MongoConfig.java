package hello.config;

import hello.repository.TodoListRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = TodoListRepository.class)
public class MongoConfig {
}
