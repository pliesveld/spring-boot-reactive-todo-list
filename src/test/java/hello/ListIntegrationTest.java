package hello;

import hello.config.MongoHelper;
import hello.model.TodoList;
import hello.model.TodoListItem;
import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import test.TodoListGenerator;
import test.TodoListGeneratorProperties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//TODO: https://www.testingexcellence.com/parse-json-response-rest-assured/
//TODO: https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html
//TODO: https://github.com/rest-assured/rest-assured/blob/master/json-path/src/test/java/io/restassured/path/json/JsonPathTest.java

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TodoListGenerator.class, TodoListGeneratorProperties.class})
public class ListIntegrationTest {

    @LocalServerPort
    private int port;

    private RequestSpecification request;

    private String GET_LIST_ID;
    private String DELETE_LIST_ID;
    private String PUT_LIST_ID;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
        RestAssured.basePath = "";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.defaultParser = Parser.JSON;
        request = RestAssured.given().accept(ContentType.JSON);

        GET_LIST_ID = MongoHelper.LIST1_ID;
        DELETE_LIST_ID = MongoHelper.LIST2_ID;
        PUT_LIST_ID = MongoHelper.LIST3_ID;
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
        request
            .given()
            .pathParam("listId", GET_LIST_ID)
            .get("/list/{listId}")
        .then()
            .statusCode(is(200))
            .body("id", is(GET_LIST_ID))
            .body("name", is("MyList"))
            .body("items", hasSize(greaterThan(0)))
            .body("items[0].name", is("Task One"))
            .appendRoot("items")
            .body(".name", containsInAnyOrder(
                    "Task One",
                    "Task Two",
                    "Task Three",
                    "Task Four"
                )
            )
        ;
    }

    @Test
    public void verifyDeleteSingle() {

        request
            .given()
            .pathParam("listId", DELETE_LIST_ID)
            .get("/list/{listId}")
        .then()
            .statusCode(is(200))
            .body("id", is(DELETE_LIST_ID))
            .body("name", is("DeleteList"))
            .body("items", hasSize(greaterThan(0)))
            .body("items[0].name", is("Task One"))
            .appendRoot("items")
            .body(".name", containsInAnyOrder(
                    "Task One",
                    "Task Two",
                    "Task Three",
                    "Task Four"
                    )
            )
        ;

        request.delete("/list/{listId}").then().statusCode(is(200));

        request
            .given()
            .pathParam("listId", DELETE_LIST_ID)
            .get("/list/{listId}")
        .then()
            .statusCode(isOneOf(200,204))
            .body(is(isEmptyOrNullString()));
    }

    @Test
    public void verifyPostSingle() {

        int previous_count = request.get("/list/count").thenReturn().as(Integer.class);

        TodoList todoListNew = new TodoList();
        todoListNew.setName("New Post List");
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setStatus(TodoListItem.StatusEnum.PENDING);
        todoListItem.setName("New Item TODO");
        todoListNew.addItemsItem(todoListItem);

        request
            .contentType(ContentType.JSON)
            .body(todoListNew)
            .post("/list")
        .then()
            .statusCode(is(200));

        int new_count = request.get("/list/count").thenReturn().as(Integer.class);
        assertThat(previous_count, lessThan(new_count));
    }

    @Test
    public void verifyPutSingle() {

        TodoList todoListOrig =

        request
            .given()
            .pathParam("listId", PUT_LIST_ID)
            .get("/list/{listId}")
        .then()
            .statusCode(is(200))
            .body("id", is(PUT_LIST_ID))
            .body("name", is("ModifyList"))
            .body("items", hasSize(greaterThan(0)))
            .body("items[0].name", is("Task One"))
            .appendRoot("items")
            .body(".name", containsInAnyOrder(
                    "Task One",
                    "Task Two",
                    "Task Three",
                    "Task Four"
                    )
            )
        .extract().response().as(TodoList.class);

        todoListOrig.addItemsItem(new TodoListItem().name("Task Five").status(TodoListItem.StatusEnum.PENDING));

        request
            .contentType(ContentType.JSON).body(todoListOrig)
            .pathParam("listId", PUT_LIST_ID)
            .put("/list/{listId}")
        .then()
            .statusCode(is(200))
                .body("id", is(PUT_LIST_ID))
                .body("name", is("ModifyList"))
                .body("items", hasSize(greaterThan(0)))
                .body("items[0].name", is("Task One"))
                .appendRoot("items")
                .body(".name", containsInAnyOrder(
                        "Task One",
                        "Task Two",
                        "Task Three",
                        "Task Four",
                        "Task Five"
                        )
                )
        ;
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
