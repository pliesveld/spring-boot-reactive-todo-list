package hello.repository;

import hello.model.TodoList;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends ReactiveCrudRepository<TodoList, String> {


}
