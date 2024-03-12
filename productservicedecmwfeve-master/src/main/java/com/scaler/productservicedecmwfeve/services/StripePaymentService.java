package com.scaler.productservicedecmwfeve.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class StripePaymentService implements PaymentService {


    Product crateProduct() throws StripeException {
        ProductCreateParams params =
                ProductCreateParams.builder().setName("Gold Plan").build();
        Product product = Product.create(params);
        return product;
    }

    Price createPrice(Product product) throws StripeException {
        PriceCreateParams params =
                PriceCreateParams.builder()
                        .setCurrency("usd")
                        .setCustomUnitAmount(
                                PriceCreateParams.CustomUnitAmount.builder().setEnabled(true).build()
                        )
                        .setProduct(product.getId())
                        .build();

        Price price = Price.create(params);
        return price;
    }

    @Override
    public String createPaymentLink(String orderId) throws StripeException {
        Stripe.apiKey = "sk_test_51OpXVFSANWPwONkm0GFVFjxEjPC90lkrfRmxwIQTApdKkCG90uTmjDiVFWsYzPevA10y0URkSfOU6JL7J7g94wNW00BIdgtuMj";
        Product product = crateProduct();
        Price price = createPrice(product);
        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()

                        )
                        .addPaymentMethodType(PaymentLinkCreateParams.PaymentMethodType.CARD)
                        .build();

        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink.getUrl();
    }

    @Override
    public String getPaymentStatus(String payementId) {
        return "Payment status for payment id: " + payementId;
    }
}
