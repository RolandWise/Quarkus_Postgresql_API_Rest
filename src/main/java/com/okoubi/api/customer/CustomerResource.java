package com.okoubi.api.customer;

import com.redhat.api.CustomerApiApplication;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.Objects;

@Path("/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "customer", description = "Customer Operations")
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All Customers",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed({CustomerApiApplication.Roles.CUSTOMER_READ})
    public Response get() {
        return Response.ok(customerService.findAll()).build();
    }

    @GET
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "200",
            description = "Get Customer by customerId",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Customer does not exist for customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed({CustomerApiApplication.Roles.CUSTOMER_READ})
    public Response getById(@Parameter(name = "customerId", required = true) @PathParam("customerId") Long customerId) {
        return customerService.findById(customerId)
                .map(customer -> Response.ok(customer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "Customer Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Customer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Customer already exists for customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed({CustomerApiApplication.Roles.CUSTOMER_WRITE})
    public Response post(@NotNull @Valid Customer customer, @Context UriInfo uriInfo) {
        Customer created = customerService.create(customer);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(created.customerId())).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "204",
            description = "Customer updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Customer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Customer object does not have customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable customerId does not match Customer.customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Customer found for customerId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed({CustomerApiApplication.Roles.CUSTOMER_WRITE})
    public Response put(@Parameter(name = "customerId", required = true) @PathParam("customerId") Long customerId, @NotNull @Valid Customer customer) {
        if (!Objects.equals(customerId, customer.customerId())) {
            throw new WebApplicationException("Path variable customerId does not match Customer.customerId", Response.Status.BAD_REQUEST);
        }
        customerService.update(customer);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "204",
            description = "Customer deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "No Customer found for customerId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed(CustomerApiApplication.Roles.CUSTOMER_WRITE)
    public Response delete(@Parameter(name = "customerId", required = true) @PathParam("customerId") Long customerId) {
        if (customerService.findById(customerId).isEmpty()) {
            throw new WebApplicationException(String.format("No Customer found for customerId[%s]", customerId), Response.Status.NOT_FOUND);
        }
        customerService.delete(customerId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}