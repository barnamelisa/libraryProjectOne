package model.validation;
import java.util.*;

// T = un tip generic, adica un placeholder care poate fi orice tip de date cand creez o instanta
// rolul clasei Notification este de a retine erorile aparute ca sa nu trebuiasca sa le retinem undeva
public class Notification<T> {
    private T result; // avem acces la result doar daca nu exista erori
    private final List<String> errors;
    public Notification(){
        this.errors =  new ArrayList<>();
    }

    public void addError(String error){
        this.errors.add(error);
    }

    public boolean hasErrors(){
        return !this.errors.isEmpty();
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        if (hasErrors()){
            throw new ResultFetchException(errors);
        }
        return result;
    }

    public String getFormattedErrors(){
        return String.join("\n",errors); // fiecare eroare scrisa pe linie diferita
    }
}
