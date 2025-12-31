import pytest


class UserServiceTest:
    @pytest.fixture
    #def repo_mock() -> FetchUserRepository:
    def repo_mock(self) -> FetchUserRepository:
        return create_autospec(FetchUserRepository, instance=True)
        """
        Repositoryのインスタンスを模したモックを生成している。
        Mock()では存在しないメソッドでもモック化できてしまうが、create_autospec()でモック化することで、そのクラスに存在しないメソッドをコールした時点でno attributeとエラーになる。
        """
    @pytest.fixture
    # def service(repo_mock: FetchUserRepository) -> FetchUserService:
    def service(self, repo_mock: FetchUserRepository) -> FetchUserService:
        return FetchUserService(repo_mock)
        """
        引数に渡した値がfixtureに登録されていた場合、その戻り値を引数に渡してfixtureに登録する。
        """

    # def ユーザー情報を取得できる(service: FetchUserService, repo_mock: FetcUserRepository) -> None:
    def test_ユーザー情報を取得できる(self, serice: FetchUserService, repo_mock: FetchUserRepository) -> None:
        """
        1. pytestがtest_で始まるメソッドを検出する
        2. 引数名serviceとrepo_mockを確認
        3. 対応するfixtureを実行
            - repo_mock fixtureを実行
            - service fixtureを実行
        4. fixtureの戻り値をテストメソッドに注入
        5. テストメソッドを実行

        テスト対象とモックをテストコードの引数に渡す。
        テストはテスト対象のテストメソッドを実行することで検証する。
        そのテストメソッド内では境界をモック化しているので、関数の戻り値を指定する。
        """
        # Arrange
        expected = [Car(id=1, maker_name="tesla")]
        repo_mock.fetch_user.retarn_value = expexted

        # Act
        actual = service.fetch_user()

        # Assert
        assert actual == excepted
        assert actual[0].maker_name == "tesla"