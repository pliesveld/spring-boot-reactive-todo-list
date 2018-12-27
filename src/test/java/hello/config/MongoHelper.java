package hello.config;

import hello.repository.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import test.TodoListGenerator;

@Component
public class MongoHelper {

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TodoListGenerator todoListGenerator;

    @EventListener
    public void onLoad(ContextRefreshedEvent contextRefreshedEvent) {
        Assert.notNull(todoListGenerator);
        Assert.notNull(todoListRepository);
    }
}
