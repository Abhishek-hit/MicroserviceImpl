package UserService.exception;

public class ResourceNotFoundException extends RuntimeException{//kisi dure expection ko impl kr lia taki kisi dusri ki property iske pas a jaye
    //we can add more property also
    public ResourceNotFoundException(){
        super("Resource not found exception");
    }
    public ResourceNotFoundException(String message){//koi apna bhi message de skta h
        super(message);
    }
}
