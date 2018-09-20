package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/{id}")
    private Mono<Greeting> getGreetingById(@PathVariable String id) {
        return Mono.just(new Greeting(counter.incrementAndGet(), id));
    }

    @GetMapping
    private Flux<Greeting> getAllGreetingById() {
        return Flux.just(
                new Greeting(counter.incrementAndGet(), "flux"),
                new Greeting(counter.incrementAndGet(), "flux"),
                new Greeting(counter.incrementAndGet(), "flux"),
                new Greeting(counter.incrementAndGet(), "flux"),
                new Greeting(counter.incrementAndGet(), "flux")
        );
    }
}
