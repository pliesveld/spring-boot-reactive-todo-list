package hello;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class StreamController {
    @GetMapping("/timestream")
    public Flux<ServerSentEvent<ServerTime>> time() {
        return Flux.interval(Duration.ofMillis(1000))
            .map(l -> {
               ServerTime st = new ServerTime(l, System.currentTimeMillis());
               return ServerSentEvent.builder(st).build();
            });
    }
}

@Data
@AllArgsConstructor
class ServerTime {
    long count;
    long time;
}
