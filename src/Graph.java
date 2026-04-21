import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Grafo não direcionado com lista de adjacência.
 * V(), E(), addEdge(), adj(), degree() e toString().
 */
public class Graph {
    private final int V;
    private int E;
    private final List<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public Graph(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("O número de vértices deve ser não negativo.");
        }
        this.V = vertices;
        this.E = 0;
        this.adj = (List<Integer>[]) new ArrayList[vertices];
        for (int v = 0; v < vertices; v++) {
            this.adj[v] = new ArrayList<>();
        }
    }

    /**
     * Lê um grafo no formato:
     * V
     * E
     * v1 w1
     * v2 w2
     * ...
     */
    public Graph(String filePath) throws IOException {
        this(readFileAsInts(filePath));
    }

    /**
     * Construtor auxiliar usado pelo construtor que lê do arquivo.
     */
    public Graph(int[] data) {
        this(data[0]);

        int expectedEdges = data[1];
        int index = 2;
        for (int i = 0; i < expectedEdges; i++) {
            int v = data[index++];
            int w = data[index++];
            addEdge(v, w);
        }
    }

    private static int[] readFileAsInts(String filePath) throws IOException {
        List<Integer> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    values.add(Integer.parseInt(st.nextToken()));
                }
            }
        }

        if (values.size() < 2) {
            throw new IllegalArgumentException("Arquivo de grafo inválido: faltam V e E.");
        }

        int V = values.get(0);
        int E = values.get(1);
        if (values.size() != 2 + 2 * E) {
            throw new IllegalArgumentException(
                "Arquivo de grafo inválido: número de arestas não bate com o cabeçalho."
            );
        }

        int[] data = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            data[i] = values.get(i);
        }
        return data;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException(
                "Vértice " + v + " fora do intervalo [0, " + (V - 1) + "]."
            );
        }
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V).append(" vertices, ").append(E).append(" edges\n");
        for (int v = 0; v < V; v++) {
            sb.append(v).append(": ");
            for (int w : adj[v]) {
                sb.append(w).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
