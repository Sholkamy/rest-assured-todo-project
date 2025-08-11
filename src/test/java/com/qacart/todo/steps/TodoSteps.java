package com.qacart.todo.steps;

import com.github.javafaker.Faker;
import com.qacart.todo.apis.TodoApis;
import com.qacart.todo.models.Todo;
import io.restassured.response.Response;

public class TodoSteps {
    public static Todo generateTodo() {
        Faker faker = new Faker();
        String item = faker.book().title();

        return new Todo(false, item);
    }

    public static String getTaskId(String token, Todo todo) {
        Response response = TodoApis.addTodo(todo, token);
        return response.body().path("_id");
    }

    public static void getTodoList(String token, Todo todo) {
        Todo todo1 = generateTodo();
        TodoApis.addTodo(todo1, token);
        TodoApis.addTodo(todo, token);
    }


}
