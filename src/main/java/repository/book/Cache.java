package repository.book;

import java.util.List;

/** Clasa generica-tipul BookRepositoryCacheDecoratore care il dau acolo va defini toate tipurile
 *  pe care le folosesc in clase.
 */

// clasa are rolul de a stoca temporar datele in memorie pt a le accesa rapid
public class Cache<T> {
    public List<T> storage;
    public List<T> load(){
        return storage; // returneaza lista de elem salvate
    }

    // de fiecare data trimitem ca param o lista, iar nu tot cate un elem (acest cache va fi incarcat tot deodata)
    public void save(List<T> storage){
        this.storage=storage;
    }

    public boolean hasResult(){
        return storage != null;
    }

    public void invalidateCache(){
        // pun valoarea null in storage
        storage=null;
    }

}
