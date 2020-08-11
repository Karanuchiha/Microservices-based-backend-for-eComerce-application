package com.karan.reviewservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Document ("reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private String id;

  @NotEmpty
  private String userId;

  @NotEmpty
  private String productId;

  @NotEmpty
  @Size (max = 150)
  private String comments;

  @Min (value = 1)
  @Max (value = 5)
  private int rating;

  public Review(Review review) {
    this.id = null;
    this.userId = review.userId;
    this.productId = review.productId;
    this.comments = review.comments;
    this.rating = review.rating;
  }


}
