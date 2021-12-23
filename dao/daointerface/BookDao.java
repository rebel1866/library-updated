package by.epamtc.stanislavmelnikov.dao.daointerface;

import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.entity.Book;

import java.util.List;

public interface BookDao {
    void writeBookIn(Book book) throws DaoException;
    List<Book> getBooks(String criteria, String request) throws DaoException;
    void removeById(String id) throws DaoException;
    List<Integer> getAllID() throws DaoException;
    void init() throws DaoException;
}
