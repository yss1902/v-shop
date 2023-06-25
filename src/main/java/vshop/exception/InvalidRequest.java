package vshop.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends VshopException {
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest(String message) {
        super(message);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
