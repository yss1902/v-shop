package vshop.exception;

public class InvalidSigninInformation extends VshopException {

    private static final String MESSAGE = "아이디/비밀번호 오류";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
