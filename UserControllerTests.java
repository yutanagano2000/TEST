package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.service.annotation.PutExchange;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    /**
     * UserContorller（ユーザー管理コントローラー）のテスト
     * テストケース① : 正常系-新しいユーザーが登録できる
     * テストケース② : 正常系-ユーザーIDでユーザーを取得できる
     * テストケース③ : 正常系-ユーザー名を更新できる
     * テストケース④ : 異常系-存在しないユーザーIDの場合は例外をスローする
     * テストケース⑤ : 異常系-重複したメールアドレスで登録しようとした場合は例外をスローする
     */

    @Autowired
    private MockMvc mockMvc;
    /**
     * このクラスはMockMvcに依存するので、@Autowiredで依存注入する。
     */

    @MockitoBean
    private UserService service;

    /**
     * ControllerはServiceに依存しているが、今回はControllerのテストなので、ServiceのビジネスロジックはMockで対応する。
     */

    @Test
    void 新しいユーザーが登録できる() throws Exception {
        // Arrange
        User user = new User(1L, "sample", "sample.email");
        when(service.registarUser(user)).thenReturn(1);

        // Act
        mockMvc.perform(post("/user/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.結果").value("$.登録完了"));
    }

    @Test
    void ユーザーIDでユーザーを取得できる() throws Exception {
        // Arrange
        User user = new User(2L, "sample", "sample.email");
        when(service.getUserById(2L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("user/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$.ユーザー名").value("sample"));
    }

    @Test //テストケース③
    void ユーザー名を更新できる() throws Exception {
        // Arrange
        User user = new User(3L, "sample", "sample.email");
        when(service.updateUser(user.getUserId())).thenReturn(1);

        // Act & Assert
        /**
         * クエリパラメータで更新する？
         */
        mockMvc.perform(put("/user/&3")).andExpect(status().isOk())
                .andExpect(jsonPath("$.更新結果").value("$.更新成功"));
    }

    @Test //テストケース④
    void 存在しないユーザーIDの場合は例外をスローする() throws Exception {
        // Arrange
        when(service.getUserById(4L)).thenReturn();
        /**
         * repositoryはOptional.empty()を返すはず。
         * ということはUserById内では例外をキャッチしているはず。
         * thenReturn()ではおかしいのでは？戻り値としてUserIDNotFoundExceptionを指定するべきでは？
         * でもthenReturn(UserIDNotFoundException.class)と指定してもいいのだろうか？
         */

        // Act & Assert
        mockMvc.perform(get("user/4")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.エラー").value("そのユーザーは存在しません。"));
        /**
         * このエラーメッセージがUserIDNotFoundExceptionで定義したメッセージの可能性がある。
         */
    }

    @Test //テストケース⑤
    void 重複したメールアドレスで登録しようとした場合は例外をスローする() throws Exception {
        // Arrange
        User user = new User(5L, "sample", "sample.email");
        when(service.registarUser(user)).thenReturn();

        // Act & Assert
        mockMvc.perform(post("/user/5")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.エラー").value("$.メールアドレスが登録済みです。"));
        /**
         * これもテストケース④と同じで、servieの戻り値がわかればできそう。
         */
    }

}
