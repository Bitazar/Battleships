from solver import Solver
from final_constrains import FinalConstrains
from hard_constrains import HardConstrains
from placement_constrains import PlacementConstrains

import numpy as np


def print_board(board, col_lim, row_lim):
    print(' ', end='')
    for val in col_lim:
        print(val, end='')
    print(end='\n')
    for lim, row in zip(row_lim, board):
        print(lim, end='')
        for value, _ in row:
            print('□' if value == 1 else '■', end='')
        print(end='\n')


class BattleshipsSolver:
    def __init__(self, value_set: list) -> None:
        self.__value_set = value_set

    def __generate_board(
            self,
            initial_values: list,
            row_limits: np.ndarray,
            column_limits: np.ndarray,
            placement: PlacementConstrains) -> np.ndarray:
        board = np.array([
            [(val, None) for val in row]
            for row in np.zeros((len(row_limits), len(column_limits)))
        ])
        for x, y, value in initial_values:
            placement(board, (x, y), 2 if value > 1 else 1)
            board[y][x][0] = value
        return board

    def __preprocessing(
            self,
            board: np.ndarray,
            row_limits: np.ndarray,
            column_limits: np.ndarray) -> np.ndarray:
        for i, row_sum in enumerate(row_limits):
            if not row_sum:
                for j, _ in enumerate(column_limits):
                    board[i][j][0] = 1
        for i, col_sum in enumerate(column_limits):
            if not col_sum:
                for j, _ in enumerate(row_limits):
                    board[j][i][0] = 1
        return board

    def __call__(
            self,
            initial_values: list,
            row_limits: np.ndarray,
            column_limits: np.ndarray,
            ship_sizes: map) -> list:
        constants = (row_limits, column_limits, ship_sizes)
        constrains = HardConstrains(constants)
        final = FinalConstrains(constants)
        placement = PlacementConstrains()
        board = self.__generate_board(
            initial_values, row_limits, column_limits, placement)
        solver = Solver(self.__value_set, constrains, placement, final,
            lambda x: x[0])
        return solver(self.__preprocessing(
            board, row_limits, column_limits))

    def print_solutions(
            self,
            initial_values: list,
            row_limits: np.ndarray,
            column_limits: np.ndarray,
            ship_sizes: map) -> None:
        solutions = self.__call__(
            initial_values, row_limits, column_limits, ship_sizes)
        for solution in solutions:
            print_board(solution, column_limits, row_limits)
