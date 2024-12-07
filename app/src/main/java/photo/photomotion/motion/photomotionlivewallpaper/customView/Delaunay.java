package photo.photomotion.motion.photomotionlivewallpaper.customView;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.triangleBitmap;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Vertex;

public class Delaunay {
    public Bitmap imagemDelaunay;
    public List<Point> listaPontos = new CopyOnWriteArrayList();
    public List<triangleBitmap> listaTriangulos = new CopyOnWriteArrayList();

    public Delaunay(Bitmap bitmap) {
        this.imagemDelaunay = bitmap;
        this.listaTriangulos.addAll(triangleBitmap.cutInitialBitmap(bitmap));
    }

    private List<triangleBitmap> flipTriangulos(triangleBitmap trianguloBitmap, triangleBitmap trianguloBitmap2) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        this.listaTriangulos.remove(trianguloBitmap);
        this.listaTriangulos.remove(trianguloBitmap2);
        trianguloBitmap.clear();
        trianguloBitmap2.clear();
        Vertex arestaComum = trianguloBitmap.getArestaComum(trianguloBitmap2);
        Point pontoForaVertice = trianguloBitmap.getPontoForaVertice(arestaComum);
        Point pontoForaVertice2 = trianguloBitmap2.getPontoForaVertice(arestaComum);
        triangleBitmap trianguloBitmap3 = new triangleBitmap(this.imagemDelaunay, pontoForaVertice, arestaComum.f153p1, pontoForaVertice2);
        triangleBitmap trianguloBitmap4 = new triangleBitmap(this.imagemDelaunay, pontoForaVertice, arestaComum.f154p2, pontoForaVertice2);
        this.listaTriangulos.add(trianguloBitmap3);
        this.listaTriangulos.add(trianguloBitmap4);
        copyOnWriteArrayList.add(trianguloBitmap3);
        copyOnWriteArrayList.add(trianguloBitmap4);
        return copyOnWriteArrayList;
    }

    private triangleBitmap getTrianguloVizinho(triangleBitmap trianguloBitmap, Vertex vertice) {
        for (triangleBitmap trianguloBitmap2 : this.listaTriangulos) {
            if (!trianguloBitmap2.equals(trianguloBitmap) && trianguloBitmap2.contemVertice(vertice.f153p1, vertice.f154p2)) {
                return trianguloBitmap2;
            }
        }
        return null;
    }

    private boolean isArestaIlegal(triangleBitmap trianguloBitmap, Point ponto) {
        return Boolean.valueOf(trianguloBitmap.pontoNoCircunscrito(ponto)).booleanValue();
    }

    private void legalizeAresta(Point ponto, triangleBitmap trianguloBitmap, Vertex vertice) {
        triangleBitmap trianguloVizinho = getTrianguloVizinho(trianguloBitmap, vertice);
        if (trianguloVizinho != null) {
            Point pontoForaVertice = trianguloVizinho.getPontoForaVertice(vertice);
            double anguloPontoOpostoAresta = trianguloVizinho.getAnguloPontoOpostoAresta(vertice) + trianguloBitmap.getAnguloPontoOpostoAresta(vertice);
            if (isArestaIlegal(trianguloBitmap, pontoForaVertice) || anguloPontoOpostoAresta > 3.141592653589793d) {
                List flipTriangulos = flipTriangulos(trianguloVizinho, trianguloBitmap);
                Vertex vertice2 = new Vertex(ponto, pontoForaVertice);
                Point pontoForaVertice2 = ((triangleBitmap) flipTriangulos.get(0)).getPontoForaVertice(vertice2);
                Point pontoForaVertice3 = ((triangleBitmap) flipTriangulos.get(1)).getPontoForaVertice(vertice2);
                legalizeAresta(ponto, (triangleBitmap) flipTriangulos.get(0), new Vertex(pontoForaVertice2, pontoForaVertice));
                legalizeAresta(ponto, (triangleBitmap) flipTriangulos.get(1), new Vertex(pontoForaVertice, pontoForaVertice3));
            }
        }
    }

    public void addPonto(Point ponto) {
        triangleBitmap trianguloBitmap;
        this.listaPontos.add(ponto);
        Iterator it = this.listaTriangulos.iterator();
        while (true) {
            if (!it.hasNext()) {
                trianguloBitmap = null;
                break;
            }
            trianguloBitmap = (triangleBitmap) it.next();
            if (trianguloBitmap != null && trianguloBitmap.contemPontoDentro(ponto)) {
                break;
            }
        }
        if (trianguloBitmap != null) {
            triangleBitmap trianguloBitmap2 = new triangleBitmap(this.imagemDelaunay, trianguloBitmap.getP1(), trianguloBitmap.getP2(), ponto);
            triangleBitmap trianguloBitmap3 = new triangleBitmap(this.imagemDelaunay, trianguloBitmap.getP2(), trianguloBitmap.getP3(), ponto);
            triangleBitmap trianguloBitmap4 = new triangleBitmap(this.imagemDelaunay, trianguloBitmap.getP3(), trianguloBitmap.getP1(), ponto);
            this.listaTriangulos.add(trianguloBitmap2);
            this.listaTriangulos.add(trianguloBitmap3);
            this.listaTriangulos.add(trianguloBitmap4);
            this.listaTriangulos.remove(trianguloBitmap);
            legalizeAresta(ponto, trianguloBitmap2, new Vertex(trianguloBitmap.getP1(), trianguloBitmap.getP2()));
            legalizeAresta(ponto, trianguloBitmap3, new Vertex(trianguloBitmap.getP2(), trianguloBitmap.getP3()));
            legalizeAresta(ponto, trianguloBitmap4, new Vertex(trianguloBitmap.getP3(), trianguloBitmap.getP1()));
        }
    }

    public void addPontoEstudo(Point ponto) {
        triangleBitmap trianguloBitmap;
        triangleBitmap trianguloBitmap2 = null;
        this.listaPontos.add(ponto);
        Iterator it = this.listaTriangulos.iterator();
        while (true) {
            if (!it.hasNext()) {
                trianguloBitmap = null;
                break;
            }
            trianguloBitmap = (triangleBitmap) it.next();
            if (trianguloBitmap.contemPontoDentro(ponto)) {
                break;
            }
        }
        if (trianguloBitmap != null) {
            CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
            triangleBitmap trianguloBitmap3 = new triangleBitmap(this.imagemDelaunay, trianguloBitmap.getP1(), trianguloBitmap.getP2(), ponto);
            triangleBitmap trianguloBitmap4 = new triangleBitmap(this.imagemDelaunay, trianguloBitmap.getP2(), trianguloBitmap.getP3(), ponto);
            triangleBitmap trianguloBitmap5 = new triangleBitmap(this.imagemDelaunay, trianguloBitmap.getP3(), trianguloBitmap.getP1(), ponto);
            copyOnWriteArrayList.add(trianguloBitmap3);
            copyOnWriteArrayList.add(trianguloBitmap4);
            copyOnWriteArrayList.add(trianguloBitmap5);
            this.listaTriangulos.add(trianguloBitmap3);
            this.listaTriangulos.add(trianguloBitmap4);
            this.listaTriangulos.add(trianguloBitmap5);
            this.listaTriangulos.remove(trianguloBitmap);
            trianguloBitmap.clear();
            for (triangleBitmap trianguloBitmap6 : this.listaTriangulos) {
                Iterator it2 = copyOnWriteArrayList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    trianguloBitmap2 = (triangleBitmap) it2.next();
                    if (trianguloBitmap6 != trianguloBitmap2 && trianguloBitmap6.eVizinho(trianguloBitmap2)) {
                        Vertex arestaComum = trianguloBitmap6.getArestaComum(trianguloBitmap2);
                        trianguloBitmap6.getAnguloPontoOpostoAresta(arestaComum);
                        trianguloBitmap2.getAnguloPontoOpostoAresta(arestaComum);
                        Point pontoForaVertice = trianguloBitmap6.getPontoForaVertice(arestaComum);
                        if (trianguloBitmap6.pontoNoCircunscrito(trianguloBitmap2.getPontoForaVertice(arestaComum)) || trianguloBitmap2.pontoNoCircunscrito(pontoForaVertice)) {
                            List flipTriangulos = flipTriangulos(trianguloBitmap6, trianguloBitmap2);
                            copyOnWriteArrayList.remove(trianguloBitmap2);
                            copyOnWriteArrayList.addAll(flipTriangulos);
                        }
                    }
                }
                List flipTriangulos2 = flipTriangulos(trianguloBitmap6, trianguloBitmap2);
                copyOnWriteArrayList.remove(trianguloBitmap2);
                copyOnWriteArrayList.addAll(flipTriangulos2);
            }
        }
        StringBuilder outline24 = new StringBuilder();
        outline24.append(this.listaTriangulos.size());
        Log.i("INFO", outline24.toString());
    }


    public void atualizarTriangulos() {
        CopyOnWriteArrayList<Point> copyOnWriteArrayList = new CopyOnWriteArrayList<>(this.listaPontos);
        this.listaTriangulos.clear();
        this.listaPontos.clear();
        this.listaTriangulos.addAll(triangleBitmap.cutInitialBitmap(this.imagemDelaunay));
        for (Point addPonto : copyOnWriteArrayList) {
            addPonto(addPonto);
        }
    }

    public void clear() {
        for (triangleBitmap clear : this.listaTriangulos) {
            clear.clear();
        }
        System.gc();
    }

    public void deletePontos(List<Point> list) {
        this.listaPontos.removeAll(list);
        atualizarTriangulos();
    }

    public Bitmap getImagemDelaunay() {
        return this.imagemDelaunay;
    }

    public List<Point> getListaPontos() {
        return this.listaPontos;
    }

    public List<triangleBitmap> getListaTriangulos() {
        return this.listaTriangulos;
    }

    public Bitmap getTriangulosEstaticos(Config config) {
        Bitmap createBitmap = Bitmap.createBitmap(this.imagemDelaunay.getWidth(), this.imagemDelaunay.getHeight(), config);
        Canvas canvas = new Canvas(createBitmap);
        for (triangleBitmap trianguloBitmap : getListaTriangulos()) {
            if (trianguloBitmap.isEstatico()) {
                trianguloBitmap.desenhaDistorcao(canvas, config);
            }
        }
        return createBitmap;
    }

    public void reiniciarPontos() {
        for (Point ponto : getListaPontos()) {
            if (!ponto.isEstatico()) {
                ponto.setPosicaoAtualAnim(ponto.getXInit(), ponto.getYInit());
            }
        }
    }

    public void setImagemDelaunay(Bitmap bitmap) {
        this.imagemDelaunay = bitmap;
        for (triangleBitmap originalImage : this.listaTriangulos) {
            originalImage.setOriginalImage(bitmap);
        }
    }
}
