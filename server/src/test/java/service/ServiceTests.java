package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {

    @Test
    void registerPositive() throws DataAccessException {
        Service service = new Service();
        RegisterRequest given = new RegisterRequest("emma", "leslie", "email");

        RegisterResult actual = service.register(given);

        assertNotNull(actual);
    }

    @Test
    void registerNegative() throws DataAccessException {
        Service service = new Service();
        RegisterRequest given = new RegisterRequest("emma", "leslie", "email");
        service.register(given);
        RegisterResult actual = service.register(given);

        assertNull(actual);
    }


}