import numpy as np


class FinalConstrains:
    def __init__(self, constant_limits):
        rl, cl, sl = constant_limits
        self.__row_limits = rl
        self.__column_limits = cl
        self.__ship_len_limits = sl

    @staticmethod
    def __row_constrain(
            row: np.ndarray,
            row_sum: int) -> bool:
        return len([val for val, _ in row if val > 1]) == row_sum

    @staticmethod
    def __column_constrain(
            column: np.ndarray,
            column_sum: int) -> bool:
        return len([val for val, _ in column if val > 1]) == column_sum

    def board_constrain(self, board: np.ndarray) -> bool:
        row_constrains = [self.__row_constrain(row, row_sum)
            for row, row_sum in zip(board, self.__row_limits)]
        column_constrains = [self.__column_constrain(board[:,i],
            column_sum) for i, column_sum in enumerate(
                self.__column_limits)]
        return all(row_constrains) and all(column_constrains)

    def ship_length_constrain(self, board: np.ndarray) -> bool:
        ships = set()
        for row in board:
            for _, ship in row:
                if ship:
                    ships.add(tuple(ship))
        ship_lengths = {}
        for ship in ships:
            if len(ship) not in ship_lengths.keys():
                ship_lengths[len(ship)] = 1
            else:
                ship_lengths[len(ship)] += 1
        return ship_lengths == self.__ship_len_limits

    def __call__(self, board: np.ndarray) -> bool:
        return (
            self.board_constrain(board) and
            self.ship_length_constrain(board)
        )
