// IBookManager.aidl
package and.elvis.androiddevsearch;

import and.elvis.androiddevsearch.Book;
import and.elvis.androiddevsearch.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(in IOnNewBookArrivedListener listener);

    void unregisterListener(in IOnNewBookArrivedListener listener);
}
