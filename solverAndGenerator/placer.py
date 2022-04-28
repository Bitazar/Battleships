from abc import ABC, abstractmethod
from typing import Any

import numpy as np


class PlacerInterface(ABC):
    @abstractmethod
    def __call__(
            self,
            board: np.ndarray,
            position: tuple,
            value: Any) -> None:
        pass

    @abstractmethod
    def generate_cell(self, value: Any) -> Any:
        pass

    @abstractmethod
    def access_cell(self, cell: tuple) -> Any:
        pass


class BattleshipsPlacer(PlacerInterface):
    def __init__(self) -> None:
        pass

    @staticmethod
    def __on_board(board: np.ndarray, position: tuple) -> bool:
        x, y = position
        lx, ly, _ = board.shape
        return lx > x >= 0 and ly > y >= 0

    @classmethod
    def __ship_neighbourhood(cls, board: np.ndarray, position: tuple) -> list:
        x, y = position
        return [cell for cell in [
            (x, y - 1),
            (x, y + 1),
            (x + 1, y),
            (x - 1, y),
        ] if cls.__on_board(board, cell)]

    @staticmethod
    def __emplace_one_ship(board, position: tuple) -> bool:
        x, y = position
        board[y][x][1] = [position]

    @staticmethod
    def __lenghten_ship(board, position: tuple, ships: list) -> bool:
        ship ,= ships
        ship.append(position)
        x, y = position
        board[y][x][1] = ship

    @classmethod
    def __concatenate_ships(
            cls,
            board,
            position: tuple,
            ships: list,
            neighborhood: list) -> bool:
        neighborhood = [(x, y) for x, y in neighborhood if board[y][x][1] is not None]
        left, right = ships
        new_ship = left + [position] + right
        for x, y in neighborhood + [position]:
            board[y][x][1] = new_ship
        x, y = position

    def __emplace_ship(cls, board: np.ndarray, position: tuple) -> list:
        neighborhood = cls.__ship_neighbourhood(board, position)
        ships = [board[y][x][1] for x, y in neighborhood if board[y][x][1] is not None]
        if not ships:
            cls.__emplace_one_ship(board, position)
        elif len(ships) == 1:
            cls.__lenghten_ship(board, position, ships)
        elif len(ships) == 2:
            cls.__concatenate_ships(board, position, ships, neighborhood)

    def __call__(
            self,
            board: np.ndarray,
            position: tuple,
            value: Any) -> None:
        x, y = position
        if value == {1}:
            board[y][x][0] = value
        elif value == {2} or value == {3}:
            self.__emplace_ship(board, position)
            board[y][x][0] = value

    def generate_cell(self, value: Any) -> Any:
        return value, None

    def access_cell(self, cell: tuple) -> Any:
        return cell[0]

    def access_ship(self, cell: tuple) -> Any:
        return cell[1]
