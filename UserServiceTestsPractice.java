package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTestsPractice {
    /**
     * UserService(ユーザー管理サービス)のテスト
     * テストケース① : 正常系-新しいユーザーが登録できる
     * テストケース② : 正常系-ユーザーIDでユーザーを取得できる
     * テストケース③ : 正常系-ユーザー名を更新できる
     * テストケース④ : 異常系-存在しないユーザーIDの場合は例外をスローする
     * テストケース⑤ : 重複したメールアドレスで登録しようとした場合は例外をスローする
     */

    @Mock
    UserRepository repo;

    @InjectMocks
    UserService service;

    @Test
    void 新しいユーザーが登録できる(){
        // Arrange
        User user1 = new User(1L, "sample", "sample.email");
        when(repo.save(user1)).thenReturn(1);

        // Act
        int registaration = service.registarUser(user1.getUserId(), user1.getUserName());

        // Assert
        assertEquals(1, registaration);
    }

    @Test
    void ユーザーIDでユーザーを取得できる(){
        // Arrange
        User user2 = new User(2L, "sample", "sample.email");
        when(repo.findById(user2.getUserId)).then(Optional.of(user2));

        // Act
        User userInDB = service.getUserById(user2.getUserId());

        // Assert
        assertEquals(user2.getUserName(), userInDB.getUserName());
    }

    @Test
    void ユーザー名を更新できる(){
        // Arrange
        User user3 = new User(3L, "sample", "sample.email");
        User user4 = new User(3L, "test", "sample.email");

        when(repo.findById(user4.getUserId())).thenReturn(Optional.of(user3));
        when(repo.save(user4)).thenReturn(1);

        // Act
        // int updated = service.registarUser(user4);
        int updated = service.updateUser(user4.getUserId(), user4.getUsername());

        // Assert
        assertTrue(updated == 1);
    }

    @Test
    void 存在しないユーザーIDの場合は例外をスローする() {
        // Arrange
        when(repo.findById(5)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserIdNotFoundException.class, () -> service.getUserById(5));
    }

    @Test
    void 重複したメールアドレスで登録しようとした場合は例外をスローする() {
        // Arrange
        User user6 = new User(6L, "sample", "sample.email");
        User user7 = new User(7L, "test", "sample.email");
        when(repo.findByEmail(user7.getEmail())).thenReturn(Optional.of(user6));

        // Act & Assert
        assertThrows(MailHasBeenRegistaredException.class, () -> service.registarUser(user7.getUserName(), user7.getEmail()));
    }

    
}