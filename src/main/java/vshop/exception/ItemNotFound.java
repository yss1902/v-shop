package vshop.exception;

public class ItemNotFound extends VshopException {

    private static final String MESSAGE = "존재하지 않는 아이템입니다.";
    public ItemNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
