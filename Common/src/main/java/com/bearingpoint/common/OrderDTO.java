package com.bearingpoint.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {

  private Long id;
  private String userId;
  private String paymentReference;
  private String status;
  private List<ProductDTO> productsDto;
  private Double totalPrice;

}
