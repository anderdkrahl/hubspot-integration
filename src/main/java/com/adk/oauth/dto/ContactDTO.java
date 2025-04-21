package com.adk.oauth.dto;

import java.util.HashMap;
import java.util.Map;

public class ContactDTO {
  private Map<String, String> properties = new HashMap<>();

  public ContactDTO() {
  }

  public ContactDTO(String email, String firstname, String lastname) {
    setEmail(email);
    setFirstname(firstname);
    setLastname(lastname);
  }

  public String getEmail() {
    return properties.get("email");
  }

  public void setEmail(String email) {
    properties.put("email", email);
  }

  public String getFirstname() {
    return properties.get("firstname");
  }

  public void setFirstname(String firstname) {
    properties.put("firstname", firstname);
  }

  public String getLastname() {
    return properties.get("lastname");
  }

  public void setLastname(String lastname) {
    properties.put("lastname", lastname);
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }
}