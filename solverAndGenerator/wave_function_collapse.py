from itertools import chain
from random import choice
from math import inf

import numpy as np

WATER = (1, {
    (0, 1): {1, 2, 3},
    (0, -1): {1, 2, 3},
    (-1, 0): {1, 2, 3},
    (1, 0): {1, 2, 3},
    (-1, 1): {1, 2, 3},
    (1, 1): {1, 2, 3},
    (-1, -1): {1, 2, 3},
    (1, -1): {1, 2, 3}
})

HOR_SHIP = (2, {
    (0, 1): {1},
    (0, -1): {1},
    (-1, 0): {1, 2},
    (1, 0): {1, 2},
    (-1, 1): {1},
    (1, 1): {1},
    (-1, -1): {1},
    (1, -1): {1}
})

VER_SHIP = (3, {
    (0, 1): {1, 3},
    (0, -1): {1, 3},
    (-1, 0): {1},
    (1, 0): {1},
    (-1, 1): {1},
    (1, 1): {1},
    (-1, -1): {1},
    (1, -1): {1}
})

# 1 - water
# 2 - horizontal ship
# 3 - vertical ship

class WaveFunctionCollapse:
    def __init__(self, constrains: list) -> None:
        self.__constrains = {}
        for index, states in constrains:
            self.__constrains[index] = states

    def __generate_board(self, width: int, height: int) -> np.ndarray:
        init_superstate = {label for label in self.__constrains.keys()}
        return np.array([
            [init_superstate for x in range(width)]
            for y in range(height)
        ])

    @staticmethod
    def __min_entropy(board: np.ndarray) -> tuple:
        lengths = np.array([[len(val) if len(val) > 1 else inf for val in row]
            for row in board])
        return np.argwhere(lengths == np.min(lengths))

    @staticmethod
    def __on_board(board: np.ndarray, position: tuple) -> bool:
        ly, lx = board.shape
        x, y = position
        return lx > x >= 0 and ly > y >= 0

    @classmethod
    def __get_neighourhood(cls, board: np.ndarray, position: tuple) -> bool:
        neighborhood = []
        x, y = position
        for u in [-1, 0, 1]:
            for v in [-1, 0, 1]:
                if u or v:
                    neighborhood.append((x + u, y + v))
        return [x for x in neighborhood if cls.__on_board(board, x)]

    def __available_states(
            self,
            board: np.ndarray,
            position: tuple,
            diff: tuple,
            states: set) -> set:
        x, y = position
        n_states = states
        for state in board[y][x]:
            n_states = n_states.intersection(self.__constrains[state][diff])
        return n_states

    def __propagate(self, board: np.ndarray, position: tuple, checked) -> None:
        neighbours = self.__get_neighourhood(board, position)
        for nx, ny in neighbours:
            if (nx, ny) not in checked:
                dx = nx - position[0]
                dy = ny - position[1]
                if len(board[ny][nx]) > 1:
                    n_states = self.__available_states(board,
                        position, (dx, dy), board[ny][nx])
                    if n_states != board[ny][nx]:
                        board[ny][nx] = n_states
                        checked.append((nx, ny))
                        self.__propagate(board, (nx, ny), checked)

    def __collapse(self, board: np.ndarray) -> None:
        y, x = choice(self.__min_entropy(board))
        board[y][x] = {choice(list(board[y][x]))}
        self.__propagate(board, (x, y), [(x, y)])

    def __is_collapsed(self, board: np.ndarray) -> bool:
        return all(chain.from_iterable([[len(val) == 1 for val in row]
            for row in board]))

    @staticmethod
    def __convert_to_collapsed(board: np.ndarray) -> np.ndarray:
        return np.array([
            [val for val, in row]
            for row in board
        ])

    def __call__(self, width: int, height: int) -> np.ndarray:
        board = self.__generate_board(width, height)
        while not self.__is_collapsed(board):
            self.__collapse(board)
        return self.__convert_to_collapsed(board)

wave = WaveFunctionCollapse([WATER, HOR_SHIP, VER_SHIP])
print(wave(6, 6))