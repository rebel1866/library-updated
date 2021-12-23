package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.logging.Logging;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAdder implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ClientService clientService = serviceFactory.getClientService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        Logger logger = Logging.getLogger();
        String[] arguments = request.split(splitSymbol);
        Map<String, String> params = new HashMap<>();
        params.put("login", arguments[0]);
        params.put("password", arguments[1]);
        params.put("first_name", arguments[2]);
        params.put("last_name", arguments[3]);
        params.put("email", arguments[4]);
        params.put("user_role", arguments[5]);
        try {
            clientService.addUser(params);
            response = operationSuccessCode + "User has been added";
        } catch (ServiceException e) {
            response = operationFailCode + e.getMessage() + "\nTry again";
            logger.log(Level.INFO, "User adding failure: {0}\n" +
                    "request: {1} response: {2}", new Object[]{e, request, response});
        }
        return response;
    }
}
