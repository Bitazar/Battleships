from dataclasses import dataclass


@dataclass(frozen=True)
class BattleshipsPreprocessor:
    width: int
    height: int

    def __on_board(self, position: tuple) -> bool:
        x, y = position
        return self.width > x >= 0 and self.height > y >= 0

    def __left_vectorized(self, position: tuple) -> list:
        x, y = position
        return [(u, v, 1) for u, v in [
            (x, y - 1), (x + 1, y), (x, y + 1),
        ] if self.__on_board((u, v))] + [(x - 1, y, 2)]

    def __up_vectorized(self, position: tuple) -> list:
        x, y = position
        return [(u, v, 1) for u, v in [
            (x - 1, y), (x + 1, y), (x, y + 1),
        ] if self.__on_board((u, v))] + [(x, y - 1, 2)]

    def __right_vectorized(self, position: tuple) -> list:
        x, y = position
        return [(u, v, 1) for u, v in [
            (x, y - 1), (x - 1, y), (x, y + 1),
        ] if self.__on_board((u, v))] + [(x + 1, y, 2)]

    def __down_vectorized(self, position: tuple) -> list:
        x, y = position
        return [(u, v, 1) for u, v in [
            (x, y - 1), (x - 1, y), (x + 1, y),
        ] if self.__on_board((u, v))] + [(x, y + 1, 2)]

    def __single_vectorized(self, position: tuple) -> list:
        x, y = position
        return [(u, v, 1) for u, v in [
            (x, y - 1), (x - 1, y), (x + 1, y), (x, y + 1),
        ] if self.__on_board((u, v))]

    def __vectorized(self, position: tuple, value: int) -> list:
        if value == 3:
            return self.__left_vectorized(position)
        if value == 4:
            return self.__up_vectorized(position)
        if value == 5:
            return self.__right_vectorized(position)
        if value == 6:
            return self.__down_vectorized(position)
        if value == 7:
            return self.__single_vectorized(position)
        return []

    def __call__(self, initial_values: list) -> list:
        extended_values = []
        for x, y, value in initial_values:
            if value > 2:
                extended_values.append((x, y, 2))
                extended_values.extend(self.__vectorized((x, y), value))
            else:
                extended_values.append((x, y, value))
        return extended_values
