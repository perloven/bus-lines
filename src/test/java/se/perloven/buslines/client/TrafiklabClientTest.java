package se.perloven.buslines.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.perloven.buslines.model.Journey;
import se.perloven.buslines.model.Response;
import se.perloven.buslines.model.Stop;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebClient
@Import({WebClientConfig.class, TrafiklabClient.class})
@TestPropertySource(properties = {"trafiklab.api-key=abc123"})
@DirtiesContext
class TrafiklabClientTest {
    private static MockWebServer mockWebServer;

    @Autowired
    private TrafiklabClient client;

    @BeforeAll
    static void start() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void registerMockServerUrl(DynamicPropertyRegistry registry) {
        registry.add("trafiklab.base-url", () -> mockWebServer.url("/").toString());
    }

    @AfterAll
    static void shutdown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getJourney() throws Exception {
        var mockResponse = new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(singleJourneyResponse());
        mockWebServer.enqueue(mockResponse);

        Optional<Response<Journey>> response = client.getJourneyData();

        assertTrue(response.isPresent());
        var journeyResponse = response.get();
        assertEquals(1, journeyResponse.responseData().result().size());
        var journey = journeyResponse.responseData().result().get(0);
        assertEquals(1, journey.lineNumber());
        assertEquals(10008, journey.journeyPatternPointNumber());
        var recordedRequest = mockWebServer.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals("/?model=jour&key=abc123", recordedRequest.getPath());
    }

    private String singleJourneyResponse() {
        return """
                {
                    "StatusCode": 0,
                    "Message": null,
                    "ExecutionTime": 595,
                    "ResponseData": {
                        "Version": "2023-05-28 00:12",
                        "Type": "JourneyPatternPointOnLine",
                        "Result": [
                            {
                                "LineNumber": "1",
                                "DirectionCode": "1",
                                "JourneyPatternPointNumber": "10008",
                                "LastModifiedUtcDateTime": "2022-02-15 00:00:00.000",
                                "ExistsFromDate": "2022-02-15 00:00:00.000"
                            }
                        ]
                    }
                }
                """;
    }

    @Test
    void getStop() throws Exception {
        var mockResponse = new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(singleStopResponse());
        mockWebServer.enqueue(mockResponse);

        Optional<Response<Stop>> response = client.getStopData();

        assertTrue(response.isPresent());
        var stopResponse = response.get();
        assertEquals(1, stopResponse.responseData().result().size());
        var stop = stopResponse.responseData().result().get(0);
        assertEquals(10001, stop.stopPointNumber());
        assertEquals("Stadshagsplan", stop.stopPointName());
        assertEquals("BUSTERM", stop.stopAreaTypeCode());
        var recordedRequest = mockWebServer.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals("/?model=stop&key=abc123", recordedRequest.getPath());
    }

    private String singleStopResponse() {
        return """
                {
                    "StatusCode": 0,
                    "Message": null,
                    "ExecutionTime": 595,
                    "ResponseData": {
                        "Version": "2023-05-28 00:12",
                        "Type": "StopPoint",
                        "Result": [
                            {
                                "StopPointNumber": "10001",
                                "StopPointName": "Stadshagsplan",
                                "StopAreaNumber": "10001",
                                "LocationNorthingCoordinate": "59.3373571967995",
                                "LocationEastingCoordinate": "18.0214674159693",
                                "ZoneShortName": "A",
                                "StopAreaTypeCode": "BUSTERM",
                                "LastModifiedUtcDateTime": "2022-10-28 00:00:00.000",
                                "ExistsFromDate": "2022-10-28 00:00:00.000"
                            }
                        ]
                    }
                }
                """;
    }
}

