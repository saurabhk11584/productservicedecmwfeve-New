package com.scaler.productservicedecmwfeve.services;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    String createPaymentLink(String orderId) throws RazorpayException, StripeException;
    String getPaymentStatus(String payementId);
}
