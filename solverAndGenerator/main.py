from battleships_solver import BattleshipsSolver
from placer import BattleshipsPlacer
from battleships_generator import BattleshipsGenerator

import numpy as np

SPACE = [1, 2]

INITS_1 = [(2, 2, 4)]
ROWS_1 = np.array([3, 1, 2, 3, 0, 1])
COLS_1 = np.array([3, 0, 3, 0, 1, 3])

INITS_2 = [(3, 0, 6)]
ROWS_2 = np.array([1, 2, 0, 5, 0, 2])
COLS_2 = np.array([1, 1, 2, 2, 1, 3])

INITS_3 = [(5, 4, 7), (2, 2, 1)]
ROWS_3 = [3, 0, 3, 1, 1, 2]
COLS_3 = [3, 0, 1, 3, 2, 1]

SHIP_SIZES = {
    1: 3,
    2: 2,
    3: 1
}

# 3 -> lewo
# 4 -> góra
# 5 -> prawo
# 6 -> dół

# 7 -> pojedynczy
# 8 -> środek

def main():
    placer = BattleshipsPlacer()
    solver = BattleshipsSolver(SPACE, placer)
    # solver.print_solutions(INITS_1, ROWS_1, COLS_1, SHIP_SIZES)
    # solver.print_solutions(INITS_2, ROWS_2, COLS_2, SHIP_SIZES)
    # solver.print_solutions(INITS_3, ROWS_3, COLS_3, SHIP_SIZES)
    generator = BattleshipsGenerator(6, 6, SHIP_SIZES)
    brd = generator(placer)
    print(brd)
    solver.print_solutions(*brd, SHIP_SIZES)

if __name__ == '__main__':
    main()
