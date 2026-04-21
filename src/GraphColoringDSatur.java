import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphColoringDSatur {

    public static class ColoringResult {
        public final int[] colors;
        public final List<Integer> order;
        public final int colorsUsed;
        public final boolean valid;

        public ColoringResult(int[] colors, List<Integer> order, int colorsUsed, boolean valid) {
            this.colors = colors;
            this.order = order;
            this.colorsUsed = colorsUsed;
            this.valid = valid;
        }
    }

    /**
     * Aplica o algoritmo DSatur em um grafo não direcionado
     * A coloração começa em 1, como pede o enunciado
     */
    @SuppressWarnings("unchecked")
    public static ColoringResult color(Graph graph) {
        int n = graph.V();
        int[] colors = new int[n];
        Arrays.fill(colors, -1);

        boolean[] colored = new boolean[n];
        Set<Integer>[] adjacentColors = (Set<Integer>[]) new HashSet[n];
        for (int v = 0; v < n; v++) {
            adjacentColors[v] = new HashSet<>();
        }

        List<Integer> order = new ArrayList<>();
        int colorsUsed = 0;

        // 1) Primeiro vértice: maior grau.
        int first = chooseHighestDegreeVertex(graph, colored);
        applyColor(graph, first, 1, colors, colored, adjacentColors);
        order.add(first);
        colorsUsed = 1;

        // 2) Repetir até colorir todos os vértices.
        while (order.size() < n) {
            int next = chooseNextVertex(graph, colored, adjacentColors);
            int chosenColor = smallestAvailableColor(adjacentColors[next]);

            applyColor(graph, next, chosenColor, colors, colored, adjacentColors);
            order.add(next);
            colorsUsed = Math.max(colorsUsed, chosenColor);
        }

        boolean valid = validate(graph, colors);
        return new ColoringResult(colors, order, colorsUsed, valid);
    }

    private static int chooseHighestDegreeVertex(Graph graph, boolean[] colored) {
        int bestVertex = -1;
        int bestDegree = -1;

        for (int v = 0; v < graph.V(); v++) {
            if (colored[v]) {
                continue;
            }
            int degree = graph.degree(v);

            // Em caso de empate, escolhemos o menor índice para deixar a saída determinística.
            if (degree > bestDegree || (degree == bestDegree && v < bestVertex)) {
                bestDegree = degree;
                bestVertex = v;
            }
        }
        return bestVertex;
    }

    private static int chooseNextVertex(Graph graph, boolean[] colored, Set<Integer>[] adjacentColors) {
        int bestVertex = -1;
        int bestSaturation = -1;
        int bestDegree = -1;

        for (int v = 0; v < graph.V(); v++) {
            if (colored[v]) {
                continue;
            }

            int saturation = adjacentColors[v].size();
            int degree = graph.degree(v);

            if (saturation > bestSaturation
                || (saturation == bestSaturation && degree > bestDegree)
                || (saturation == bestSaturation && degree == bestDegree && v < bestVertex)) {
                bestSaturation = saturation;
                bestDegree = degree;
                bestVertex = v;
            }
        }

        return bestVertex;
    }

    private static int smallestAvailableColor(Set<Integer> usedColors) {
        int color = 1;
        while (usedColors.contains(color)) {
            color++;
        }
        return color;
    }

    private static void applyColor(Graph graph,
                                   int vertex,
                                   int color,
                                   int[] colors,
                                   boolean[] colored,
                                   Set<Integer>[] adjacentColors) {
        colors[vertex] = color;
        colored[vertex] = true;

        // Toda vez que um vértice recebe uma cor, essa cor passa a contar
        // na saturação dos vizinhos que ainda não foram coloridos.
        for (int neighbor : graph.adj(vertex)) {
            if (!colored[neighbor]) {
                adjacentColors[neighbor].add(color);
            }
        }
    }

    /**
     * Verifica se dois vértices adjacentes nunca receberam a mesma cor.
     */
    public static boolean validate(Graph graph, int[] colors) {
        for (int v = 0; v < graph.V(); v++) {
            for (int w : graph.adj(v)) {
                if (v < w && colors[v] == colors[w]) {
                    return false;
                }
            }
        }
        return true;
    }
}
