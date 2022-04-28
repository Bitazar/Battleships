from dataclasses import dataclass
from itertools import chain
from random import sample

from placer import BattleshipsPlacer
from soft_constrains import SoftConstrains
from hard_constrains import HardConstrains
from wave_function_collapse import WaveFunctionCollapse
from states import BattleshipsStates

import numpy as np


# read from json
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


@dataclass(frozen=True)
class BattleshipsGenerator:
    width: int
    height: int
    ship_lengths: map
    resolution: int = 2

    CONSTRAINS = [WATER, HOR_SHIP, VER_SHIP]
    STATES = BattleshipsStates()

    def __unify(self, board: np.ndarray) -> np.ndarray:
        for y, row in enumerate(board):
            for x, cell in enumerate(row):
                if cell == 3:
                    board[y][x] = 2
        return board

    def __on_board(self, position: tuple) -> bool:
        x, y = position
        return self.width > x >= 0 and self.height > y >= 0

    def __ship_neighbourhood(self, position: tuple) -> list:
        x, y = position
        return [cell for cell in [
            (x, y - 1),
            (x, y + 1),
            (x + 1, y),
            (x - 1, y),
        ] if self.__on_board(cell)]

    def __directed_vector(self, neighbour: tuple, position: tuple) -> int:
        x_diff = position[0] - neighbour[0]
        y_diff = position[1] - neighbour[1]
        if x_diff != 0:
            return 3 if x_diff > 0 else 5
        return 4 if y_diff > 0 else 6

    def __vectorize(self, board: np.ndarray, position: tuple) -> int:
        neighborhood = [(x, y) for x, y in
            self.__ship_neighbourhood(position) if board[y][x] > 1]
        if not neighborhood:
            return 7
        if len(neighborhood) == 1:
            return self.__directed_vector(neighborhood[0], position)
        return 8

    def __strip(self, board: np.ndarray) -> list:
        indexes = list(chain.from_iterable([[(x, y) for x, _ in
            enumerate(row) if board[y][x] > 1
            ] for y, row in enumerate(board)]))
        stripped_board = []
        for x, y in sample(indexes, self.resolution):
            if board[y][x] == 1:
                stripped_board.append((x, y, 1))
            else:
                stripped_board.append((x, y, self.__vectorize(
                    board, (x, y))))
        return stripped_board

    def __calculate_row_lim(self, row: np.ndarray) -> list:
        return len([val for val in row if val == 2])

    def __calculate_limits(self, board: np.ndarray) -> list:
        row_limits = [self.__calculate_row_lim(row)
            for row in board]
        col_limits = [self.__calculate_row_lim(board[:,col])
            for col in range(self.width)]
        return row_limits, col_limits

    def __call__(self, placer: BattleshipsPlacer) -> tuple:
        constants = [None, None, self.ship_lengths]
        soft = SoftConstrains(constants, placer)
        hard = HardConstrains(constants, placer)
        solver = WaveFunctionCollapse(self.CONSTRAINS,
            hard.ship_length_constrain, soft.ship_length_constrain,
            placer, self.STATES)
        board = self.__unify(solver(self.width, self.height, []))
        return self.__strip(board), *self.__calculate_limits(board)
