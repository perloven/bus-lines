package se.perloven.buslines.model;

import org.springframework.util.Assert;

public record Stop(Integer stopPointNumber, String stopPointName, String stopAreaTypeCode) implements ResponseDataType {
    public Stop {
        Assert.notNull(stopPointNumber, "Stop point number must not be null");
        Assert.hasLength(stopPointName, "Stop point name must not be empty");
        Assert.hasLength(stopAreaTypeCode, "Stop area type code must not be empty");
    }
}
