import pytest

class UserServicePraictice:
    @pytest.fixture
    def repo_mock(self) -> FetchUserRepository:
        return create_autospec(FetchUserRepository, instance=True)
        """
        Repositoryのインスタンスに模したモックを作成する。
        """

    @pytest.fixture
    def service(self, repo_mock: FetchUserRepository) -> FetchUserService:
        return FetchUserService(repo_mock)
        """
        モック化したRepositoryをServiceに渡してインスタンを注入する。
        """

    def test_ユーザー情報を取得できる() -> None:
        # Arrange
        expected = [Car(id=1, maker_name="tesla")]
        repo_mock.fetch_user().return_value = expected

        # Act
        actual = service.fetch_user()

        #Assert
        assert actual == expected
        assert actual[0].maker_name == "tesla"
    


