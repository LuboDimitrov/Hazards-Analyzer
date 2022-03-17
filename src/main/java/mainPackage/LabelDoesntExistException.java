package mainPackage;

public class LabelDoesntExistException  extends Exception{
    public LabelDoesntExistException(String errorMessage) {
        super(errorMessage);
    }
}
