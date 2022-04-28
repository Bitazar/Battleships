from abc import ABC, abstractmethod
from typing import Any

from placer import BattleshipsPlacer

import numpy as np


class Constrains(ABC):
    def __init__(
            self,
            constant_limits: list,
            placer: BattleshipsPlacer):
        rl, cl, sl = constant_limits
        self._row_limits = rl
        self._column_limits = cl
        self._ship_len_limits = sl
        self._placer = placer

    def _contained(self, row: np.ndarray) -> tuple:
        ships, probs = 0, 0
        for cell in row:
            cell = self._placer.access_cell(cell)
            if len(cell) == 1:
                ships += len({2, 3}.intersection(cell)) > 0
            else:
                probs += len({2, 3}.intersection(cell)) > 0
        return ships, probs

    @abstractmethod
    def _row_constrain(self, row: np.ndarray, row_sum: int) -> bool:
        pass

    def board_constrain(self, board: np.ndarray) -> bool:
        row_constrains = [self._row_constrain(row, row_sum)
            for row, row_sum in zip(board, self._row_limits)]
        column_constrains = [self._row_constrain(board[:,i],
            column_sum) for i, column_sum in enumerate(
                self._column_limits)]
        return all(row_constrains) and all(column_constrains)

    def _get_ship_lengths(self, board: np.ndarray) -> bool:
        ships = set()
        for row in board:
            for element in row:
                if (ship := self._placer.access_ship(element)) is not None:
                    ships.add(tuple(ship))
        ship_lengths = {}
        for ship in ships:
            if len(ship) not in ship_lengths.keys():
                ship_lengths[len(ship)] = 1
            else:
                ship_lengths[len(ship)] += 1
        return ship_lengths

    @abstractmethod
    def ship_length_constrain(self, board: np.ndarray) -> bool:
        pass

    def __call__(self, board: np.ndarray) -> bool:
        return (
            self.board_constrain(board) and
            self.ship_length_constrain(board)
        )
