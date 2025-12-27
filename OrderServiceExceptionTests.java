package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceExceptionTests {

    // ServiceクラスはDBアクセス等の境界をRepositoryに依存している。今回はServiceクラスのテストなので、Repositoryをモック化する。
    @Mock
    OrderRepository repo;

    // モック化したRepositoryをServiceに注入する。
    @InjectMocks
    OrderService service;

    @Test
    void Orderが0件の場合例外をスローする(){
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(empty));

        // Act & Assert
        assertThrows(OrderNotFoundException, () -> service.calculateTotal(1));
    }
    
}
