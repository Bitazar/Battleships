from copy import deepcopy

import numpy as np


class Solver:
    def __init__(
            self,
            value_set,
            constrains,
            placement,
            finished,
            depth=lambda x: x) -> None:
        self.__value_set = value_set
        self.__constrains = constrains
        self.__placement = placement
        self.__finished = finished
        self.__solved_boards = []
        self.__indexes = []
        self.__depth = depth

    def __generate_indexes(self, board: np.ndarray) -> None:
        self.__indexes = []
        for y, row in enumerate(board):
            for x, cell in enumerate(row):
                if not self.__depth(cell):
                    self.__indexes.append((x, y))

    def __solve(self, board, step) -> None:
        if step == len(self.__indexes):
            if self.__finished(board):
                self.__solved_boards.append(board)
            return None
        position = self.__indexes[step]
        for value in self.__value_set:
            temp_board = deepcopy(board)
            if (self.__placement(temp_board, position, value) and
                    self.__constrains(temp_board)):
                self.__solve(temp_board, step + 1)

    def __call__(self, board: np.ndarray) -> list:
        self.__solved_boards = []
        self.__generate_indexes(board)
        self.__solve(board, 0)
        return self.__solved_boards
