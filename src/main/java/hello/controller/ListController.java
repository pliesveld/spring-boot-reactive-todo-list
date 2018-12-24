package hello.controller;

import hello.model.TodoList;
import hello.repository.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private TodoListRepository todoListRepository;

    @GetMapping
    private Flux<TodoList> getAllLists() {
        return todoListRepository.findAll();
    }

    @PostMapping
    private Mono<?> updateListById(@Valid @RequestBody TodoList list) {
        list.setId(null);
        return todoListRepository.save(list);
    }

    @PutMapping("/{listId}")
    private Mono<?> updateListById(@PathVariable String listId, @Valid @RequestBody TodoList todoList) {
        todoList.setId(listId);
        return todoListRepository.save(todoList);
    }

    @GetMapping("/{listId}")
    private Mono<TodoList> getListById(@PathVariable String listId) {
        return todoListRepository.findById(listId);
    }

    @DeleteMapping("/{listId}")
    private Mono<?> deleteListById(@PathVariable String listId) {
        return todoListRepository.deleteById(listId);
    }

}
