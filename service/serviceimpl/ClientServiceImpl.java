package by.epamtc.stanislavmelnikov.service.serviceimpl;

import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.dao.daointerface.UserDao;
import by.epamtc.stanislavmelnikov.entity.CurrentUser;
import by.epamtc.stanislavmelnikov.entity.User;
import by.epamtc.stanislavmelnikov.service.encryption.PasswordEncryption;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import by.epamtc.stanislavmelnikov.service.validation.Validation;

import java.util.Map;

public class ClientServiceImpl implements ClientService {
    @Override
    public User signIn(String login, String password) throws ServiceException {
        User user= null;
        try {
            DaoFactory factory = DaoFactory.getInstance();
            UserDao userDao = factory.getUserDao();
             user = userDao.getUser(login);
            String encryptedPassword = PasswordEncryption.encryptPassword(password);
            if (!Validation.isRightPassword(user.getPassword(), encryptedPassword)) {
                throw new ServiceException("Wrong password");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return user;
    }

    @Override
    public void addUser(Map<String, String> params) throws ServiceException {
        String login = params.get("login");
        String password = params.get("password");
        String firstName = params.get("first_name");
        String lastName = params.get("last_name");
        String ageStr = params.get("age");
        String email = params.get("email");
        String userRole = params.get("user_role");
        if (Validation.isLoginExists(login)) throw new ServiceException("Login already exists");
        if (!Validation.isCorrectPassword(password)) throw new ServiceException("Incorrect password");
        if (!Validation.isCorrectAge(ageStr)) throw new ServiceException("Incorrect Age");
        if (!Validation.isCorrectEmail(email)) throw new ServiceException("Incorrect email");
        if (!Validation.isCorrectUserRole(userRole)) throw new ServiceException("Incorrect user role");
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        String encryptedPassword = PasswordEncryption.encryptPassword(password);
        User user = new User(login, encryptedPassword, firstName, lastName, Integer.parseInt(ageStr),
                email, Boolean.parseBoolean(userRole));
        try {
            userDao.writeUserIn(user);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void removeByLogin(String login) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        try {
            userDao.removeByLogin(login);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateByLogin(Map<String, String> request) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        String login = request.get("login");
        if (login == null) throw new ServiceException("You haven't entered login.");
        String newLogin = request.get("newlogin");
        String password = request.get("password");
        String firstName = request.get("firstname");
        String lastname = request.get("lastname");
        String ageStr = request.get("age");
        String email = request.get("email");
        String isAdmin = request.get("admin");
        try {
            User user = userDao.getUser(login);
            setUserFields(user, newLogin, password, firstName, lastname, email, ageStr, isAdmin);
            userDao.removeByLogin(login);
            userDao.writeUserIn(user);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void signOut() {
        User empty = new User();
        CurrentUser.setCurrentUser(empty);
    }

    public void setUserFields(User user, String newLogin, String password, String firstName, String lastname,
                              String email, String ageStr, String isAdmin) throws ServiceException {
        if (firstName != null) user.setFirstName(firstName);
        if (lastname != null) user.setLastName(lastname);
        if (password != null) {
            if (!Validation.isCorrectPassword(password)) throw new ServiceException("Incorrect password");
            user.setPassword(PasswordEncryption.encryptPassword(password));
        }
        if (newLogin != null) {
            if (Validation.isLoginExists(newLogin)) throw new ServiceException("Login already exists");
            user.setLogin(newLogin);
        }
        if (ageStr != null) {
            if (!Validation.isCorrectAge(ageStr)) throw new ServiceException("Incorrect Age");
            user.setAge(Integer.parseInt(ageStr));
        }
        if (email != null) {
            if (!Validation.isCorrectEmail(email)) throw new ServiceException("Incorrect email");
            user.setEmail(email);
        }
        if (isAdmin != null) {
            if (!Validation.isCorrectUserRole(isAdmin)) throw new ServiceException("Incorrect user role");
            user.setAdmin(Boolean.parseBoolean(isAdmin));
        }
    }
}
