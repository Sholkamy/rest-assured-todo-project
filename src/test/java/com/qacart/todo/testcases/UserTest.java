package com.qacart.todo.testcases;

import com.qacart.todo.apis.UserApis;
import com.qacart.todo.data.ErrorMessages;
import com.qacart.todo.models.Error;
import com.qacart.todo.models.User;
import com.qacart.todo.steps.UserSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("User Module")
public class UserTest {

	@Story("Should Be Able To Register")
	@Test
	public void shouldBeAbleToRegister() {
		User user = UserSteps.generateUser();

		Response response = UserApis.registration(user);
		User userResponse = response.body().as(User.class);

		assertThat(response.statusCode(), equalTo(201));
		assertThat(userResponse.getFirstName(), equalTo(user.getFirstName()));
	}

	@Test
	@Story("Should Not Be Able To Register With Same Email")
	public void shouldNotBeAbleToRegisterWithSameEmail() {
		User user = UserSteps.getRegisteredUser();

		Response response = UserApis.registration(user);
		Error returnedError = response.body().as(Error.class);

		assertThat(response.statusCode(), equalTo(400));
		assertThat(returnedError.getMessage(),equalTo(ErrorMessages.EMAIL_ALREADY_EXIST));
	}

	@Test
	@Story("Should Be Able To Login")
	public void shouldBeAbleToLogin() {
		User user = UserSteps.getRegisteredUser();
		User loginData = new User(user.getEmail(), user.getPassword());

		Response response = UserApis.login(loginData);
		User userResponse = response.body().as(User.class);

		assertThat(response.statusCode(),equalTo(200));
		assertThat(userResponse.getFirstName(), equalTo(user.getFirstName()));
		assertThat(userResponse.getAccessToken(), not(equalTo(null)));
	}

	@Test
	@Story("Should Be Able To Login With Invalid Password")
	public void shouldBeAbleToLoginWithInvalidPassword() {
		User user = UserSteps.getRegisteredUser();
		User loginData = new User(user.getEmail(), "wrongPassword");

		Response response = UserApis.login(loginData);
		Error returnedError = response.body().as(Error.class);

		assertThat(response.statusCode(),equalTo(401));
		assertThat(returnedError.getMessage(), equalTo(ErrorMessages.INVALID_CREDENTIALS));
	}
}
