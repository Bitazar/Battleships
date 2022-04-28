from dataclasses import dataclass
from wave_function_collapse import WaveFunctionCollapse
from hard_constrains import HardConstrains
from soft_constrains import SoftConstrains
from placer import BattleshipsPlacer
from battleships_preprocessor import BattleshipsPreprocessor
from states import BattleshipsStates

import numpy as np


def print_board(board, col_lim, row_lim):
    print(' ', end='')
    for val in col_lim:
        print(val, end='')
    print(end='\n')
    for lim, row in zip(row_lim, board):
        print(lim, end='')
        for value in row:
            print('□' if value == 1 else '■', end='')
        print(end='\n')


@dataclass(frozen=True)
class BattleshipsSolver:
    value_set: list
    placer: BattleshipsPlacer
    # load from json
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

    SHIP = (2, {
        (0, 1): {1, 2},
        (0, -1): {1, 2},
        (-1, 0): {1, 2},
        (1, 0): {1, 2},
        (-1, 1): {1},
        (1, 1): {1},
        (-1, -1): {1},
        (1, -1): {1}
    })

    CONSTRAINS = [WATER, SHIP]
    STATES = BattleshipsStates()

    def __extend_initial_values(
            self,
            row_limits: int,
            column_limits: int,
            initial_values: list) -> list:
        preprocessor = BattleshipsPreprocessor(
            len(column_limits), len(row_limits))
        preprocessed = set(preprocessor(initial_values))
        for y, limit in enumerate(row_limits):
            if limit == 0:
                for x in range(len(column_limits)):
                    preprocessed.add((x, y, 1))
        for x, limit in enumerate(column_limits):
            if limit == 0:
                for y in range(len(row_limits)):
                    preprocessed.add((x, y, 1))
        return list(preprocessed)

    def __call__(
            self,
            initial_values: list,
            row_limits: np.ndarray,
            column_limits: np.ndarray,
            ship_sizes: map) -> list:
        constants = (row_limits, column_limits, ship_sizes)
        soft = SoftConstrains(constants, self.placer)
        hard = HardConstrains(constants, self.placer)
        width, height = len(column_limits), len(row_limits)
        solver = WaveFunctionCollapse(self.CONSTRAINS,
            hard, soft, self.placer, self.STATES)
        return solver(width, height,
            self.__extend_initial_values(row_limits, column_limits,
            initial_values))

    def print_solutions(
            self,
            initial_values: list,
            row_limits: np.ndarray,
            column_limits: np.ndarray,
            ship_sizes: map) -> None:
        solution = self.__call__(
            initial_values, row_limits, column_limits, ship_sizes)
        print_board(solution, column_limits, row_limits)
