from abc import ABC, abstractmethod
from typing import Any

import numpy as np

from solver import Solver


# Callable in javia
class States(ABC):
    @abstractmethod
    def __call__(
            self,
            solver: Solver,    # interfejs solvera
            board: np.ndarray,
            position: tuple,
            diff: tuple) -> Any:
        pass


class BattleshipsStates(States):
    def __init__(self) -> None:
        pass

    def __call__(
            self,
            solver: Solver,
            board: np.ndarray,
            position: tuple,
            diff: tuple) -> Any:
        x, y = position
        nx, ny = x + diff[0], y + diff[1]
        states = solver.placer.access_cell(board[ny][nx])
        for state in solver.placer.access_cell(board[y][x]):
            states = states.intersection(
                solver.constrains[state][diff])
        return states
