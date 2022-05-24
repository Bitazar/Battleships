package backend.facades;

import backend.boards.BoardDTO;
import backend.boards.GeneratorBoard;
import backend.constrains.HardConstrains;
import backend.constrains.SoftConstrains;
import backend.solvers.NoSolutionException;
import backend.solvers.WaveFunctionCollapse;
import backend.states.BattleshipsStates;
import backend.states.States;
import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.*;

public class BattleshipsSolver {
    private final Map<Integer, Map<Coord, Set<Integer>>>        constrains;
    private final States                                        states = new BattleshipsStates();

    public BattleshipsSolver(Map<Integer, Map<Coord, Set<Integer>>> constrains) {
        this.constrains = constrains;
    }

    private void addZeroRows(Set<InitValue> preprocessed, List<Integer> rowLimits, List<Integer> columnLimits) {
        for (int y = 0; y < rowLimits.size(); ++y) {
            if (rowLimits.get(y) == 0) {
                for (int x = 0; x < columnLimits.size(); ++x) {
                    preprocessed.add(new InitValue(new Coord(x, y), 1));
                }
            }
        }
    }

    private void addZeroColumns(Set<InitValue> preprocessed, List<Integer> rowLimits, List<Integer> columnLimits) {
        for (int x = 0; x < columnLimits.size(); ++x) {
            if (columnLimits.get(x) == 0) {
                for (int y = 0; y < rowLimits.size(); ++y) {
                    preprocessed.add(new InitValue(new Coord(x, y), 1));
                }
            }
        }
    }

    private List<InitValue> extendInitialValues(List<Integer> rowLimits, List<Integer> columnLimits, List<InitValue> initValueList) {
        BattleshipsPreprocessor preprocessor = new BattleshipsPreprocessor(new Coord(columnLimits.size(), rowLimits.size()));
        Set<InitValue> preprocessed = new HashSet<>(preprocessor.preprocess(initValueList));
        addZeroRows(preprocessed, rowLimits, columnLimits);
        addZeroColumns(preprocessed, rowLimits, columnLimits);
        return new ArrayList<>(preprocessed);
    }

    public BoardDTO solve(List<InitValue> initValueList, List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) throws NoSolutionException {
        SoftConstrains soft = new SoftConstrains(rowLimits, columnLimits, shipLimits);
        HardConstrains hard = new HardConstrains(rowLimits, columnLimits, shipLimits);
        WaveFunctionCollapse solver = new WaveFunctionCollapse(soft, hard, constrains, states);
        return (BoardDTO) solver.solve(new GeneratorBoard(new Coord(columnLimits.size(), rowLimits.size())),
            extendInitialValues(rowLimits, columnLimits, initValueList));
    }

}
