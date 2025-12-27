package com.example.demo;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
// ECサイトのServiceクラスを想定したテストクラス。
public class OrderServiceTests {

    /**今の時点ではOrderRepositoryクラスを定義していないのでエラーになる。Serviceクラスはビジネスロジックを実装するクラスで、
     * DBアクセス等の処理はRepositoryクラスに移譲している。
     * 依存を外から渡すため、Serviceクラスはコンストラクタ引数にRepositoryクラスのインスタンスを受け取る
     * 今回はテストとしてDBアクセス等の処理をモック化するので、@Mockと@InjectMocksを使って、テスト対象クラスのServiceクラスに依存を注入する。
     */
    // TODO:OrderRepositoryクラスの実装
    @Mock
    OrderRepository repo; 

    /**今の時点ではOrderServiceクラスを定義していないのでエラーになる。
     * このテストクラスではServiceクラスで想定されるビジネスロジックをテストするので、テスト対象はOrderServiceクラスとなる。
     * Serviceクラスはコンストラクタ引数にRepositoryが渡されて、DBアクセス等の処理を移譲する。
     * 今回はビジネスロジック以降のDB処理についてはテスト対象ではないため、モック化したRepositoryをServiceに注入する。
     */
    // TODO:OrderServiceクラスの実装
    @InjectMocks
    OrderService service;
    
    @Test
    void subTotal2000なら送料500円で合計2500円() {
        /**
         * Arrangeセクション。テストに必要なテストデータを用意する。
         * DBアクセス処理等のデータはRepositoryに移譲していて、今回はRepositoryをモック化しているので、
         * その戻り値としてのテストデータを用意する必要がある。
         * 戻り値はEntityのOrderクラスを想定する、Orderクラスは以下のような定義だと仮定する。
         * public class Order {
         *  private final int orderId;
         *  private final List<Item> items;
         * }
         * Orderクラスのクラスオブジェクトとして定義されているItemクラスの定義は以下のように仮定する。
         * public class Item {
         *  private final string itemName;
         *  private final int itemPrice;
         *  private final int itemAmount;
         * }
         * 以上から、OrderクラスはorderIdに紐づいているリスト形式のitemsというクラス構成になるので、
         * この想定に即したテストデータを用意する。
         */
        // TODO:Orderクラスの実装
        Order order1 = new Order(1, List.of(
            new Item("pencil", 500, 4)
        ));
        /**
         * Serviceクラスには様々なビジネスロジックが実装されている。
         * Repositoryによるデータ取得はその各ビジネスロジック内で実装されている。
         * ビジネスロジックの引数でorderIdを受け取るとして、そのorderIdをRepositoryが受け取った時の
         * 戻り値をここで設定する。
         */
        /**
         * Repositoryで設定されているインターフェース定義は以下を想定する。
         * Optional<Order> findById(int id);
         * これはfindByIdの戻り値が「見つからない」場合を想定している。
         */
        // TODO: RepositoryインターフェースでfindByIDの実装
        when(repo.findById(1)).thenReturn(Optional.of(order1));

        /**
         * Serviceクラスには小計金額を計算するロジックが実装されいているとする。
         * その合計金額計算ロジックにorderIdを渡して、合計金額を取得している。
         * このビジネスロジックで小計を取得し、テスト要件のsubTotal2000を検証する。
         */
        int subTotal = service.calculateSubTotal(1);
        // assertEquals(subTotal, 2000);
        assertEquals(2000, subTotal, "小計は2000円であること。");
        /**
         * assertEqualsの書き方はassertEquals(expecetd, actual)
         */

        int shippings = service.calculateShippings(1);
        // assertEquals(shippings, 500);
        assertEquals(500, shippings, "小計2000円の時は送料が500円以下であること。");

        int total = service.calculateTotal(1);
        // assertEquals(total, 2500);
        assertEquals(2500, total, "小計が2000円の時は合計が2500円であること。");

    }
    
}
