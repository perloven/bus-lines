package se.perloven.buslines.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.util.Assert;

import java.util.List;

public record ResponseData<T extends ResponseDataType>(@JsonAlias("Result") List<T> result) {
    public ResponseData {
        Assert.notNull(result, "Result must not be null");
    }
}
