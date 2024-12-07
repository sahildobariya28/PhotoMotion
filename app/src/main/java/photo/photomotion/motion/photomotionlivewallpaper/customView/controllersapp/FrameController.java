package photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import photo.photomotion.motion.photomotionlivewallpaper.customView.Delaunay;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.triangleBitmap;

public class FrameController {
    public static FrameController controller;
    public static Bitmap manipulatedImage;
    public int ANIM_ALPHA_MAX = 255;
    public int ANIM_ALPHA_PERCENT = 50;
    public Canvas canvas;

    public static FrameController getInstance() {
        if (controller == null) {
            controller = new FrameController();
        }
        return controller;
    }

    public int getAlpha(int alphaValue, float progress) {
        float alphaThreshold = (ANIM_ALPHA_PERCENT * alphaValue) / 100f;
        float alphaFactor = ANIM_ALPHA_MAX / alphaThreshold;
        float alphaDelta = alphaValue - alphaThreshold;

        if (progress >= alphaDelta) {
            return Math.round((alphaDelta - progress) * alphaFactor) + ANIM_ALPHA_MAX;
        }

        return progress < alphaThreshold ? Math.round(alphaFactor * progress) : ANIM_ALPHA_MAX;
    }

    public Bitmap getFrameMotion(Delaunay delaunay, int width, int height, float progress, Config config) {
        int pointCount = Math.round(((float) (width * height)) / 1000.0f);
        int frameStep = (int) Math.floor((progress / 1000.0f) * ((float) height));

        for (Point point : delaunay.getListaPontos()) {
            if (!point.isEstatico()) {
                float xInit = point.getXInit() - (point.getXFim() - point.getXInit());
                float yInit = point.getYInit() - (point.getYFim() - point.getYInit());
                point.setPosicaoAtualAnim(
                        interpolate(point.getXFim(), xInit, frameStep, pointCount),
                        interpolate(point.getYFim(), yInit, frameStep, pointCount)
                );
            }
        }

        Bitmap resultBitmap = Bitmap.createBitmap(delaunay.getImagemDelaunay().getWidth(), delaunay.getImagemDelaunay().getHeight(), config);
        Canvas canvas = new Canvas(resultBitmap);

        for (triangleBitmap triangle : delaunay.getListaTriangulos()) {
            if (!triangle.isEstatico()) {
                triangle.desenhaDistorcao(canvas, config);
            }
        }

        return resultBitmap;
    }

    private float interpolate(float end, float start, float step, float totalSteps) {
        return ((end - start) / totalSteps) * step + start;
    }
}
