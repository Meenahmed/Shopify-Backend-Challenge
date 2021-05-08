package com.demo.shopifychallenge.bucket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bucket {
    PRODUCT_IMAGE("shopify-intern-image-upload");

    private final String name;
}
