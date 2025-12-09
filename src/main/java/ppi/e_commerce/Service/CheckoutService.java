package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.CheckoutRequest;
import ppi.e_commerce.Dto.OrderDto;

public interface CheckoutService {
    OrderDto processCheckout(String username, CheckoutRequest request);
}
