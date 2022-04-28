from copy import deepcopy
from itertools import chain
from random import shuffle, choice
from math import inf
from typing import Any

import numpy as np

from constrains import Constrains
from placer import PlacerInterface
from solver import Solver
from states import States


class NoSolutionException(Exception):
    def __init__(self) -> None:
        super().__init__("No solution can be found for the given problem")


class WaveFunctionCollapse(Solver):
    def __init__(
            self,
            constrains: list,
            hard_constrains: Constrains,
            soft_constrains: Constrains,
            placer: PlacerInterface,
            states: States) -> None:
        super().__init__(constrains, hard_constrains,
            soft_constrains, placer)
        self.__states = states

    @property
    def states(self) -> States:
        return self.__states

    def __generate_board(self, width: int, height: int) -> np.ndarray:
        init_superstate = {label for label in self.constrains.keys()}
        init_cell_value = self.placer.generate_cell(init_superstate)
        return np.array([
            [init_cell_value for x in range(width)]
            for y in range(height)
        ])

    def __get_length(self, cell: Any) -> int:
        length = len(self.placer.access_cell(cell))
        return length if length > 1 else inf

    def __min_entropy(self, board: np.ndarray) -> tuple:
        lengths = np.array([[self.__get_length(cell)
            for cell in row] for row in board])
        return np.argwhere(lengths == np.min(lengths))

    def __on_board(self, board: np.ndarray, position: tuple) -> bool:
        lx = board.shape[0]
        ly = board.shape[1]
        x, y = position
        return lx > x >= 0 and ly > y >= 0

    def __get_neighourhood(
            self,
            board: np.ndarray,
            position: tuple) -> bool:
        neighborhood = []
        x, y = position
        for u in [-1, 0, 1]:
            for v in [-1, 0, 1]:
                if u or v:
                    neighborhood.append((x + u, y + v))
        return [x for x in neighborhood if self.__on_board(board, x)]

    def __propagate(
            self,
            board: np.ndarray,
            position: tuple,
            checked: list) -> None:
        neighbours = self.__get_neighourhood(board, position)
        for nx, ny in neighbours:
            if (nx, ny) not in checked:
                dx = nx - position[0]
                dy = ny - position[1]
                if len(self.placer.access_cell(board[ny][nx])) > 1:
                    n_states = self.states(self, board, position, (dx, dy))
                    if n_states != self.placer.access_cell(board[ny][nx]):
                        self.placer(board, (nx, ny), n_states)
                        checked.append((nx, ny))
                        self.__propagate(board, (nx, ny), checked)

    def __is_collapsed(self, board: np.ndarray) -> bool:
        return all(chain.from_iterable([[
                len(self.placer.access_cell(cell)) <= 1 for cell in row
            ] for row in board]))

    def __check_constrains(self, board: np.ndarray) -> tuple:
        if self.soft_constrains(board):
            if self.__is_collapsed(board):
                return board if self.hard_constrains(board) else None
            elif (result := self.__collapse(board)) is not None:
                return result
        return None

    def __collapse(self, board: np.ndarray) -> tuple:
        y, x = choice(self.__min_entropy(board))
        superposition = list(self.placer.access_cell(board[y][x]))
        shuffle(superposition)
        for state in superposition:
            temp_board = deepcopy(board)
            self.placer(temp_board, (x, y), {state})
            self.__propagate(temp_board, (x, y), [(x, y)])
            if (result := self.__check_constrains(temp_board)) is not None:
                return result
        return None

    def __precollapse(self, board: np.ndarray, init_vals: list) -> None:
        for x, y, state in init_vals:
            self.placer(board, (x, y), {state})
            self.__propagate(board, (x, y), [(x, y)])

    def __unpack_state(self, cell) -> int:
        val ,= self.placer.access_cell(cell)
        return val

    def __convert_to_collapsed(self, board: np.ndarray) -> np.ndarray:
        return np.array([
            [self.__unpack_state(cell) for cell in row]
            for row in board
        ])

    def __call__(
            self,
            width: int,
            height: int,
            init_vals: list) -> np.ndarray:
        board = self.__generate_board(width, height)
        self.__precollapse(board, init_vals)
        if (result := self.__collapse(board)) is not None:
            return self.__convert_to_collapsed(result)
        raise NoSolutionException()
