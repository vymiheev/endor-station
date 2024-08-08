package battle.station.endor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestTemplateException extends Exception{

    private final HttpStatus statusCode;

    private final String error;

    public RestTemplateException(HttpStatus statusCode, String error) {
        super(error);
        this.statusCode = statusCode;
        this.error = error;
    }

}
