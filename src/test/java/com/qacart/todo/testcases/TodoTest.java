package com.qacart.todo.testcases;

import com.qacart.todo.apis.TodoApis;
import com.qacart.todo.data.ErrorMessages;
import com.qacart.todo.models.Error;
import com.qacart.todo.models.Tasks;
import com.qacart.todo.models.Todo;
import com.qacart.todo.steps.TodoSteps;
import com.qacart.todo.steps.UserSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Feature("Todo Module")
public class TodoTest {

    @Test
    @Story("Should Be Able To Add Todo")
    public void shouldBeAbleToAddTodo() {
        String token = UserSteps.getUserToken();
        Todo todo = TodoSteps.generateTodo();

        Response response = TodoApis.addTodo(todo, token);
        Todo todoResponse = response.body().as(Todo.class);

        assertThat(response.statusCode(),equalTo(201));
        assertThat(todoResponse.getIsCompleted(),equalTo(todo.getIsCompleted()));
        assertThat(todoResponse.getItem(), equalTo(todo.getItem()));
    }

    @Test
    @Story("Should Be Not Able To Add Todo Without Completed")
    public void shouldBeNotAbleToAddTodoWithoutCompleted() {
        String token = UserSteps.getUserToken();
        Todo todo = new Todo("Learn Appium");

        Response response = TodoApis.addTodo(todo, token);
        Error returnedError = response.body().as(Error.class);

        assertThat(response.statusCode(), equalTo(400));
        assertThat(returnedError.getMessage(), equalTo(ErrorMessages.IS_COMPLETED_IS_REQUIRED));
    }

    @Test
    @Story("Should Be Able To Get All Todos")
    public void shouldBeAbleToGetAllTodos() {
        String token = UserSteps.getUserToken();
        Todo todo = TodoSteps.generateTodo();
        TodoSteps.getTodoList(token, todo);

        Response response = TodoApis.getAllTodos(token);
        Tasks TasksResponse = response.body().as(Tasks.class);
        List<Todo> tasks = TasksResponse.getTasks();

        assertThat(response.statusCode(), equalTo(200));
        assertThat(tasks.get(tasks.size() - 1).getItem(), equalTo(todo.getItem()));
    }

    @Test
    @Story("Should Be Able To Get A Todo By Id")
    public void shouldBeAbleToGetATodoById() {
        String token = UserSteps.getUserToken();
        Todo todo = TodoSteps.generateTodo();
        String taskID = TodoSteps.getTaskId(token, todo);

        Response response = TodoApis.getTodoById(token, taskID);
        Todo returnedTodo = response.body().as(Todo.class);

        assertThat(response.statusCode(), equalTo(200));
        assertThat(returnedTodo.getIsCompleted(), equalTo(todo.getIsCompleted()));
        assertThat(returnedTodo.getItem(), equalTo(todo.getItem()));
    }

    @Test
    @Story("Should Be Able To Update A Todo")
    public void shouldBeAbleToUpdateATodo() {
        String token = UserSteps.getUserToken();
        Todo firstTodo = TodoSteps.generateTodo();
        Todo secondTodo = TodoSteps.generateTodo();
        String taskID = TodoSteps.getTaskId(token, firstTodo);

        Response response = TodoApis.updateATodo(secondTodo, token, taskID);
        Todo returnedTodo = response.body().as(Todo.class);

        assertThat(response.statusCode(), equalTo(200));
        assertThat(returnedTodo.getIsCompleted(),equalTo(secondTodo.getIsCompleted()));
        assertThat(returnedTodo.getItem(), equalTo(secondTodo.getItem()));
    }

    @Test
    @Story("should Be Able To Delete A Todo")
    public void shouldBeAbleToDeleteATodo() {
        String token = UserSteps.getUserToken();
        Todo todo = TodoSteps.generateTodo();
        String taskID = TodoSteps.getTaskId(token, todo);

        Response response = TodoApis.deleteATodo(token, taskID);
        Todo returnedTodo = response.body().as(Todo.class);

        assertThat(response.statusCode(), equalTo(200));
        assertThat(returnedTodo.getIsCompleted(), equalTo(todo.getIsCompleted()));
        assertThat(returnedTodo.getItem(), equalTo(todo.getItem()));
    }
}
