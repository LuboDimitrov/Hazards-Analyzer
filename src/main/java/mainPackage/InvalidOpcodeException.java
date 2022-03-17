package mainPackage;

public class InvalidOpcodeException extends Exception{
    public InvalidOpcodeException(String errorMessage){
        super(errorMessage);
    }
}
