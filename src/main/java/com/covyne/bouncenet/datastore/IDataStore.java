package com.covyne.bouncenet.datastore;

import com.covyne.bouncenet.admin.RegisteredUser;

import java.io.IOException;
import java.util.Optional;

public interface IDataStore {
    void healthCheck() throws IOException;
    void createUser(RegisteredUser registeredUser);

    void deleteUser(RegisteredUser registeredUser);

    Optional<RegisteredUser> GetUser(String email);
}
