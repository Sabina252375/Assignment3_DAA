
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RouteTest {
    public static void main(String[] args) {
        int[][] edges = {
            {0, 1, 3},   // edge ID 0
            {1, 2, -7},  // edge ID 1
            {2, 3, 4},   // edge ID 2
            {0, 2, -4},  // edge ID 3: equal-cost alternative
            {1, 2, -7}   // edge ID 4: parallel edge
        };

        int[] parentEdge = {-1, 0, 1, 2};

        check(
            "Reachable target",
            Algorithms.reconstructRoute(0, 3, edges, parentEdge),
            Arrays.asList(0, 1, 2, 3)
        );

        check(
            "Source equals target",
            Algorithms.reconstructRoute(0, 0, edges, parentEdge),
            Arrays.asList(0)
        );

        int[] unreachableParents = {-1, 0, -1, -1};

        check(
            "Unreachable target",
            Algorithms.reconstructRoute(0, 3, edges, unreachableParents),
            Collections.emptyList()
        );

        int[] parallelParents = {-1, 0, 4, 2};

        check(
            "Parallel edge ID",
            Algorithms.reconstructRoute(0, 3, edges, parallelParents),
            Arrays.asList(0, 1, 2, 3)
        );

        System.out.println("All route tests passed.");
    }

    private static void check(
        String name,
        List<Integer> actual,
        List<Integer> expected
    ) {
        if (!actual.equals(expected)) {
            throw new AssertionError(
                name + ": expected " + expected + ", got " + actual
            );
        }

        System.out.println("PASS: " + name + " -> " + actual);
    }
}