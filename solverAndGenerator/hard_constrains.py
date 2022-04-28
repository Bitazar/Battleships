import numpy as np

from placer import BattleshipsPlacer
from constrains import Constrains


class HardConstrains(Constrains):
    def __init__(
            self,
            constant_limits: list,
            placer: BattleshipsPlacer):
        super().__init__(constant_limits, placer)

    def _row_constrain(
            self,
            row: np.ndarray,
            row_sum: int) -> bool:
        ships, probs = self._contained(row)
        return ships == row_sum and probs == 0

    def ship_length_constrain(self, board: np.ndarray) -> bool:
        return self._get_ship_lengths(board) == self._ship_len_limits
