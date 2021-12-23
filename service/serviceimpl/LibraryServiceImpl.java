package by.epamtc.stanislavmelnikov.service.serviceimpl;

import by.epamtc.stanislavmelnikov.dao.daointerface.BookDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.entity.Book;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;
import by.epamtc.stanislavmelnikov.service.validation.Validation;

import java.util.List;
import java.util.Map;

public class LibraryServiceImpl implements LibraryService {
    @Override
    public void addBook(Map<String, String> params) throws ServiceException {
        String name = params.get("name");
        String author = params.get("author");
        String yearStr = params.get("year");
        String amountPagesStr = params.get("pages");
        if (!Validation.isCorrectPositiveInteger(yearStr)) throw new ServiceException("Incorrect Year");
        if (!Validation.isCorrectPositiveInteger(amountPagesStr)) throw new ServiceException("Incorrect amount pages");
        int id = generateId();
        int year = Integer.parseInt(yearStr);
        int amountPages = Integer.parseInt(amountPagesStr);
        Book book = new Book(name, author, year, amountPages, id);
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        try {
            bookDao.writeBookIn(book);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Book> getAllBooks() throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        List<Book> bookList;
        String allBooksCriteria = "ALL";
        try {
            bookList = bookDao.getBooks(allBooksCriteria, "");
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (bookList.size() == 0) throw new ServiceException("No books in the library");
        return bookList;
    }

    @Override
    public List<Book> searchByCriteria(String criteria, String request) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        List<Book> bookList;
        try {
            bookList = bookDao.getBooks(criteria, request);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (bookList.size() == 0) throw new ServiceException("No books found");
        return bookList;
    }


    @Override
    public void removeById(String id) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        try {
            bookDao.removeById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateById(Map<String, String> request) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        String idStr = request.get("id");
        if (idStr == null) throw new ServiceException("You haven't entered id.");
        String name = request.get("name");
        String author = request.get("author");
        String yearStr = request.get("year");
        String pagesStr = request.get("pages");
        try {
            Book book = bookDao.getBooks("id", idStr).get(0);
            setBookFields(book, author, name, yearStr, pagesStr);
            bookDao.removeById(idStr);
            bookDao.writeBookIn(book);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setBookFields(Book book, String author,
                              String name, String yearStr, String pagesStr) throws ServiceException {
        if (yearStr != null) {
            if (!Validation.isCorrectPositiveInteger(yearStr)) throw new ServiceException("Incorrect Year");
            book.setYear(Integer.parseInt(yearStr));
        }
        if (pagesStr != null) {
            if (!Validation.isCorrectPositiveInteger(pagesStr)) throw new ServiceException("Incorrect amount pages");
            book.setAmountPages(Integer.parseInt(pagesStr));
        }
        if (name != null) {
            book.setName(name);
        }
        if (author != null) {
            book.setAuthor(author);
        }
    }

    public int generateId() throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        List<Integer> listID;
        try {
            listID = bookDao.getAllID();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        int maxIDValue = Integer.MAX_VALUE;
        for (int id = 1; id < maxIDValue; id++) {
            if (listID == null) return id;
            if (!listID.contains(id)) {
                return id;
            }
        }
        throw new ServiceException("Fail in ID generating");
    }
}
