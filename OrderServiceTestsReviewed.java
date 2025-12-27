package com.example.demo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTestsReviewed {

    @Mock
    OrderRepository repo;

    @InjectMocks
    OrderService service;

    @Test
    void 小計が2000円と計算されるかテスト() {
        // Arrange
        /**
         * Orderクラスの想定
         * public class Order{
         *  private final int orderId;
         *  private final List<Item> items;
         * 
         * 　public Order(int orderId, List<item> items){
         *  this.orderId = orderId;
         *  this.items = items;
         * }
         * }
         * 
         * Itemクラスの想定
         */
        Order order1 = new Order(1, List.of(
            new Item("pencil", 500, 4)
        ));
        /**
         * Repositoryの想定
         * Optinal<Order> findById(int orderId);
         */
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        //Act
        int subTotal = service.calculateSubTotal(1);
        //Assert
        assertEquals(2000, subTotal, "小計が2000円であること。");
    }

    @Test 
    void 小計が2500円以下の場合は送料が500円() {
        // Arrange
        /**
         * Orderクラスの想定ではitemsはListなので、List.of(item1, item2, item3,...)のように
         * 複数のitemsを受け取れる。
         */
        Order order1 = new Order(1, List.of(
            new Item("paper", 200, 5),
            new Item("milk", 400, 2)
        ));
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        // Act
        int shippings = service.calculateShippings(1);
        // Assert
        assertEquals(500, shippings);
    }

    @Test
    void 小計が2000円の時は合計が2500円() {
        // Arrange
        Order order1 = new Order(1, List.of(
            new Item("pencil", 500, 2),
            new Item("spice", 500, 2)
        ));
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        // Act
        int total = service.calculateTotal(1);
        // Assert
        assertEquals(2500, total);
    }
}
