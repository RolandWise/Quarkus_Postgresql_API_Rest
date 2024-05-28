package com.okoubi.api.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record Customer(Long customerId, @NotEmpty String firstName, String prenoms, @NotEmpty String nom,
                       String suffix, @Email String email, String phoneMobile) {
}