package hello.config;

import hello.model.TodoList;
import hello.model.TodoListItem;
import hello.repository.TodoListRepository;
import org.assertj.core.util.Lists;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import test.TodoListGenerator;

import java.util.UUID;

@Component
public class MongoHelper {

    public static String LIST1_ID = UUID.randomUUID().toString();

    public static String LIST2_ID = UUID.randomUUID().toString();

    public static String LIST3_ID = UUID.randomUUID().toString();

    public static String LIST1_NAME = "MyList";

    public static String LIST2_NAME = "DeleteList";

    public static String LIST3_NAME = "ModifyList";

    private final TodoListRepository todoListRepository;

    private final TodoListGenerator todoListGenerator;

    public MongoHelper(TodoListRepository todoListRepository, TodoListGenerator todoListGenerator) {
        this.todoListRepository = todoListRepository;
        this.todoListGenerator = todoListGenerator;
    }

    @EventListener
    public void onLoad(ContextRefreshedEvent contextRefreshedEvent) {
        TodoListItem[] todoListItems = {
                new TodoListItem().name("Task One").status(TodoListItem.StatusEnum.PENDING),
                new TodoListItem().name("Task Two").status(TodoListItem.StatusEnum.PENDING),
                new TodoListItem().name("Task Three").status(TodoListItem.StatusEnum.PENDING),
                new TodoListItem().name("Task Four").status(TodoListItem.StatusEnum.COMPLETED)
        };
        todoListRepository.save(new TodoList().id(LIST1_ID).name(LIST1_NAME).items(Lists.newArrayList(todoListItems))).block();
        todoListRepository.save(new TodoList().id(LIST2_ID).name(LIST2_NAME).items(Lists.newArrayList(todoListItems))).block();
        todoListRepository.save(new TodoList().id(LIST3_ID).name(LIST3_NAME).items(Lists.newArrayList(todoListItems))).block();
    }
}
