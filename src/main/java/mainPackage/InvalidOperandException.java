package mainPackage;

public class InvalidOperandException extends Exception{
    public InvalidOperandException(String errorMessage){
        super(errorMessage);
    }
}
