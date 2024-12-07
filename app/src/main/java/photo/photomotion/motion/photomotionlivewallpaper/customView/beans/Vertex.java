package photo.photomotion.motion.photomotionlivewallpaper.customView.beans;

public class Vertex {

    public Point f153p1;
    public Point f154p2;

    public static class PacoteDistanciaVertice implements Comparable<PacoteDistanciaVertice> {
        public double distance;
        public Vertex edge;

        public PacoteDistanciaVertice(Vertex vertice, double d) {
            this.edge = vertice;
            this.distance = d;
        }

        public int compareTo(PacoteDistanciaVertice pacoteDistanciaVertice) {
            return Double.compare(this.distance, pacoteDistanciaVertice.distance);
        }
    }

    public Vertex(Point ponto, Point ponto2) {
        this.f153p1 = ponto;
        this.f154p2 = ponto2;
    }

    public float coeficienteAngular() {
        return (this.f154p2.getYInit() - this.f153p1.getYInit()) / (this.f154p2.getXInit() - this.f153p1.getXInit());
    }

    public boolean equals(Object obj) {
        Vertex vertice = (Vertex) obj;
        return (vertice.f153p1.equals(this.f153p1) || vertice.f153p1.equals(this.f154p2)) && (vertice.f154p2.equals(this.f153p1) || vertice.f154p2.equals(this.f154p2));
    }
}
