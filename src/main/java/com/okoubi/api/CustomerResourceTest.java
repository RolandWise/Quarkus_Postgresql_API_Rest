package com.okoubi.api;

import com.okoubi.api.customer.Customer;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class CustomerResourceTest {

    @Test
    public void getAll() {
        given()
                .when()
                .get("/api/v1/customers")
                .then()
                .statusCode(200);
    }

    @Test
    public void getById() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post("/api/v1/customers")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        Customer got = given()
                .when()
                .get("/api/v1/customers/{customerId}", saved.customerId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved).isEqualTo(got);
    }

    @Test
    public void getByIdNotFound() {
        given()
                .when()
                .get("/api/v1/customers/{customerId}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void post() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post("/api/v1/customers")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        assertThat(saved.customerId()).isNotNull();
    }

    @Test
    public void postFailNoFirstName() {
        Customer customer = new Customer(null, null, null, RandomStringUtils.randomAlphabetic(10),
                null, null, null);
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post("/api/v1/customers")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void put() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post("/api/v1/customers")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        Customer updated = new Customer(saved.customerId(), saved.firstName(), saved.middleName(), saved.lastName(),
                saved.suffix(), saved.email(), saved.phone());
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .put("/api/v1/customers/{customerId}", updated.customerId())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void putFailNoLastName() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post("/api/v1/customers")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Customer.class);
        Customer updated = new Customer(saved.customerId(), saved.firstName(), saved.middleName(), null,
                saved.suffix(), saved.email(), saved.phone());
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .put("/api/v1/customers/{customerId}", updated.customerId())
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    private Customer createCustomer() {
        return new Customer(null, RandomStringUtils.randomAlphabetic(10), null,
                RandomStringUtils.randomAlphabetic(10),    null,
                RandomStringUtils.randomAlphabetic(10) + "@rhenergy.dev", RandomStringUtils.randomNumeric(10));
    }

}