package com.scaler.productservicedecmwfeve.controllers;

import com.razorpay.RazorpayException;
import com.scaler.productservicedecmwfeve.dtos.CreatePaymentLinkRequestDto;
import com.scaler.productservicedecmwfeve.services.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public String createPaymentLink(@RequestBody CreatePaymentLinkRequestDto request) throws RazorpayException, StripeException {
        String link = paymentService.createPaymentLink(request.getOrderId());
        return link;
    }

    @PostMapping("/webhook")
    public void handleWebhookEvent(@RequestBody Map<String, String> webhookEvent) {
        System.out.println(webhookEvent);
    }
}
