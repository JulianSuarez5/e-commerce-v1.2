package ppi.e_commerce.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutRequest {
    
    @NotBlank(message = "La dirección de envío es obligatoria")
    private String shippingAddress;
    
    @NotBlank(message = "La ciudad es obligatoria")
    private String shippingCity;
    
    @NotBlank(message = "El estado es obligatorio")
    private String shippingState;
    
    @NotBlank(message = "El código postal es obligatorio")
    private String shippingZipCode;
    
    private String notes;
    
    @NotNull(message = "El método de pago es obligatorio")
    private String paymentMethod; // PAYPAL, CREDIT_CARD, etc.

    public CheckoutRequest() {}

    // Getters and Setters
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }

    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }

    public String getShippingZipCode() { return shippingZipCode; }
    public void setShippingZipCode(String shippingZipCode) { this.shippingZipCode = shippingZipCode; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}

