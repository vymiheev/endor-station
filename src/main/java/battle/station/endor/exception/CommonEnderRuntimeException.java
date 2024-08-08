package battle.station.endor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonEnderRuntimeException extends RuntimeException{

    private final HttpStatus statusCode;

    private final String error;

    public CommonEnderRuntimeException(HttpStatus statusCode, String error) {
        super(error);
        this.statusCode = statusCode;
        this.error = error;
    }

}
