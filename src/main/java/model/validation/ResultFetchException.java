package model.validation;
import java.util.*;
import java.util.stream.Collectors;

public class ResultFetchException extends RuntimeException{
    private final List<String> errors;

    public ResultFetchException(List<String> errors){
        super("Failed to fetch the result as operation encountered errors!");
        this.errors=errors;
    }

    @Override
    public String toString(){
        // fiecare ob din lista il voi mapa la string-ul lui, iar toate rez se vor colecta cu collect
        return errors.stream().map(Object::toString).collect(Collectors.joining(",")) + super.toString();
    }
}
