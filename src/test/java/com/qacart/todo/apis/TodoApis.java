package com.qacart.todo.apis;

import com.qacart.todo.base.Specs;
import com.qacart.todo.data.Route;
import com.qacart.todo.models.Todo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TodoApis {
    public static Response addTodo(Todo todo, String token) {
        return given()
                .spec(Specs.getRequestSpecs())
                .body(todo)
                .auth().oauth2(token)
                .when().post(Route.TODOS_ROUTE)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getAllTodos(String token) {
        return given()
                .spec(Specs.getRequestSpecs())
                .auth().oauth2(token)
                .when().get(Route.TODOS_ROUTE)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getTodoById(String token, String taskID) {
        return given()
                .spec(Specs.getRequestSpecs())
                .auth().oauth2(token)
                .when().get(Route.TODOS_ROUTE + "/" + taskID)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response updateATodo(Todo todo, String token, String taskID) {
        return given()
                .spec(Specs.getRequestSpecs())
                .body(todo)
                .auth().oauth2(token)
                .when().put(Route.TODOS_ROUTE + "/"+ taskID)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response deleteATodo(String token, String taskID) {
        return given()
                .spec(Specs.getRequestSpecs())
                .auth().oauth2(token)
                .when().delete(Route.TODOS_ROUTE + "/" + taskID)
                .then()
                .log().all()
                .extract().response();
    }
}
