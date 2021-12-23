package by.epamtc.stanislavmelnikov.runner;

import by.epamtc.stanislavmelnikov.controller.executor.Executor;
import by.epamtc.stanislavmelnikov.dao.daointerface.BookDao;
import by.epamtc.stanislavmelnikov.dao.daointerface.UserDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.entity.Book;
import by.epamtc.stanislavmelnikov.logging.Logging;
import by.epamtc.stanislavmelnikov.view.input.DataScanner;
import by.epamtc.stanislavmelnikov.view.menu.IMenu;
import by.epamtc.stanislavmelnikov.view.menu.Menu;
import by.epamtc.stanislavmelnikov.view.output.ConsoleOutput;
import by.epamtc.stanislavmelnikov.view.requestgenerator.RequestGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Runner {
    public static void main(String[] args) {
        ConsoleOutput consoleOutput = new ConsoleOutput();
        DataScanner dataScanner = new DataScanner();
        Executor executor = new Executor();
        RequestGenerator requestGenerator = new RequestGenerator(consoleOutput, dataScanner, executor);
        try (FileInputStream input = new FileInputStream("log.config")) {
            LogManager.getLogManager().readConfiguration(input);
        } catch (IOException e) {
            consoleOutput.printMessage(e.getMessage());
        }
        Logger logger = Logging.getLogger();
        DaoFactory daoFactory = DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();
        BookDao bookDao = daoFactory.getBookDao();
        try {
            bookDao.init();
        } catch (DaoException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        try {
            userDao.init();
        } catch (DaoException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        requestGenerator.doSignInRequest();
        IMenu menu = new Menu(requestGenerator, consoleOutput, dataScanner);
        menu.createMenu();
        menu.runMenu();
    }
}
