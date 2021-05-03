package  com.soat.formation.saga.order.application.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossibleAddOrderException extends RuntimeException {

    public ImpossibleAddOrderException(String message) {
        super(message);
    }


}
