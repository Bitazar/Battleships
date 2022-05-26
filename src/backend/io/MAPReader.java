/**
 * @author Mateusz Jaracz
 */
package backend.io;

import backend.utility.Coord;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reader of the MAP format
 */
public class MAPReader {

    /**
     * Reads the MAP format from the given buffer reader
     *
     * @param file the buffer reader
     * @return the parsed MAP format
     */
    public Map<Integer, Map<Coord, Set<Integer>>> read(BufferedReader file) {
        Map<Integer, Map<Coord, Set<Integer>>> data = new HashMap<>();
        Map<Coord, Set<Integer>> record = new HashMap<>();
        int recordTag = 0;
        for (String line : file.lines().toList()) {
            if (line.contains("}")) {
                data.put(recordTag, record);
                record = new HashMap<>();
            } else if (line.contains("{")) {
                recordTag = Integer.parseInt(line.substring(0, line.indexOf(":")));
            } else {
                String[] parts = line.split("->");
                record.put(parseCoord(parts[0]), parseSet(parts[1]));
            }
        }
        return data;
    }

    /**
     * Preprocesses the line used by the parsers
     *
     * @param line the raw line
     * @return the preprocessed line
     */
    private String[] preprocessLine(String line) {
        String stripped = line.strip();
        stripped = stripped.substring(1, stripped.length() - 1);
        return stripped.split("\\|");
    }

    /**
     * Parses the set of integers from the given string
     *
     * @param line the parsed string
     * @return the set of integers
     */
    private Set<Integer> parseSet(String line) {
        Set<Integer> set = new HashSet<>();
        for (String number : preprocessLine(line)) {
            set.add(Integer.parseInt(number));
        }
        return set;
    }

    /**
     * Parses the coordinates from the given string
     *
     * @param line the parsed string
     * @return the coordinates
     */
    private Coord parseCoord(String line) {
        String[] numbers = preprocessLine(line);
        return new Coord(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
    }

}
