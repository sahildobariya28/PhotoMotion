package photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import photo.photomotion.motion.photomotionlivewallpaper.customView.Delaunay;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Project;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.triangleBitmap;

public class AnimationController {
    public static final int ANIM_ALPHA_POINT = 255;
    public static final int ANIM_FPS = 30;
    public static AnimationController controller;
    public CountDownTimer animCount;
    public Bitmap presentationImage;
    public Canvas canvas;
    public Delaunay delaunayPresentation;
    public FrameController frameController;
    public AnimateListener listenerAnim = null;
    public Paint paintMask = new Paint(1);
    public Project currentProject;
    public int timeAnimation = 10000;

    public interface AnimateListener {
        void onAnimate(Bitmap bitmap);
    }

    public interface PointIterator {
        void onIterate(Point point);
    }

    public interface PointManipulator {
        void onFinish(Bitmap bitmap);

        void onManipulate(Point point);
    }

    public AnimationController(Project project) {
        setNewProject(project);
        frameController = FrameController.getInstance();
        paintMask.setAntiAlias(true);
        paintMask.setFilterBitmap(true);
        paintMask.setStyle(Style.FILL);
        paintMask.setColor(-16777216);
        paintMask.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    }

    public static int getImageHeight() {
        return controller.presentationImage.getHeight();
    }

    public static int getImageWidth() {
        return controller.presentationImage.getWidth();
    }

    public static AnimationController getInstance() {
        return controller;
    }

    public static AnimationController init(Project project) {
        AnimationController animationController = controller;
        if (animationController == null) {
            controller = new AnimationController(project);
        } else {
            animationController.setNewProject(project);
        }
        return controller;
    }

    private void setNewProject(Project project) {
        currentProject = project;
        float width = (float) project.getImagem().getWidth();
        float height = (float) project.getImagem().getHeight();
        float f = width > height ? 800.0f / width : 800.0f / height;
        presentationImage = Bitmap.createScaledBitmap(project.getImagem(), Math.round(width * f), Math.round(height * f), true);
        Delaunay delaunay = delaunayPresentation;
        if (delaunay != null) {
            delaunay.clear();
        }
        delaunayPresentation = new Delaunay(presentationImage);
        for (Point copy : project.getListaPontos()) {
            delaunayPresentation.addPonto(copy.getCopia(1.0f / project.getDisplayProportion()));
        }
    }

    public void addPoint(Point point) {
        delaunayPresentation.addPonto(point);
    }

