package main;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

class Aresta {
    private String rotulo;

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public String toString() {
        return "Aresta [rotulo=" + rotulo + "]";
    }
}

class Vertice {
    private String rotulo;
    private boolean visitado;
    private final Set<Aresta> adj;

    public Vertice() {
        this.visitado = false;
        this.adj = new HashSet<>();
    }

    public boolean isVisitado() {
        return this.visitado;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public Set<Aresta> getAdj() {
        return adj;
    }

    public void addAresta(String rotulo) {
        Aresta a = new Aresta();
        a.setRotulo(rotulo);
        this.adj.add(a);
    }

    public void setVisitado() {
        this.visitado = true;
    }

    public void setNaoVisitado() {
        this.visitado = false;
    }

    @Override
    public String toString() {
        return "Vertice{" +
                "rotulo='" + rotulo + '\'' +
                ", visitado=" + visitado +
                ", listaArestas=" + adj +
                '}';
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

    @Override
    public String toString() {
        return "Pair{" +
                "verticeLabel='" + verticeLabel + '\'' +
                ", level=" + level +
                '}';
    }
}


class Grafo {
    private List<Vertice> listaVertices;

    public Grafo() {
        this.listaVertices = new ArrayList<>(10);
    }

    public List<Vertice> getListaVertices() {
        return listaVertices;
    }

    public void addVertice(String rotuloVertice) {
        Vertice v = new Vertice();
        v.setRotulo(rotuloVertice);
        this.listaVertices.add(v);
    }

    public void addAresta(Grafo g, String verticeRecebeAresta, String verticeParteAresta) {
        g.getListaVertices().forEach(v -> {
            if (v.getRotulo().equalsIgnoreCase(verticeParteAresta))
                v.addAresta(verticeRecebeAresta);
            if (v.getRotulo().equalsIgnoreCase(verticeRecebeAresta))
                v.addAresta(verticeParteAresta);
        });
    }

    public void setListaVertices(List<Vertice> verticeList) {
        this.listaVertices = verticeList;
    }


    public int buscaLargura(String rotuloVerticeInicial, String rotuloVerticeFinal) {
        Queue<Pair> q = new LinkedList<>();
        int distance;

        q.add(new Pair(rotuloVerticeInicial, 0));

        while (!q.isEmpty()) {
            String queueFront = q.peek().getVerticeLabel();
            distance = q.peek().getLevel();
            q.poll();

            if (queueFront.equalsIgnoreCase(rotuloVerticeFinal))
                return distance;

            Vertice vertex = this.getListaVertices().stream()
                    .filter(v2 -> v2.getRotulo().equalsIgnoreCase(queueFront)).toList().get(0);

            analyseEdgesAndVertexList(q, distance, vertex);
        }
        return 0;
    }

    private void analyseEdgesAndVertexList(Queue<Pair> q, int distance, Vertice vertex) {
        vertex.getAdj().forEach(aresta ->

                this.getListaVertices().forEach(vertice -> {

                    if (aresta.getRotulo().equalsIgnoreCase(vertice.getRotulo()) && !vertice.isVisitado()) {
                        q.add(new Pair(vertice.getRotulo(), distance + 1));
                        vertice.setVisitado();
                    }
                })
        );
    }

    public void abreVertices() {
        this.getListaVertices().forEach(Vertice::setNaoVisitado);
    }

    @Override
    public String toString() {
        return "Grafo{" +
                "listaVertices=" + listaVertices +
                '}';
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grafo graph = new Grafo();
        int edges;
        int tot;

        String aux = sc.nextLine();

        edges = Integer.parseInt(aux.split(" ")[1]);

        for (int i = 0; i < edges; ++i) {
            aux = sc.nextLine();

            String labelVertexOne = aux.split(" ")[0];
            String labelVertexTwo = aux.split(" ")[1];

            graph.addVertice(labelVertexOne);
            graph.addVertice(labelVertexTwo);

            graph.addAresta(graph, labelVertexOne, labelVertexTwo);
        }

        graph.setListaVertices(graph.getListaVertices().stream()
                .filter(Vertice.distinctByKey(Vertice::getRotulo))
                .sorted(Comparator.comparing(Vertice::getRotulo)).toList());

        tot = graph.buscaLargura("Entrada", "*");
        graph.abreVertices();
        tot += graph.buscaLargura("*", "Saida");
        System.out.println(tot);
    }
}
