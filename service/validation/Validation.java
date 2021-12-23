package by.epamtc.stanislavmelnikov.service.validation;

import by.epamtc.stanislavmelnikov.dao.daointerface.UserDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.entity.CurrentUser;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final String regexSymb = "[A-Za-z]";
    private static final String regexNumber = "[0-9]";
    private static final String regexEmail = "[A-Za-z0-9]+@[a-z]+\\.[a-z]+";

    public static boolean isCorrectPassword(String password) {
        Pattern pattern = Pattern.compile(regexSymb);
        Pattern pattern1 = Pattern.compile(regexNumber);
        Matcher matcher = pattern.matcher(password);
        Matcher matcher1 = pattern1.matcher(password);
        if (!matcher.find()) return false;
        if (!matcher1.find()) return false;
        return true;
    }

    public static boolean isCorrectEmail(String email) {
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) return false;
        return true;
    }

    public static boolean isCorrectUserRole(String userRole) {
        if (userRole.equals("true") || userRole.equals("false")) return true;
        return false;
    }

    public static boolean isCorrectAge(String str) {
        int age;
        try {
            age = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        if (age <= 0 || age > 130) {
            return false;
        }
        return true;
    }

    public static boolean isCorrectPositiveInteger(String pagesStr) {
        int pages;
        try {
            pages = Integer.parseInt(pagesStr);
        } catch (NumberFormatException e) {
            return false;
        }
        if (pages <= 0) {
            return false;
        }
        return true;
    }


    public static boolean isLoginExists(String login) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        List<String> logins;
        try {
            logins = userDao.getLogins();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (logins.contains(login)) return true;
        return false;
    }

    public static boolean isRightPassword(String sourcePassword, String targetPassword) {
        return sourcePassword.equals(targetPassword);
    }

    public static void verifyUserRights() throws ServiceException {
        boolean isAdmin = CurrentUser.getCurrentUser().isAdmin();
        if (!isAdmin) throw new ServiceException("You're not allowed to do this action.");
    }
}
