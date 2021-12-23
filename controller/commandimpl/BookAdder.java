package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.logging.Logging;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookAdder implements Command {
    @Override
    public String execute(String request) {
        Logger logger = Logging.getLogger();
        logger.log(Level.INFO, "request: {0}", request);
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        LibraryService libraryService = serviceFactory.getLibraryService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        String[] arguments = request.split(splitSymbol);
        Map<String,String> params = new HashMap<>();
        params.put("name", arguments[0]);
        params.put("author", arguments[1]);
        params.put("year", arguments[2]);
        params.put("pages", arguments[3]);
        try {
            libraryService.addBook(params);
            response = operationSuccessCode + "Book has been added";
        } catch (ServiceException e) {
            response = operationFailCode + e.getMessage() + "\nTry again";
            logger.log(Level.INFO, "Book adding exception \n Exception: {0}" +
                    "Request: {1} Response: {2}", new Object[]{e, request, response});
        }
        return response;
    }
}