    public void deletePoint(Point point) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(point);
        delaunayPresentation.deletePontos(copyOnWriteArrayList);
    }

    public void deletePoints(List<Point> list) {
        delaunayPresentation.deletePontos(list);
    }

    public void deleteSelectedPoints() {
        deletePoints(getListSelectedPoints());
    }

    public boolean existsPoint(Point point) {
        for (Point equals : delaunayPresentation.getListaPontos()) {
            if (equals.equals(point)) {
                return true;
            }
        }
        return false;
    }

    public Bitmap getImageRepresentation(boolean z, float f) {
        Bitmap copy = presentationImage.copy(Config.ARGB_8888, true);
        delaunayPresentation.reiniciarPontos();
        canvas = new Canvas(copy);
        if (z) {
            for (triangleBitmap drawPath : delaunayPresentation.getListaTriangulos()) {
                drawPath.desenhaTrajeto(canvas, f);
            }
        }
        for (Point point : delaunayPresentation.getListaPontos()) {
            if (!point.isEstatico()) {
                point.desenharSeta(canvas, 255, f);
            }
            point.desenharPonto(canvas, 255, f);
        }
        return copy;
    }

    public List<Point> getListSelectedPoints() {
        LinkedList linkedList = new LinkedList();
        for (Point point : delaunayPresentation.getListaPontos()) {
            if (point.isSelecionado()) {
                linkedList.add(point);
            }
        }
        return linkedList;
    }

    public int getTimeAnimation() {
        return timeAnimation;
    }

    public void iteratePoints(PointIterator PointIterator) {
        for (Point onIterate : delaunayPresentation.getListaPontos()) {
            PointIterator.onIterate(onIterate);
        }
    }

    public Bitmap applyColorAndCropBitmap(int i, Bitmap bitmap) {
        Rect rect = currentProject.getRect();
        if (rect == null) {
            return bitmap;
        }
        Paint paint = new Paint(2);
        paint.setColor(i);
        Bitmap copy = bitmap.copy(Config.ARGB_8888, true);
        Canvas canvas2 = new Canvas(copy);
        canvas2.drawPaint(paint);
        float proportionPresentation = 1.0f / currentProject.getDisplayProportion();
        canvas2.drawBitmap(Bitmap.createBitmap(bitmap, (int) (((float) rect.left) * proportionPresentation), (int) (((float) rect.top) * proportionPresentation), (int) (((float) rect.width()) * proportionPresentation), (int) (((float) rect.height()) * proportionPresentation)), ((float) rect.left) * proportionPresentation, ((float) rect.top) * proportionPresentation, new Paint(2));
        return copy;
    }

    public void setOnAnimateListener(AnimateListener animateListener) {
        listenerAnim = animateListener;
    }

    public void setTimeAnimation(int i) {
        timeAnimation = i;
    }

    public void startAnimation() {
        CountDownTimer countDownTimer = animCount;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        final Config config = Config.ARGB_8888;
        delaunayPresentation.setImagemDelaunay(Utils.getImagemSemMascara(presentationImage, ToolsController.getMask(), config));
        Bitmap staticTriangles = delaunayPresentation.getTriangulosEstaticos(Config.ARGB_8888);
        Bitmap imageMask = Utils.getMascaraDaImagem(presentationImage, ToolsController.getMask(), Config.ARGB_8888);
        final Bitmap createBitmap = Bitmap.createBitmap(staticTriangles);
        new Canvas(createBitmap).drawBitmap(imageMask, 0.0f, 0.0f, null);
        final Bitmap createBitmap2 = Bitmap.createBitmap(presentationImage.getWidth(), presentationImage.getHeight(), config);
        final Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawBitmap(presentationImage, 0.0f, 0.0f, null);
        final Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);
        CountDownTimer r1 = new CountDownTimer(timeAnimation, 33) {
            public void onFinish() {
                startAnimation();
            }

            public void onTick(long j) {
                System.currentTimeMillis();
                AnimationController animationController = AnimationController.this;
                float f = (float) (((long) animationController.timeAnimation) - j);
                Bitmap frameSMovement = animationController.frameController.getFrameMotion(delaunayPresentation, timeAnimation, 30, f, config);
                AnimationController animationController2 = AnimationController.this;
                int i = animationController2.timeAnimation;
                float f2 = ((((float) i) / 2.0f) + f) % ((float) i);
                Bitmap frameSMovement2 = animationController2.frameController.getFrameMotion(delaunayPresentation, timeAnimation, 30, f2, config);
                int alpha = frameController.getAlpha(timeAnimation, f);
                paint.setAlpha(alpha);
                canvas2.drawBitmap(frameSMovement, 0.0f, 0.0f, null);
                int alpha2 = frameController.getAlpha(timeAnimation, f2);
                paint.setAlpha(alpha2);
                canvas2.drawBitmap(frameSMovement2, 0.0f, 0.0f, paint);
                StringBuilder sb = new StringBuilder();
                sb.append("ALPHA1: ");
                sb.append(alpha);
                sb.append(" ALPHA2:");
                sb.append(alpha2);
                Log.i("ANIMATION", sb.toString());
                canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, null);
                if (listenerAnim != null) {
                    listenerAnim.onAnimate(createBitmap2);
                }
            }
        };
        animCount = r1.start();
    }

    public void stopAnimation(Context context) {
        CountDownTimer countDownTimer = animCount;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        delaunayPresentation.clear();
    }

    public boolean hasSelectedPoint() {
        for (Point isSelected : delaunayPresentation.getListaPontos()) {
            if (isSelected.isSelecionado()) {
                return true;
            }
        }
        return false;
    }
}
