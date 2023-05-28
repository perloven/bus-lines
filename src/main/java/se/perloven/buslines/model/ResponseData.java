package se.perloven.buslines.model;

import org.springframework.util.Assert;

import java.util.List;

public record ResponseData<T extends ResponseDataType>(List<T> result) {
    public ResponseData {
        Assert.notNull(result, "Result must not be null");
    }
}
