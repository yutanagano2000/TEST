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

    /**
     * ServiceクラスはDBアクセス等の境界をRepositoryに依存している。今回はServiceクラスのテストなので、Repositoryをモック化する。
     */
    @Mock
    OrderRepository repo;

    /**
     * モック化したRepositoryをServiceに注入する。
     */
    @InjectMocks
    OrderService service;

    @Test
    // void Orderが0件の場合例外をスローする(){
    void 存在しないOrderIDの場合は例外をスローする() {
        // Arrange
        // when(repo.findById(1)).thenReturn(Optional.of(empty));
        /**
         * empty()の場合は「そのOrderID自体が存在しない」ことになる。
         * 仮に以下のようなテストデータの場合、OrderID自体は存在しているがリストが空になる。
         * Order order = new Order(1, List.of());
         * この場合は、例外はスローされずに、小計が0円になる。
         */
        when(repo.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        // assertThrows(OrderNotFoundException, () -> service.calculateTotal(1));
        assertThrows(OrderNotFoundException.class, () -> service.calculateSubTotal(1));
    }
    
}
