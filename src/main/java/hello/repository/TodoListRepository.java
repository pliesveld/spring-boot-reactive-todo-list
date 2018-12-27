package hello.repository;

import hello.model.TodoList;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TodoListRepository extends ReactiveCrudRepository<TodoList, String> {
    @Query(value = "{}", fields = "{name : 1, _id : 1}")
    Flux<TodoList> findNameAndExcludeId();
}
