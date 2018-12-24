package hello;

import hello.model.TodoList;
import hello.model.TodoListItem;
import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.is;


// TODO: http://toolsqa.com/rest-assured/post-request-using-rest-assured/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListIntegrationTest {

    @LocalServerPort
    private int port;

    private RequestSpecification request;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
        RestAssured.basePath = "";
        request = RestAssured.given();
    }

    @Test
    public void verifySwaggerHtml() {
        request.get("/swagger-ui.html").then().assertThat().statusCode(is(200));
    }

    @Test
    public void verifyListAll() {
        request.get("/list").then().statusCode(is(200));
    }

    @Test
    public void verifyListSingle() {
        request.get("/list/1").then().statusCode(is(200));
    }

    @Test
    public void verifyDeleteSingle() {
        request.delete("/list/1").then().statusCode(is(200));
    }

    @Test
    public void verifyPostSingle() {
        TodoList todoListNew = new TodoList();
        todoListNew.setName("New Post List");
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setStatus(TodoListItem.StatusEnum.PENDING);
        todoListItem.setName("New Item TODO");
        todoListNew.addItemsItem(todoListItem);

        request.contentType(ContentType.JSON).body(todoListNew).
                post("/list").then().statusCode(is(200));
    }

    @Test
    public void verifyPutSingle() {
        TodoList todoListNew = new TodoList();
        todoListNew.setName("New List");
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setStatus(TodoListItem.StatusEnum.PENDING);
        todoListItem.setName("New Item");
        todoListNew.addItemsItem(todoListItem);

        request.contentType(ContentType.JSON).body(todoListNew).
        put("/list/1").then().statusCode(is(200));
    }

    @Test
    public void verifyPutSingle_invalid() {
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setStatus(TodoListItem.StatusEnum.PENDING);
        todoListItem.setName("New Item");
        request.log().body(true).contentType(ContentType.JSON).body(todoListItem).
                put("/list/1").then().log().body(true).statusCode(is(400));
    }
}
