package battle.station.endor.exception;

import lombok.Getter;

@Getter
public class CommonEnderRuntimeException extends RuntimeException {

    private final String error;

    public CommonEnderRuntimeException(String error) {
        super(error);
        this.error = error;
    }

}
