package test;

import hello.model.TodoList;
import hello.model.TodoListItem;
import hello.repository.TodoListRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Getter
public class TodoListGenerator {

    private SentenceGenerator sentenceGenerator = new SentenceGenerator();

    private final TodoListRepository todoListRepository;

    private final TodoListGeneratorProperties todoListGeneratorProperties;

    @Autowired
    public TodoListGenerator(TodoListRepository todoListRepository, TodoListGeneratorProperties todoListGeneratorProperties) {
        this.todoListRepository = todoListRepository;
        this.todoListGeneratorProperties = todoListGeneratorProperties;

        todoListSupplier = () -> {
            TodoList todoList = new TodoList();
            todoList.setName(UUID.randomUUID().toString());
            int min = todoListGeneratorProperties.getMin();
            int max = todoListGeneratorProperties.getMax();
            int listSize = ThreadLocalRandom.current().nextInt(min, max);
            todoList.setItems(Stream.generate(todoListItemSupplier::get).limit(listSize).collect(Collectors.toList()));
            return todoList;
        };
    }

    private Supplier<TodoListItem> todoListItemSupplier = () -> {
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setName(sentenceGenerator.get());
        todoListItem.setStatus(TodoListItem.StatusEnum.PENDING);
        return todoListItem;
    };

    private Supplier<TodoList> todoListSupplier;

    @PostConstruct
    public void onLoad() {
        todoListSupplier = () -> {
            TodoList todoList = new TodoList();
            todoList.setName(UUID.randomUUID().toString());
            int min = todoListGeneratorProperties.getMin();
            int max = todoListGeneratorProperties.getMax();
            int listSize = ThreadLocalRandom.current().nextInt(min, max);
            todoList.setItems(Stream.generate(todoListItemSupplier::get).limit(listSize).collect(Collectors.toList()));
            return todoList;
        };
    }
}
