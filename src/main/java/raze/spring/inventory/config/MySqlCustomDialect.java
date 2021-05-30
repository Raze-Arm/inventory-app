package raze.spring.inventory.config;

import org.hibernate.dialect.MySQL57Dialect;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
public class MySqlCustomDialect extends MySQL57Dialect {

    public MySqlCustomDialect() {
        super();
        registerColumnType(Types.BIGINT, "NUMERIC(19, 0)");
    }
}
