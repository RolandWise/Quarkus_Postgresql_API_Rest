package com.okoubi.api.customer;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface CustomerMapper {
    
    Customer toDomain(CustomerEntity entity);
    
    CustomerEntity toEntity(Customer domain);
    
}