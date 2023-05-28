package se.perloven.buslines.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.util.Assert;

public record Stop(
        @JsonAlias("StopPointNumber") Integer stopPointNumber,
        @JsonAlias("StopPointName") String stopPointName,
        @JsonAlias("StopAreaTypeCode") String stopAreaTypeCode
) implements ResponseDataType {
    public Stop {
        Assert.notNull(stopPointNumber, "Stop point number must not be null");
        Assert.hasLength(stopPointName, "Stop point name must not be empty");
        Assert.hasLength(stopAreaTypeCode, "Stop area type code must not be empty");
    }

    public boolean isBusStop() {
        return stopAreaTypeCode.equals("BUSTERM");
    }
}
