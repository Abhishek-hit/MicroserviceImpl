package UserService.exception;

import UserService.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//kahi bhi exception ati h to ham yaha se handle kr lenke
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)//ye kab chalega ye batane ke liye use kia exphandler
    public ResponseEntity<ApiResponse> handleResurceNotFoundException(ResourceNotFoundException exception){
        String message=exception.getMessage ();//is response ko apiresponse ke andr lege aur bpas bhej denge
        ApiResponse response=ApiResponse.builder ().message (message).success (true).status (HttpStatus.NOT_FOUND).build ();
        return new ResponseEntity<ApiResponse> (response,HttpStatus.NOT_FOUND);

    }

}
