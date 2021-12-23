package by.epamtc.stanislavmelnikov.service.serviceinterface;

import by.epamtc.stanislavmelnikov.entity.Book;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface LibraryService {
    void addBook(Map<String,String> params) throws ServiceException;

    List<Book> getAllBooks() throws ServiceException;

    List<Book> searchByCriteria(String criteria, String request) throws ServiceException;

    void removeById(String request) throws ServiceException;

    void updateById(Map<String, String> request) throws ServiceException;
}
