package hello.controller;

import hello.model.TodoList;
import hello.model.TodoListItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/list")
public class ListController {

    private static TodoList todoList;

    static {
        todoList = new TodoList();
        todoList.setId(1L);
        todoList.setName("Example TODO list");
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setName("Take a walk");
        todoListItem.setStatus(TodoListItem.StatusEnum.PENDING);
        todoList.addItemsItem(todoListItem);
    }

    @GetMapping("/{listId}")
    private Mono<TodoList> getListById(@PathVariable String listId) {
        return Mono.just(todoList);

    }

    @GetMapping("/")
    private Flux<TodoList> getAllLists() {
        return Flux.just(todoList);
    }


}
