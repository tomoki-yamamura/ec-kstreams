package com.example.cartservice.application.controller.dto.command.request;

public record AddItemRequest(
    String cartId,
    String userId,
    String itemId,
    int quantity) {
}
