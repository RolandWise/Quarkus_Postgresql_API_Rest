package com.okoubi.api.customer;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

@Entity(name = "Customer")
@Table(name = "customer")
public class CustomerEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    public Long customerId;

    @Column(name = "prenoms")
    @NotEmpty(message = "{Customer.prenoms.required}")
    public String prenoms;

    @Column(name = "middle_name")
    public String middleName;

    @Column(name = "nom")
    @NotEmpty(message = "{Customer.nom.required}")
    public String nom;

    @Column(name = "suffix")
    public String suffix;

    @Column(name = "email")
    @Email(message = "{Customer.email.invalid}")
    public String email;

    @Column(name = "phone_mobile")
    public String phoneMobile;

    # equals, hashCode and toString removed for brevity

}