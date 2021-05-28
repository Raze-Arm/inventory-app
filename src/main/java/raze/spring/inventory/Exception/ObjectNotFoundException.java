package raze.spring.inventory.Exception;

import java.util.function.Supplier;

public class ObjectNotFoundException extends Exception implements Supplier<ObjectNotFoundException> {

    public ObjectNotFoundException(String message) {
        super(message);
    }

    @Override
    public ObjectNotFoundException get() {
        return this;
    }
}

