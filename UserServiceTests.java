package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    /**
     * UserService(ユーザー管理サービス)のテスト
     * テストケース① : 正常系-新しいユーザーが登録できる
     * テストケース② : 正常系-ユーザーIDでユーザーを取得できる
     * テストケース③ : 正常系-ユーザー名を更新できる
     * テストケース④ : 異常系-存在しないユーザーIDの場合は例外をスローする
     * テストケース⑤ : 重複したメールアドレスで登録しようとした場合は例外をスローする
     */

    /**
     * このテストはUserServiceを対象とする。ServiceクラスはCRUDをRepositoryに依存しているので、Repositoryをモックにする。
     */
    @Mock
    UserRepository repo;

    /**
     * Serviceクラスはコンストラクタ引数にRepositoryを渡すことで依存を注入する。今回は境界をモック化してServiceに渡す。
     */

    @InjectMocks
    UserService service;

    @Test
    void 新しいユーザーが登録できる() {
        // Arrange
        User user = new User(1L, "botan", "botan.email");
        /**
         * Userクラスは以下の定義を想定する。
         * class User {
         *  private final Long userId;
         *  private final String name;
         *  private final String email;
         *  
         *  public User(Long userId, String name, String email){
         *   this.userId = userId;
         *   this.name = name;
         *   this.email = email;
         *  }
         * }
         */
        when(repo.save(user)).thenReturn(1);
        /**
         * Repositoryにはユーザー登録用にint save(User user)が定義されているとする。
         */

        // Act
        int rows = service.registarUser(user.getName(), user.getEmail());

        // Assert
        assertEquals(1, rows, "ユーザー登録時は1が返ってくる。");
        

    }

    @Test
    void ユーザーIDでユーザーを取得できる() {
        // Arrange
        User user = new User(1L, "botan", "bota.email");
        /**
         * botanというユーザーを取得する。
         */
        when(repo.getUserById(1L)).thenReturn(Optional.of(user));
        /**
         * ユーザー取得のインターフェース定義は以下を想定する。
         * Optional getUserById(Long userId);
         */
        // Act
        User userInDB = service.getUserById(1L);

        // Assert
        assertEquals("botan", userInDB.getName());
        
    }

    @Test
    void ユーザー名を更新できる(){
        // Arrange
        User user = new User(1L, "botan", "botan.email");
        User updatedUser = new User(1L, "button", "botan.email");
        /**
         * updatedUserの情報を検索すると、userの情報が該当する。
         * 恐らく更新ロジック内では先に重複チェックをしているはず。
         * 重複チェックに該当した上で、戻り値が正常であることを検証すれば更新を判定できる。
         */
        when(repo.findById(updatedUser.getUserId())).thenReturn(Optional.of(user));
        when(repo.save(updatedUser)).thenReturn(1);
        // Act
        int updated = service.registarUser(updatedUser.getUserId(), updatedUser.getName());
        assertTrue(updated==1, "更新の戻り値は1であること");
    

    }

    @Test
    void 存在しないユーザーIDの場合は例外をスローする() {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.empty());
        // Assert & Act
        assertThrows(UserIdNotFoundException.class, () -> service.getUserById(1));
    }

    @Test
    void 重複したメールアドレスで登録しようとした場合は例外をスローする() {
        // Arrange
        User user1 = new User(1L, "user1", "example.email");
        User user2 = new User(2L, "user2", "example.email");

        /**
         * user2のメールアドレスで検索したところuser1の情報がヒットする。
         * 恐らくRegistarUser内では、登録前に重複チェックをしているはず。
         */
        when(repo.findByEmail(user2.getEmail())).thenReturn(Optional.of(user1));
        assertThrows(EmailHasBeenRegistaredException.class, service.registarUser(user2));
    }

    
    
}
