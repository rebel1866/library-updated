package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.entity.CurrentUser;
import by.epamtc.stanislavmelnikov.logging.Logging;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.validation.Validation;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminRightsVerifier implements Command {
    @Override
    public String execute(String request) {
        String response;
        Logger logger = Logging.getLogger();
        try {
            Validation.verifyUserRights();
            response = operationSuccessCode;
        } catch (ServiceException e) {
            logger.log(Level.INFO, "Admin rights check is not passed \n Current admin status: {0}",
                    CurrentUser.getCurrentUser().isAdmin());
            response = operationFailCode + e.getMessage();
        }
        return response;
    }
}

