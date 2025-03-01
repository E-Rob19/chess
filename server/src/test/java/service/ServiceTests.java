package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {

    @Test
    void registerPositive() throws DataAccessException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");

        RegisterResult actual = service.register(req);

        assertNotNull(actual);
    }

    @Test
    void registerNegative() throws DataAccessException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        service.register(req);
        RegisterResult actual = service.register(req);

        assertNull(actual);
    }


}