import numpy as np


class PlacementConstrains:
    def __init__(self) -> None:
        pass

    @staticmethod
    def __on_board(board: np.ndarray, position: tuple) -> bool:
        x, y = position
        lx, ly, _ = board.shape
        return lx > x >= 0 and ly > y >= 0

    @classmethod
    def __get_neighborhood(cls, board: np.ndarray, position: tuple) -> list:
        x, y = position
        return [cell for cell in [
            (x - 1, y - 1),
            (x - 1, y + 1),
            (x + 1, y - 1),
            (x + 1, y + 1),
        ] if cls.__on_board(board, cell)]

    @classmethod
    def __cross_neigborhood(cls, board: np.ndarray, position: tuple) -> bool:
        return not any([board[y][x][0] > 1
            for x, y in cls.__get_neighborhood(board, position)])

    @classmethod
    def __ship_neighbourhood(cls, board: np.ndarray, position: tuple) -> list:
        x, y = position
        return [cell for cell in [
            (x, y - 1),
            (x, y + 1),
            (x + 1, y),
            (x - 1, y),
        ] if cls.__on_board(board, cell)]

    @staticmethod
    def __emplace_one_ship(board, position: tuple) -> bool:
        x, y = position
        board[y][x][1] = [position]
        board[y][x][0] = 2
        return True

    @staticmethod
    def __lenghten_ship(board, position: tuple, ships: list) -> bool:
        ship ,= ships
        ship.append(position)
        x, y = position
        board[y][x][1] = ship
        board[y][x][0] = 2
        return True

    @staticmethod
    def __on_same_axis(neighborhood) -> bool:
        left, right = neighborhood
        return left[0] == right[0] or left[1] == right[1]

    @classmethod
    def __concatenate_ships(
            cls,
            board,
            position: tuple,
            ships: list,
            neighborhood: list) -> bool:
        neighborhood = [(x, y) for x, y in neighborhood if board[y][x][1] is not None]
        if not cls.__on_same_axis(neighborhood):
            return False
        left, right = ships
        new_ship = left + [position] + right
        for x, y in neighborhood + [position]:
            board[y][x][1] = new_ship
        x, y = position
        board[y][x][0] = 2
        return True

    @classmethod
    def __emplace_ship(cls, board: np.ndarray, position: tuple) -> list:
        neighborhood = cls.__ship_neighbourhood(board, position)
        ships = [board[y][x][1] for x, y in neighborhood if board[y][x][1] is not None]
        if not ships:
            return cls.__emplace_one_ship(board, position)
        if len(ships) == 1:
            return cls.__lenghten_ship(board, position, ships)
        if len(ships) == 2:
            return cls.__concatenate_ships(board, position, ships, neighborhood)
        return False

    @staticmethod
    def __emplace_water(board: np.ndarray, position: tuple) -> bool:
        x, y = position
        board[y][x][0] = 1
        return True

    @classmethod
    def __vec_neighbours(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        return [(x, y) for x, y in cls.__ship_neighbourhood(
            board, position) if board[y][x][0] > 1]

    @classmethod
    def __valid_left_vector(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        x, y = position
        x, y = (x - 1, y)
        return (len(cls.__vec_neighbours(board, position)) <= 1
            and board[y][x][0] != 1)

    @classmethod
    def __valid_right_vector(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        x, y = position
        x, y = (x + 1, y)
        return (len(cls.__vec_neighbours(board, position)) <= 1
            and board[y][x][0] != 1)

    @classmethod
    def __valid_up_vector(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        x, y = position
        x, y = (x, y - 1)
        return (len(cls.__vec_neighbours(board, position)) <= 1
            and board[y][x][0] != 1)

    @classmethod
    def __valid_down_vector(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        x, y = position
        x, y = (x, y + 1)
        return (len(cls.__vec_neighbours(board, position)) <= 1
            and board[y][x][0] != 1)

    @classmethod
    def __valid_single_vector(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        return (len(cls.__vec_neighbours(board, position)) == 0)

    @classmethod
    def __valid_double_vector(
            cls,
            board: np.ndarray,
            position: tuple) -> bool:
        neighbourhood = cls.__vec_neighbours(board, position)
        if len(neighbourhood) == 1:
            return True
        return (len(neighbourhood) <= 2 and
            cls.__on_same_axis(neighbourhood))

    @classmethod
    def __valid_vectorize_field(
            cls,
            value: int,
            board: np.ndarray,
            position: tuple) -> bool:
        if value == 3:
            return cls.__valid_left_vector(board, position)
        elif value == 4:
            return cls.__valid_up_vector(board, position)
        elif value == 5:
            return cls.__valid_right_vector(board, position)
        elif value == 6:
            return cls.__valid_down_vector(board, position)
        elif value == 7:
            return cls.__valid_single_vector(board, position)
        return cls.__valid_double_vector(board, position)

    @classmethod
    def vectorized_fields(cls, board: np.ndarray):
        for y, row in enumerate(board):
            for x, cell in enumerate(row):
                if (cell[0] > 2 and not
                    cls.__valid_vectorize_field(cell[0], board, (x, y))):
                        return False
        return True

    def __call__(
            self,
            board: np.ndarray,
            position: tuple,
            value: int):
        if value == 1:
            return (
                self.__emplace_water(board, position) and
                self.vectorized_fields(board)
            )
        if not self.__cross_neigborhood(board, position):
            return False
        return (
            self.__emplace_ship(board, position) and
            self.vectorized_fields(board)
        )
