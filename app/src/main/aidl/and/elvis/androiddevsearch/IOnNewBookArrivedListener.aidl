// IOnNewBookArrivedListener.aidl
package and.elvis.androiddevsearch;

// Declare any non-default types here with import statements
import and.elvis.androiddevsearch.Book;
interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book newBook);
}
