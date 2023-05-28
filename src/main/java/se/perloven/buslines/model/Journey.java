package se.perloven.buslines.model;

import org.springframework.util.Assert;

public record Journey(Integer lineNumber, Integer journeyPatternPointNumber) implements ResponseDataType {
    public Journey {
        Assert.notNull(lineNumber, "Line number must not be null");
        Assert.notNull(journeyPatternPointNumber, "Point number must not be null");
    }
}
