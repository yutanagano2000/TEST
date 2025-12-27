package com.example.demo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTestsPractice {

    /**
     * Serviceクラスは外からRepositoryの依存を渡され、DB処理等を移譲している。
     * 今回はServicekクラスのテストなのでRepository以降の処理は感知しない。
     */
    @Mock
    OrderRepository repo;

    @InjectMocks
    OrderService service;

    @Test
    void 小計が2000円() {
        //Arrange
        Order order1 = new Order(1, List.of(
            new Item("pencil", 100, 10),
            new Item("eraiser", 100, 5),
            new Item("box", 500, 1)
        ));
        when(repo.findById(1)).thenReturn(Optional.of(order1));

        //Act
        int subTotal = service.calculateSubTotal(1);

        //Assert
        assertEquals(2000, subTotal);
    }

    @Test
    void 小計が5000円未満の場合は送料が500円(){
        //Arrange
        Order order2 = new Order(2, List.of(
            new Item("notebook", 2000, 2),
            new Item("light", 999, 1)
            
        ));
        when(repo.findById(2)).thenReturn(Optional.of(order2));

        //Act
        int shippings = service.calculateShippings(2);

        //Assert
        assertEquals(500, shippings);
    }

    @Test
    void 小計が4999円の時は合計が5499円() {
        // Arrange
        Order order3 = new Order(3, List.of(
            new Item("notebook", 2000, 2),
            new Item("light", 999, 1) 
        ));
        when(repo.findById(3)).thenReturn(Optional.of(order3));

        // Act
        int total = service.calculateTotal(3);
        
        // Assert
        assertEquals(5499, total);
    }

    
    
}
