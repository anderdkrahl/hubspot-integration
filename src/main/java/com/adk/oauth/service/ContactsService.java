package com.adk.oauth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adk.oauth.config.HubspotConfig;
import com.adk.oauth.dto.ContactDTO;
import com.adk.oauth.util.RateLimitHandler;

@Service
public class ContactsService {

  @Autowired
  private HubspotConfig config;

  @Autowired
  TokenService tokenService;

  @Autowired
  RateLimitHandler rateHandler;

  public ResponseEntity<String> listContacts() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenService.getAccessToken());
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    HttpEntity<Void> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();

    System.out.println("listing contacts");
    ResponseEntity<String> response = restTemplate.exchange(
        config.getContactUrl(),
        HttpMethod.GET,
        request,
        String.class);

    rateHandler.handleRateLimit(response.getHeaders());
    return response;
  }

  public ResponseEntity<String> createContact(ContactDTO contactDTO) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenService.getAccessToken());
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ContactDTO> request = new HttpEntity<>(contactDTO, headers);

    ResponseEntity<String> response = new RestTemplate().exchange(
        config.getContactUrl(),
        HttpMethod.POST,
        request,
        String.class);

    rateHandler.handleRateLimit(response.getHeaders());
    return response;
  }
}
