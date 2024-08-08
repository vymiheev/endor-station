package battle.station.endor.exception;

import lombok.Getter;

@Getter
public class RestTemplateException extends Exception {

    private final String error;

    public RestTemplateException(String error) {
        super(error);
        this.error = error;
    }

}
