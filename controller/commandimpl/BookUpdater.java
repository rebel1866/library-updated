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

public class BookUpdater implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        LibraryService libraryService = serviceFactory.getLibraryService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        Logger logger = Logging.getLogger();
        String[] params = request.split(splitSymbol);
        Map<String, String> args = new HashMap<>();
        try {
            for (int i = 0; i < params.length; i++) {
                String[] keyValue = params[i].split("=");
                args.put(keyValue[0], keyValue[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log(Level.INFO, "Index out of bound", e);
            response = operationFailCode + "Wrong input";
            return response;
        }
        try {
            libraryService.updateById(args);
            response = operationSuccessCode + "Operation is done";
        } catch (ServiceException e) {
            logger.log(Level.INFO, "Book updating failure", e);
            response = operationFailCode + e.getMessage();
        }
        return response;
    }
}
