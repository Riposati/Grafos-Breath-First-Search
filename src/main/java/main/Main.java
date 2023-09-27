package main;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

class Edge {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Vertex {
    private String name;
    private boolean visited;
    private final Set<Edge> adj;

    public Vertex() {
        this.visited = false;
        this.adj = new HashSet<>();
    }

    public boolean isVisited() {
        return this.visited;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Edge> getAdj() {
        return adj;
    }

    public void addEdge(String name) {
        Edge a = new Edge();
        a.setName(name);
        this.adj.add(a);
    }

    public void setVisited() {
        this.visited = true;
    }

    public void setNotVisited() {
        this.visited = false;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

class Pair {
    private final String verticeLabel;
    private final int level;

    public Pair(String verticeLabel, int level) {
        this.verticeLabel = verticeLabel;
        this.level = level;
    }

    public String getVerticeLabel() {
        return verticeLabel;
    }

    public int getLevel() {
        return level;
    }
}

class Graph {
    private List<Vertex> vertexList;

    public Graph() {
        this.vertexList = new ArrayList<>(10);
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public void addVertex(String vertexName) {
        Vertex v = new Vertex();
        v.setName(vertexName);
        this.vertexList.add(v);
    }

    public void addEdge(Graph g, String verticeRecebeAresta, String verticeParteAresta) {
        g.getVertexList().forEach(v -> {
            if (v.getName().equalsIgnoreCase(verticeParteAresta))
                v.addEdge(verticeRecebeAresta);
            if (v.getName().equalsIgnoreCase(verticeRecebeAresta))
                v.addEdge(verticeParteAresta);
        });
    }

    public void setVertexList(List<Vertex> verticeList) {
        this.vertexList = verticeList;
    }


    public int breathFirstSearch(String rotuloVerticeInicial, String rotuloVerticeFinal) {
        Queue<Pair> q = new LinkedList<>();
        int distance;

        q.add(new Pair(rotuloVerticeInicial, 0));

        while (!q.isEmpty()) {
            String queueFront = q.peek().getVerticeLabel();
            distance = q.peek().getLevel();
            q.poll();

            if (queueFront.equalsIgnoreCase(rotuloVerticeFinal))
                return distance;

            Vertex vertex = this.getVertexList().stream()
                    .filter(v2 -> v2.getName().equalsIgnoreCase(queueFront)).toList().get(0);

            analyseEdgesAndVertexList(q, distance, vertex);
        }
        return 0;
    }

    private void analyseEdgesAndVertexList(Queue<Pair> q, int distance, Vertex vertex) {
        vertex.getAdj().forEach(edge ->

            this.getVertexList().forEach(v -> {

                if (edge.getName().equalsIgnoreCase(v.getName()) && !v.isVisited()) {
                    q.add(new Pair(v.getName(), distance + 1));
                    v.setVisited();
                }
            })
        );
    }

    public void abreVertices() {
        this.getVertexList().forEach(Vertex::setNotVisited);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Graph graph = new Graph();
        int edges;
        int tot;

        String aux = sc.nextLine();
        edges = Integer.parseInt(aux.split(" ")[1]);

        for (int i = 0; i < edges; ++i) {
            aux = sc.nextLine();

            String labelVertexOne = aux.split(" ")[0];
            String labelVertexTwo = aux.split(" ")[1];

            graph.addVertex(labelVertexOne);
            graph.addVertex(labelVertexTwo);

            graph.addEdge(graph, labelVertexOne, labelVertexTwo);
        }

        graph.setVertexList(graph.getVertexList().stream()
                .filter(Vertex.distinctByKey(Vertex::getName))
                .sorted(Comparator.comparing(Vertex::getName)).toList());

        tot = graph.breathFirstSearch("Entrada", "*");
        graph.abreVertices();
        tot += graph.breathFirstSearch("*", "Saida");
        System.out.println(tot);
    }
}
