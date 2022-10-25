package miniStringBuilderApp.exceptions;

public class UserRequestException extends Exception {



    public UserRequestException(String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }






}
