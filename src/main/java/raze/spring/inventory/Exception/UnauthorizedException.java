package raze.spring.inventory.Exception;

import java.util.function.Supplier;

public class UnauthorizedException extends Exception implements Supplier<UnauthorizedException> {
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public UnauthorizedException get() {
        return this;
    }
}
