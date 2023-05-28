package se.perloven.buslines.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.util.Assert;

public record Journey(
        @JsonAlias("LineNumber") Integer lineNumber,
        @JsonAlias("JourneyPatternPointNumber") Integer journeyPatternPointNumber
) implements ResponseDataType {
    public Journey {
        Assert.notNull(lineNumber, "Line number must not be null");
        Assert.notNull(journeyPatternPointNumber, "Point number must not be null");
    }
}
