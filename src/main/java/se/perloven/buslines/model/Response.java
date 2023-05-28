package se.perloven.buslines.model;

public record Response<T extends ResponseDataType>(int statusCode, ResponseData<T> responseData) {
}
