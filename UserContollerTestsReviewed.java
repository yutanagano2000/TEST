package com.example.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
/**
 * UserController（ユーザー管理コントローラー）のテスト
 * Web層のテストをする際は@WebMvcTestアノテーションを使う。
 * 引数にはロード対象のクラスを入れて、「このクラスのテストを行います」と明示する。
 * 
 * テストケース① : 正常系-新しいユーザーが登録できる
 * テストケース② : 正常系-ユーザーIDでユーザーを取得できる
 * テストケース③ : 正常系-ユーザー名を更新できる
 * テストケース④ : 異常系-存在しないユーザーIDの場合は例外をスローする
 * テストケース⑤ : 異常系-重複したメールアドレスで登録しようとした場合は例外をスローする
 */
public class UserContollerTestsReviewed {

    @Autowired
    private MockMvc mockMvc;
    /**
     * このテストクラスはmockMvcに依存するので、@Autowiredで自動的に依存注入する。
     * MockMvcはHTTPリクエストをシミュレートするためのクラス。
     */

    @MockitoBean
    private UserService userService;
    /**
     * ControllerはServiceに依存しているが、Serviceはビジネスロジック層なので今回はテスト対象外。
     * そのため@MockitoBeanでモック化する。
     */

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * JSONのシリアライズ/デシリアライズに使用する。
     * @Autowiredで自動的に注入される。
     */

    @Test
    void 新しいユーザーが登録できる() throws Exception {
        // Arrange
        /**
         * Userクラスは以下の定義を想定する。
         * class User {
         * private final Long userId;
         * private final String name;
         * private final String email;
         * 
         * public User(Long userId, String name, String email){
         * this.userId = userId;
         * this.name = name;
         * this.email = email;
         * }
         * }
         */
        when(userService.registarUser("sample", "sample.email")).thenReturn(1);
        /**
         * UserServiceにはユーザー登録用にint registarUser(String name, String email)が定義されているとする。
         * 登録成功時は1が返ってくる。
         */
        UserRegistrationRequest request = new UserRegistrationRequest("sample", "sample.email");
        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("sample"))
                .andExpect(jsonPath("$.email").value("sample.email"));
        /**
         * mockMvc.performでHTTPリクエストを送信する。
         * POST /user でユーザー登録を行う。
         * RESTful APIの設計では、リソースの作成にはPOSTメソッドを使用し、リソースIDはURLに含めない。
         * jsonPathでは以下のようなJSONレスポンスを想定している。
         * { "userId" : 1, "name" : "sample", "email" : "sample.email" }
         */
    }

    @Test
    void ユーザーIDでユーザーを取得できる() throws Exception {
        // Arrange
        User user = new User(1L, "sample", "sample.email");
        /**
         * sampleというユーザーを取得する。
         */
        when(userService.getUserById(1L)).thenReturn(user);
        /**
         * UserServiceにはユーザー取得用にUser getUserById(Long userId)が定義されているとする。
         */

        // Act & Assert
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("sample"))
                .andExpect(jsonPath("$.email").value("sample.email"));
        /**
         * GET /user/{id} でユーザーを取得する。
         * RESTful APIの設計では、リソースの取得にはGETメソッドを使用する。
         * jsonPathでは以下のようなJSONレスポンスを想定している。
         * { "userId" : 1, "name" : "sample", "email" : "sample.email" }
         */
    }

    @Test
    void ユーザー名を更新できる() throws Exception {
        // Arrange
        /**
         * ユーザーID 1のユーザー名を"updatedName"に更新する。
         */
        when(userService.updateUser(1L, "updatedName")).thenReturn(1);
        /**
         * UserServiceにはユーザー更新用にint updateUser(Long userId, String name)が定義されているとする。
         * 更新成功時は1が返ってくる。
         */
        UserUpdateRequest request = new UserUpdateRequest("updatedName");
        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("updatedName"));
        /**
         * PUT /user/{id} でユーザー情報を更新する。
         * RESTful APIの設計では、リソースの更新にはPUTメソッドを使用する。
         * jsonPathでは以下のようなJSONレスポンスを想定している。
         * { "userId" : 1, "name" : "updatedName" }
         */
    }

    @Test
    void 存在しないユーザーIDの場合は例外をスローする() throws Exception {
        // Arrange
        when(userService.getUserById(1L))
                .thenThrow(new UserIdNotFoundException("ユーザーが見つかりません"));
        /**
         * 存在しないユーザーIDを指定した場合、UserIdNotFoundExceptionがスローされる。
         */

        // Act & Assert
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound());
        /**
         * 存在しないユーザーIDを指定した場合、HTTPステータスコード404(Not Found)が返される。
         * RESTful APIの設計では、リソースが見つからない場合は404を返す。
         */
    }

    @Test
    void 重複したメールアドレスで登録しようとした場合は例外をスローする() throws Exception {
        // Arrange
        /**
         * 既に"duplicate.email"というメールアドレスで登録されているユーザーが存在する。
         */
        when(userService.registarUser("newUser", "duplicate.email"))
                .thenThrow(new EmailHasBeenRegistaredException("メールアドレスが登録済みです"));
        /**
         * UserServiceには重複チェックがあり、重複したメールアドレスで登録しようとした場合、
         * EmailHasBeenRegistaredExceptionがスローされる。
         */
        UserRegistrationRequest request = new UserRegistrationRequest("newUser", "duplicate.email");
        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
        /**
         * 重複したメールアドレスで登録しようとした場合、HTTPステータスコード409(Conflict)が返される。
         * RESTful APIの設計では、リソースの競合が発生した場合は409を返す。
         */
    }

}
