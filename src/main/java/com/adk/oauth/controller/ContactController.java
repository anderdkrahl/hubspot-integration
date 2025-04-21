package com.adk.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adk.oauth.dto.ContactDTO;
import com.adk.oauth.service.ContactsService;
import com.adk.oauth.util.WebhookSecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private WebhookSecurityUtil webhookUtil;

    @Autowired
    private ContactsService contactService;

    @GetMapping("/list")
    public ResponseEntity<String> listContacts() {
        return contactService.listContacts();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createContact(@RequestBody ContactDTO contactDTO) {
        return contactService.createContact(contactDTO);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            HttpServletRequest request,
            @RequestHeader("X-HubSpot-Signature-V3") String signature,
            @RequestHeader("X-HubSpot-Request-Timestamp") String timestamp,
            @RequestBody String body) {

        if (!webhookUtil.validate(request, body, signature, timestamp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("Valid webhook: " + body);
        return ResponseEntity.ok().build();
    }
}
