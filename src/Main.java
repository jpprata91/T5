import java.io.IOException;
import java.util.Arrays;

public class Main {
    private static final String[] STATES = {
        "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA",
        "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN",
        "RO", "RR", "RS", "SC", "SE", "SP", "TO"
    };

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java Main dados/brasil.txt");
            return;
        }

        String filePath = args[0];

        try {
            Graph graph = new Graph(filePath);

            System.out.println("=== Lista de adjacência do grafo do Brasil ===");
            printAdjacencyList(graph);

            System.out.println("\n=== Execução do DSatur ===");
            GraphColoringDSatur.ColoringResult result = GraphColoringDSatur.color(graph);

            printColoringOrder(result);
            printFinalColors(result);

            System.out.println("\nTotal de cores utilizadas: " + result.colorsUsed);
            System.out.println("Coloração válida: " + (result.valid ? "SIM" : "NÃO"));

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro no grafo:" + e.getMessage());
        }
    }

    private static void printAdjacencyList(Graph graph) {
        for (int v = 0; v < graph.V(); v++) {
            System.out.print(STATES[v] + " (" + v + "): ");
            boolean first = true;
            for (int w : graph.adj(v)) {
                if (!first) {
                    System.out.print(", ");
                }
                System.out.print(STATES[w] + " (" + w + ")");
                first = false;
            }
            System.out.println();
        }
    }

    private static void printColoringOrder(GraphColoringDSatur.ColoringResult result) {
        System.out.println("Ordem de coloração:");
        for (int i = 0; i < result.order.size(); i++) {
            int vertex = result.order.get(i);
            System.out.printf("%2d. %s (%d) -> cor %d%n",
                    i + 1, STATES[vertex], vertex, result.colors[vertex]);
        }
    }

    private static void printFinalColors(GraphColoringDSatur.ColoringResult result) {
        System.out.println("\nCor final de cada estado:");
        for (int v = 0; v < STATES.length; v++) {
            System.out.printf("%s (%d): cor %d%n", STATES[v], v, result.colors[v]);
        }
    }
}
