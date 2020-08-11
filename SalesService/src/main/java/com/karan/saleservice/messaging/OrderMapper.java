package com.karan.saleservice.messaging;

import com.bearingpoint.common.OrderDTO;
import com.bearingpoint.common.ProductDTO;
import com.karan.saleservice.model.Order;
import com.karan.saleservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

  OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
  @Mapping(source = "products", target = "productsDto")
  OrderDTO toDto(Order order);
  ProductDTO toDto(Product product);
}
