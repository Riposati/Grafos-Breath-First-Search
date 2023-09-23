package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
    private List<Aresta> listaArestas;

    public Vertice() {
        this.visitado = false;
        this.listaArestas = new ArrayList<>(10);
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

    public List<Aresta> getListaArestas() {
        return listaArestas;
    }

    public void addAresta(String rotulo) {

        Aresta a = new Aresta();
        a.setRotulo(rotulo);
        this.listaArestas.add(a);
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
                ", listaArestas=" + listaArestas +
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

        boolean flag = false;
        boolean flag2 = false;

        int x = 0;
        for (int i = 0; i < g.getListaVertices().size(); i++) {

            if (verticeRecebeAresta.equalsIgnoreCase(g.getListaVertices().get(i).getRotulo())) {
                x = i;
                flag = true;
                break;
            }
        }

        int t = 0;
        for (int i = 0; i < g.getListaVertices().size(); i++) {

            if (verticeParteAresta.equalsIgnoreCase(g.getListaVertices().get(i).getRotulo())) {
                t = i;
                flag2 = true;
                break;
            }
        }

        if (flag && flag2) {
            g.getListaVertices().get(x).addAresta(verticeParteAresta);
            g.getListaVertices().get(t).addAresta(verticeRecebeAresta);
        }
    }

    public void buscaLargura(String rotuloVerticeInicial) {
        Queue<Vertice> fila = new LinkedList<>();

        Vertice verticeInicial = this.getListaVertices().stream()
                .filter(v -> v.getRotulo().equalsIgnoreCase(rotuloVerticeInicial)).toList().get(0);

        verticeInicial.setVisitado();
        fila.add(verticeInicial);

        while (!fila.isEmpty()) {

            Vertice v = fila.poll();

            for (int i = 0; i < v.getListaArestas().size(); i++) {

                Aresta aresta = v.getListaArestas().get(i);

                for (int j = 0; j < this.getListaVertices().size(); j++) {

                    if (aresta.getRotulo().equalsIgnoreCase(this.getListaVertices().get(j).getRotulo())) {

                        Vertice v3 = this.getListaVertices().get(j);

                        if (!v3.isVisitado()) {
                            fila.add(v3);
                            v3.setVisitado();
                            System.out.print(v3.getRotulo() + " ");
                            break;
                        }
                    }
                }
            }
        }
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

        Grafo grafo = new Grafo();

        grafo.addVertice("A");
        grafo.addVertice("F");
        grafo.addVertice("C");

        grafo.addVertice("B");
        grafo.addVertice("D");
        grafo.addVertice("J");
        grafo.addVertice("H");

        grafo.addAresta(grafo, "A", "F");
        grafo.addAresta(grafo, "C", "B");
        grafo.addAresta(grafo, "D", "J");
        grafo.addAresta(grafo, "B", "H");
        grafo.addAresta(grafo, "B", "D");
        grafo.addAresta(grafo, "A", "C");
        grafo.addAresta(grafo, "F", "B");
        grafo.addAresta(grafo, "F", "J");
        grafo.addAresta(grafo, "J", "H");

        grafo.buscaLargura("D");

//        System.out.println(grafo);
    }
}
