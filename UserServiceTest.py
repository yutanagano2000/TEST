import pytest


class UserServiceTest:
    @pytest.fixture
    def repo_mock() -> FetchUserRepository:
        return create_autospec(FetchUserRepository, instance=True)
        """
        Repositoryのインスタンスを模したモックを生成している。
        Mock()では存在しないメソッドでもモック化できてしまうが、create_autospec()でモック化することで、そのクラスに存在しないメソッドをコールした時点でno attributeとエラーになる。
        """
    @pytest.fixture
    def service(repo_mock: FetchUserRepository) -> FetchUserService:
        return FetchUserService(repo_mock)
        """
        引数に渡した値がfixtureに登録されていた場合、その戻り値を引数に渡してfixtureに登録する。
        """

    def ユーザー情報を取得できる(service: FetchUserService, repo_mock: FetcUserRepository) -> None:
        # Arrange
        expected = [Car(id=1, maker_name="tesla")]
        repo_mock.fetch_user.retarn_value = expexted

        # Act
        actual = service.fetch_user()

        # Assert
        assert actual == excepted
        assert actual[0].maker_name == "tesla"