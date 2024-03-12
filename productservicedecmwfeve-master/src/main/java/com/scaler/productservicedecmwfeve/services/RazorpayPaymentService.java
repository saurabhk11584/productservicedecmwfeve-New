package com.scaler.productservicedecmwfeve.services;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service

public class RazorpayPaymentService implements PaymentService {
    private RazorpayClient razorpayClient;

    public RazorpayPaymentService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String createPaymentLink(String orderId) throws RazorpayException {

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",1000);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",false);
        //paymentLinkRequest.put("first_min_partial_amount",100);
        paymentLinkRequest.put("expire_by",System.currentTimeMillis() +  (15*60*1000));
        paymentLinkRequest.put("reference_id",orderId);
        paymentLinkRequest.put("description","Payment for order no " + orderId);
        JSONObject customer = new JSONObject();

        //Order order = orderService.getOrderDetails(orderId);
        //String customerName = order.getUser().getName();
        //String contact = order.getUser().getContact();

        customer.put("name","Saurabh Kumavat");
        customer.put("contact","+918983677174");
        customer.put("email","saurabhkumavat11@gmail.com");
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notes = new JSONObject();
        notes.put("Order Items","1. IPhone 15 Pro Max");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://naman.dev/");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
        return payment.get("short_url");
    }

    @Override
    public String getPaymentStatus(String payementId) {
        return "Payment status for payment id: " + payementId;
    }
}
