package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
/**
 * WEb層のテストをする際はWebMvcTestアノテーションを使う 引数にはロード対象のクラスを入れて、「このクラスのテストを行います」と明示する。
 */
public class UserControllerTestsSample {

    @Autowired
    private MockMvc mockMvc;
    /**
     * このテストクラスはmockMvcに依存するので、Autowiredで自動的に依存注入する。
     */

    @MockitoBean
    private UserService userService;

    /**
     * ControllerはServiceに依存しているが、Serviceはビジネスロジック層なので 今回はテスト対象外。そのため@MockitoBeanでモック化する。
     */
    @Test
    void ユーザーIDでユーザーを取得できる() throws Exception {
        // Arrange
        User user = new User(1L, "sample", "sample.email");
        when(userService.getUserById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/user/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("sample"));
        /**
         * mockMvc.performでHTTPリクエストを送信する。 jsonPathでは以下のようなJSONレスポンスを想定している。 { "userId" : 1, "name"
         * : "sample" }
         */


    }



}
