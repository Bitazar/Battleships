from abc import ABC, abstractmethod
from typing import Any

import numpy as np

from constrains import Constrains
from placer import PlacerInterface


class Solver:
    def __init__(
            self,
            constrains: list,
            hard_constrains: Constrains,
            soft_constrains: Constrains,
            placer: PlacerInterface) -> None:
        self.__constrains = {}
        for index, states in constrains:
            self.__constrains[index] = states
        self.__hard_constrains = hard_constrains
        self.__soft_constrains = soft_constrains
        self.__placer = placer

    @property
    def constrains(self) -> map:
        return self.__constrains

    @property
    def hard_constrains(self) -> Constrains:
        return self.__hard_constrains

    @property
    def soft_constrains(self) -> Constrains:
        return self.__soft_constrains

    @property
    def placer(self) -> PlacerInterface:
        return self.__placer

    @abstractmethod
    def __call__(
            self,
            width: int,
            height: int,
            init_vals: list) -> np.ndarray:
        pass
