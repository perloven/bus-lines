package se.perloven.buslines.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Response<T extends ResponseDataType>(
        @JsonAlias("StatusCode") int statusCode,
        @JsonAlias("ResponseData") ResponseData<T> responseData
) {}
