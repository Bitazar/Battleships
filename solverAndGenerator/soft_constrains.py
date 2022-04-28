import numpy as np

from placer import BattleshipsPlacer
from constrains import Constrains


class SoftConstrains(Constrains):
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
        return ships <= row_sum and ships + probs >= row_sum

    def ship_length_constrain(self, board: np.ndarray):
        ship_lengths = self._get_ship_lengths(board)
        for key in sorted(self._ship_len_limits.keys(), reverse=True):
            if key not in ship_lengths.keys():
                return True
            if ship_lengths[key] != self._ship_len_limits[key]:
                return ship_lengths[key] < self._ship_len_limits[key]
        return True
