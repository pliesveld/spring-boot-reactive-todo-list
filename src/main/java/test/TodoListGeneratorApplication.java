package test;

import hello.config.MongoConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Configuration
@EnableAutoConfiguration(exclude = WebFluxAutoConfiguration.class)
@Import({MongoConfig.class, TodoListGenerator.class})
public class TodoListGeneratorApplication implements CommandLineRunner {

    private final TodoListGenerator todoListGenerator;

    public TodoListGeneratorApplication(TodoListGenerator todoListGenerator) {
        this.todoListGenerator = todoListGenerator;
    }

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
                .main(TodoListGeneratorApplication.class)
                .headless(true)
                .profiles("generator")
                .web(WebApplicationType.NONE)
                .sources(MongoConfig.class)
                .sources(TodoListGeneratorApplication.class)
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

            todoListGenerator.getTodoListRepository().deleteAll().block();
        };

        Consumer<String> generateCmd = cmd -> {
            int count = todoListGenerator.getTodoListGeneratorProperties().getCount();
            System.out.println("Generating data... count: " + count);
            Stream.generate(todoListGenerator.getTodoListSupplier()).limit(count).parallel().map(todoListGenerator.getTodoListRepository()::save).forEach(Mono::block);
        };

        Consumer<String> countCmd = cmd -> {
            System.out.println("found " + todoListGenerator.getTodoListRepository().count().log().block());
        };

        Consumer<String> envCmd = cmd -> {
            System.out.println(todoListGenerator.getTodoListGeneratorProperties());
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
