package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.entity.Book;
import by.epamtc.stanislavmelnikov.logging.Logging;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookViewer implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        LibraryService libraryService = serviceFactory.getLibraryService();
        Logger logger = Logging.getLogger();
        try {
            response = operationSuccessCode + formatBooks(libraryService.getAllBooks());
        } catch (ServiceException e) {
            logger.log(Level.INFO, "Book viewer exception", e);
            response = operationFailCode + e.getMessage();
        }
        return response;
    }

    public String formatBooks(List<Book> bookList) {
        StringBuilder books = new StringBuilder();
        for (Book book : bookList) {
            books.append("ID: " + book.getId() + "\n");
            books.append("Book name: " + book.getName() + "\n");
            books.append("Book author: " + book.getAuthor() + "\n");
            books.append("Book year issue: " + book.getYear() + "\n");
            books.append("Book amount of pages: " + book.getAmountPages() + "\n\n");
        }
        return books.toString();
    }
}

