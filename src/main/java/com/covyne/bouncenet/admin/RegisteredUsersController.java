package com.covyne.bouncenet.admin;

import com.covyne.bouncenet.datastore.IDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class RegisteredUsersController {

    @Autowired
    private IDataStore dataStore;

    private static final String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @GetMapping("/users/{email}")
    public GetUserResponse getUser(@PathVariable String email, HttpServletResponse response){

        final GetUserResponse getUserResponse = new GetUserResponse();
        final Optional<RegisteredUser> registeredUser = dataStore.GetUser(email);
        registeredUser.ifPresent(user -> getUserResponse.setEmail(user.getEmail()));
        registeredUser.ifPresent(user -> getUserResponse.setMessage("Success"));

        if(registeredUser.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return  getUserResponse;

    }

    @DeleteMapping("/users/{email}")
    public void deleteUser(@PathVariable String email, HttpServletResponse response){

        final Optional<RegisteredUser> registeredUser = dataStore.GetUser(email);
        registeredUser.ifPresent(dataStore::deleteUser);
        final Optional<RegisteredUser> deletedUser = dataStore.GetUser(email);
        deletedUser.ifPresent(user -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @PostMapping("/users/create")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletResponse response){

        final CreateUserResponse createUserResponse = new CreateUserResponse();

        if(!createUserRequest.getEmail().matches(regexEmail)){
            createUserResponse.setMessage(String.format("Invalid email address %s.", createUserRequest.getEmail()));
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return createUserResponse;
        }

        final Optional<RegisteredUser> registeredUser = dataStore.GetUser(createUserRequest.getEmail());

        if(registeredUser.isPresent()){
            createUserResponse.setMessage(String.format("User %s exists.",createUserRequest.getEmail()));
            response.setStatus(HttpStatus.CONFLICT.value());
            return createUserResponse;
        }

        final RegisteredUser newUser = new RegisteredUser();
        newUser.setEmail(createUserRequest.getEmail());
        dataStore.createUser(newUser);
        createUserResponse.setMessage(String.format("User %s created.",createUserRequest.getEmail()));
        return createUserResponse;

    }





}
