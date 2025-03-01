package com.empik.complaint.service;

import com.empik.complaint.dto.Country;
import com.empik.complaint.exception.CouldNotDetermineCountryException;
import com.empik.complaint.exception.WrongResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CountryService {
  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public Country findCountryForIp(String ip) {
    if (ip == null || ip.isBlank()) {
      log.error("Could not determine a country for a blank ip address");
      throw new CouldNotDetermineCountryException(ip);
    }
    try {
      HttpRequest httpRequest = prepareRequestFor(ip);
      HttpResponse<String> response = askExternalService(httpRequest);
      if (response.statusCode() != 200) {
        throw new WrongResponseStatusException(response.statusCode());
      }
      return objectMapper.readValue(response.body(), Country.class);
    } catch (Exception e) {
      log.error("Failed to determine the country for {}", ip, e);
      throw new CouldNotDetermineCountryException(ip, e);
    }
  }

  private static HttpRequest prepareRequestFor(String ip) throws URISyntaxException {
      return HttpRequest.newBuilder()
          .uri(new URI("http://ip-api.com/json/" + ip))
          .timeout(Duration.ofSeconds(2))
          .GET()
          .build();
  }

  private HttpResponse<String> askExternalService(HttpRequest httpRequest)
      throws IOException, InterruptedException {
    return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
  }
}
