package test;

import hello.config.MongoConfig;
import hello.model.TodoList;
import hello.model.TodoListItem;
import hello.repository.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableAutoConfiguration(exclude = WebFluxAutoConfiguration.class)
@Import({MongoConfig.class})
public class TodoListGenerator implements CommandLineRunner {

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

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Invalid command. ");
            System.out.println("Commands: ");
            System.out.println("\tCLEAR\tDrop collection");
            System.out.println("\tGENERATE\tGenerate test data");
            System.out.println("\tCOUNT\tConnect to database and count number of documents");
            System.exit(1);
        }

        new SpringApplicationBuilder()
                .logStartupInfo(true)
                .main(TodoListGenerator.class)
                .headless(true)
                .profiles("generator")
                .web(WebApplicationType.NONE)
                .sources(MongoConfig.class)
                .sources(TodoListGenerator.class)
                .sources(TodoListGeneratorProperties.class)
                .addCommandLineProperties(true)
                .registerShutdownHook(true)
                .build().run(args);

        System.out.println("Finished.");
        System.exit(0);
    }

    @Override
    public void run(String... args) throws Exception {

        if (args.length == 0) {
            System.out.println("Invalid arugment.");
            System.exit(1);
        }

        Consumer<String> clearCmd = cmd -> {
            System.out.println("Dropping collection");
            todoListRepository.deleteAll().block();
        };

        Consumer<String> generateCmd = cmd -> {
            int count = todoListGeneratorProperties.getCount();
            System.out.println("Generating data... count: " + count);
            Stream.generate(todoListSupplier).limit(count).parallel().map(todoListRepository::save).forEach(Mono::block);
        };

        Consumer<String> countCmd = cmd -> {
            System.out.println("found " + todoListRepository.count().log().block());
        };

        Consumer<String> envCmd = cmd -> {
            System.out.println(todoListGeneratorProperties);
        };

        for (String arg : args) {
            switch (arg) {
                case "CLEAR":
                    clearCmd.accept(arg);
                    break;
                case "GENERATE":
                    generateCmd.accept(arg);
                    break;
                case "COUNT":
                    countCmd.accept(arg);
                    break;
                case "ENV":
                    envCmd.accept(arg);
                    break;
                default:
                    System.out.println("Unknown command: " + arg);
                    System.exit(-2);
            }
        }
    }
}
