package ppi.e_commerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SellerAccessDeniedException extends RuntimeException {
    public SellerAccessDeniedException(String message) {
        super(message);
    }
}
