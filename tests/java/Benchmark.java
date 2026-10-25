import java.util.*;

public class Benchmark {
    public static void main(String[] args) {
        int[] sizes = {500, 1000, 2000};
        Random random = new Random(42);

        for (int n : sizes) {
            List<int[]> list = new ArrayList<>();

            for (int i = 0; i < n * 4; i++) {
                int u = random.nextInt(n);
                int v = random.nextInt(n);
                int w = random.nextInt(1000);
                list.add(new int[]{u, v, w});
            }

            Map<String, Object> input = new HashMap<>();
            input.put("n", n);
            input.put("edges", list.stream()
                .map(row -> Arrays.stream(row).boxed().toList())
                .toList());

            long[] kruskalTimes = new long[3];
            long[] primTimes = new long[3];

            for (int i = 0; i < 3; i++) {
                long start = System.nanoTime();
                Map<String, Object> k = Algorithms.kruskal(input);
                kruskalTimes[i] = System.nanoTime() - start;

                start = System.nanoTime();
                Map<String, Object> p = Algorithms.prim(input);
                primTimes[i] = System.nanoTime() - start;

                if (!k.get("cost").equals(p.get("cost"))
                        || !k.get("components").equals(p.get("components"))) {
                    throw new RuntimeException("Results do not match!");
                }
            }

            Arrays.sort(kruskalTimes);
            Arrays.sort(primTimes);

            System.out.println("Vertices: " + n + ", edges: " + n * 4);
            System.out.println("Kruskal median: " + kruskalTimes[1] / 1_000_000.0 + " ms");
            System.out.println("Prim median: " + primTimes[1] / 1_000_000.0 + " ms");
            System.out.println();
        }
    }
}