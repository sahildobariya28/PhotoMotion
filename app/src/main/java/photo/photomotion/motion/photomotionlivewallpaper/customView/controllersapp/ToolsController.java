package photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.activity.MotionEditActivity;
import photo.photomotion.motion.photomotionlivewallpaper.adapter.EffectAdapter;
import photo.photomotion.motion.photomotionlivewallpaper.adapter.StickerAdapter;
import photo.photomotion.motion.photomotionlivewallpaper.callback.OnClickListener;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.triangleBitmap;
import photo.photomotion.motion.photomotionlivewallpaper.model.EffectData;
import photo.photomotion.motion.photomotionlivewallpaper.model.StickerData;

public class ToolsController implements OnClickListener {
    public static final int INIT_TAM_MOV_SEQUENCIA = Math.round(64.0f);
    public static final int MAX_TAMANHO_MOV_SEQUENCIA = Math.round(240.00002f);
    public static final int MIN_TAMANHO_MOV_SEQUENCIA = Math.round(24.0f);
    public static final int MIN_TEMPO_PREVIEW = 2000;
    public static float STROKE_INIT = 4.0f;
    public static ToolsController controller;
    public Activity activity;
    public int alphaMask = 150;
    public ImageView btApagarMask;
    public FloatingActionButton btDelete;
    public ImageView btEffect;
    public ImageView btEstabilizacao;
    public ImageView btMask;
    public ImageView btMovSequencia;
    public ImageView btMovimento;
    public ImageView btSelect;
    public ImageView btSticker;
    public ImageView btZoom;
    public ImageView detalhesTopo;
    public boolean enabled = true;
    public int ferramentaAtual = -1;
    public int ferramentaAtualT = -1;
    public boolean isErasingMask = false;
    public boolean isMascarando = false;
    public boolean isSelecionando = false;
    public MaskController maskController;
    public EffectAdapter moEffectAdapter;
    public EffectData moEffectData = null;
    public List<EffectData> moEffectlist = new ArrayList();
    public RecyclerView moRcvEffects;
    public LinearLayout moRlEffects;
    public StickerAdapter moStickerAdapter;
    public StickerData moStickerData = null;
    public List<StickerData> moStickerList = new ArrayList();
    public Paint paintMascarando;
    public Paint paintRepresentacaoMascara;
    public Paint paintSelect;
    public boolean playingPreview = false;
    public int raioMask = 20;
    public SeekBar seekBrushSize;
    public SeekBar seekVelocidadePreview;
    public Point selecaoFinal;
    public Point selecaoInicial;
    public boolean showDetalhes = false;
    public int stepSize = 2000;
    public LinearLayout subToolSize;
    public LinearLayout subToolSpeed;
    public int tamMovSequencia = INIT_TAM_MOV_SEQUENCIA;
    public int tempoPreview = 10000;
    public int tooltype = 0;
    public ToolsListener toolsListener;
    public float xAtual;
    public float yAtual;
    public int position;

    public static int getAlphaMask() {
        ToolsController toolsController = controller;
        if (toolsController == null) {
            return 150;
        }
        return toolsController.alphaMask;
    }

    public void setAlphaMask(int i) {
        alphaMask = i;
    }

    public static int getColorMask() {
        ToolsController toolsController = controller;
        if (toolsController == null) {
            return -65536;
        }
        return toolsController.maskController.getColor();
    }

    public static Bitmap getMask() {
        ToolsController toolsController = controller;
        if (toolsController != null) {
            return toolsController.maskController.mask;
        }
        return null;
    }

    public static ToolsController getObject() {
        return controller;
    }

    public static int getRaioMask() {
        return controller.raioMask;
    }

    public static int getTempoPreview() {
        ToolsController toolsController = controller;
        if (toolsController != null) {
            return toolsController.tempoPreview;
        }
        return 10000;
    }

    public void setTempoPreview(int i) {
        tempoPreview = i;
        seekVelocidadePreview.setProgress(1);
        seekVelocidadePreview.setProgress(2);
        seekVelocidadePreview.setProgress(10000 - i);
    }

    public static ToolsController init(Activity activity2, int position) {
        if (controller == null) {
            controller = new ToolsController();
        }
        ToolsController toolsController = controller;
        toolsController.activity = activity2;
        toolsController.restartDefinitions();
        toolsController.position = position;
        controller.resetTextView(R.id.txMovimento);
        controller.resetTextView(R.id.txSequencia);
        controller.resetTextView(R.id.txEstabilizar);
        controller.resetTextView(R.id.txMask);
        controller.resetTextView(R.id.txSelect);
        controller.resetTextView(R.id.txApagar);
        controller.resetTextView(R.id.txEffect);
        controller.resetTextView(R.id.txSticker);
        if (controller.tooltype != 0) {
            Log.e("controller", "init: gyu");
            ToolsController toolsController2 = controller;
            toolsController2.setFerramentaAtual(toolsController2.ferramentaAtual, toolsController2.ferramentaAtualT);
        }
        return controller;
    }

    public static ToolsController init(Activity activity2) {
        if (controller == null) {
            controller = new ToolsController();
        }
        ToolsController toolsController = controller;
        toolsController.activity = activity2;
        toolsController.restartDefinitions();
        controller.resetTextView(R.id.txMovimento);
        controller.resetTextView(R.id.txSequencia);
        controller.resetTextView(R.id.txEstabilizar);
        controller.resetTextView(R.id.txMask);
        controller.resetTextView(R.id.txSelect);
        controller.resetTextView(R.id.txApagar);
        controller.resetTextView(R.id.txEffect);
        controller.resetTextView(R.id.txSticker);
        if (controller.tooltype != 0) {
            Log.e("controller", "init: gyu");
            ToolsController toolsController2 = controller;
            toolsController2.setFerramentaAtual(toolsController2.ferramentaAtual, toolsController2.ferramentaAtualT);
        }
        return controller;
    }

    public Bitmap getRaioRepresentacao() {
        Bitmap createBitmap = Bitmap.createBitmap(Math.round((STROKE_INIT * 2.0f) + ((float) (getRaioMask() * 2))), Math.round((STROKE_INIT * 2.0f) + ((float) (getRaioMask() * 2))), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        paintMascarando.setStrokeWidth(STROKE_INIT);
        if (tooltype == 3) {
            paintRepresentacaoMascara.setColor(-65536);
        } else {
            paintRepresentacaoMascara.setColor(-16711936);
        }
        canvas.drawCircle((float) Math.round(((float) createBitmap.getWidth()) / 2.0f), (float) Math.round(((float) createBitmap.getHeight()) / 2.0f), (float) getRaioMask(), paintRepresentacaoMascara);
        canvas.drawCircle((float) Math.round(((float) createBitmap.getWidth()) / 2.0f), (float) Math.round(((float) createBitmap.getHeight()) / 2.0f), (float) getRaioMask(), paintMascarando);
        return createBitmap;
    }

    public Bitmap getSetaRepresentacao() {
        Bitmap createBitmap = Bitmap.createBitmap(tamMovSequencia + 10, 10, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Point ponto = new Point(5.0f, 5.0f, (float) (tamMovSequencia + 5), 5.0f);
        ponto.desenharPonto(canvas, 255, 1.0f);
        ponto.desenharSeta(canvas, 255, 1.0f);
        return createBitmap;
    }

    public void addMask(Bitmap bitmap) {
        maskController.addMask(bitmap);
    }

    @SuppressLint({"RestrictedApi", "WrongConstant"})
    public void deleteSelection() {
        AnimationController.getInstance().iteratePoints(new C04711());
        btDelete.setVisibility(View.INVISIBLE);
    }

    public void deleteMask(Bitmap bitmap) {
        maskController.deleteMask(bitmap);
    }

    public int getCurrentStickerPosition() {
        StickerAdapter stickerAdapter = moStickerAdapter;
        if (stickerAdapter != null) {
            return stickerAdapter.getSelectedEffect().getStickerId();
        }
        return -1;
    }

    public EffectData getSelecetdEffect() {
        return moEffectAdapter.getSelectedEffect();
    }

    public int getTamMovSequencia() {
        return tamMovSequencia;
    }

    public int getTooltype() {
        return tooltype;
    }

    public void setTooltype(int i) {
        tooltype = i;
    }

    public boolean isErasingMask() {
        return isErasingMask;
    }

    public boolean isPlayingPreview() {
        return playingPreview;
    }


    @SuppressLint({"WrongConstant", "RestrictedApi"})
    public void onClick(View view) {

    }

    public void pintar(Bitmap bitmap, float f) {
        maskController.pintarCor(bitmap);
        Canvas canvas = new Canvas(bitmap);
        float f2 = STROKE_INIT;
        float max = Math.max(f2 / f, f2 / 2.0f);
        if (!(selecaoInicial == null || selecaoFinal == null || !isSelecionando)) {
            Paint paint = paintSelect;
            float f3 = triangleBitmap.STROKE_DOT_SPACE_INIT;
            paint.setPathEffect(new DashPathEffect(new float[]{f3 / f, (f3 * 2.0f) / f}, 0.0f));
            paintSelect.setStrokeWidth(max);
            canvas.drawRect(selecaoInicial.getXInit(), selecaoInicial.getYInit(), selecaoFinal.getXInit(), selecaoFinal.getYInit(), paintSelect);
        }
        if (isMascarando) {
            paintMascarando.setStrokeWidth(max);
            canvas.drawCircle(xAtual, yAtual, ((float) getRaioMask()) / f, paintMascarando);
        }
    }

    @SuppressLint({"WrongConstant"})
    public void playPreview() {
        playingPreview = true;
        subToolSpeed.setVisibility(View.VISIBLE);
        if (toolsListener != null) {
            toolsListener.onPlayPreview();
        }
    }

    public void resetStickerAdapter() {
        if (moStickerAdapter != null) {
            MotionEditActivity.object.binding.customView.setVisibility(View.GONE);
            moStickerAdapter.resetAdapter();
        }
    }

    public void resetTextView(int i) {
        ((TextView) activity.findViewById(i)).setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
    }

    @SuppressLint({"WrongConstant", "RestrictedApi"})
    public void restartDefinitions() {
        maskController = new MaskController();
        detalhesTopo = activity.findViewById(R.id.detailsTop);
        detalhesTopo.setVisibility(View.INVISIBLE);
        paintSelect = new Paint(1);
        paintSelect.setFilterBitmap(true);
        paintSelect.setStyle(Style.STROKE);
        paintSelect.setColor(-1);
        paintSelect.setStrokeWidth(STROKE_INIT);
        paintMascarando = new Paint(1);
        paintMascarando.setFilterBitmap(true);
        paintMascarando.setStyle(Style.STROKE);
        paintMascarando.setColor(-1);
        paintMascarando.setStrokeWidth(STROKE_INIT);
        paintRepresentacaoMascara = new Paint(1);
        paintRepresentacaoMascara.setFilterBitmap(true);
        paintRepresentacaoMascara.setStyle(Style.FILL);
        paintRepresentacaoMascara.setAlpha(maskController.getAlpha());
        paintRepresentacaoMascara.setColor(maskController.getColor());
        btMovimento = activity.findViewById(R.id.btMovimento);
        btMovimento.setEnabled(false);
        btMovimento.setImageResource(R.drawable.ic_menu_motion);
        btMovimento.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnMotion).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btMovimento.getId(), R.id.txMovimento);
            moRlEffects.setVisibility(View.GONE);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            tooltype = 1;
            toolsListener.onPressSetaMovimento();
        });
        btMovSequencia = activity.findViewById(R.id.btMovSequence);
        btMovSequencia.setEnabled(false);
        btMovSequencia.setImageResource(R.drawable.ic_menu_sequence);
        btMovSequencia.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnSequence).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btMovSequencia.getId(), R.id.txSequencia);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            tooltype = 5;
            moRlEffects.setVisibility(View.GONE);
            subToolSize.setVisibility(View.VISIBLE);
            seekBrushSize.setMax(MAX_TAMANHO_MOV_SEQUENCIA - MIN_TAMANHO_MOV_SEQUENCIA);
            seekBrushSize.setProgress(tamMovSequencia - MIN_TAMANHO_MOV_SEQUENCIA);
            toolsListener.onPressSetaMovSequencia();
            toolsListener.onChangeTamanhoSetaMovSequencia(tamMovSequencia, getSetaRepresentacao());
        });
        btSelect = activity.findViewById(R.id.btSelect);
        btSelect.setEnabled(false);
        btSelect.setImageResource(R.drawable.ic_menu_select);
        btSelect.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnSelect).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btSelect.getId(), R.id.txSelect);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            moRlEffects.setVisibility(View.GONE);
            tooltype = 4;
            toolsListener.onPressSelecao();
        });
        btZoom = activity.findViewById(R.id.btZoom);
        btZoom.setEnabled(false);
        btZoom.setImageResource(R.drawable.tool_zoom);
        btZoom.setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            setFerramentaAtual(view.getId(), R.id.txZoom);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            moRlEffects.setVisibility(View.GONE);
            tooltype = 7;
            toolsListener.onPressZoom();
        });
        btEstabilizacao = activity.findViewById(R.id.btEstabilizar);
        btEstabilizacao.setEnabled(false);
        btEstabilizacao.setImageResource(R.drawable.ic_menu_stabilize);
        btEstabilizacao.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnStabilize).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btEstabilizacao.getId(), R.id.txEstabilizar);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            moRlEffects.setVisibility(View.GONE);
            tooltype = 2;
            toolsListener.onPressEsbilizar();
        });
        btMask = activity.findViewById(R.id.btMask);
        btMask.setEnabled(false);
        btMask.setImageResource(R.drawable.ic_menu_mask);
        btMask.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnMask).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btMask.getId(), R.id.txMask);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            tooltype = 3;
            moRlEffects.setVisibility(View.GONE);
            subToolSize.setVisibility(View.VISIBLE);
            seekBrushSize.setMax(97);
            seekBrushSize.setProgress(raioMask - 3);
            toolsListener.onPressMascara();
            toolsListener.onChangeRaioMascara(getRaioMask(), getRaioRepresentacao());
        });
        btApagarMask = activity.findViewById(R.id.btApagarMascara);
        btApagarMask.setEnabled(false);
        btApagarMask.setImageResource(R.drawable.ic_menu_erase);
        btApagarMask.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnErase).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btApagarMask.getId(), R.id.txApagar);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            isErasingMask = true;
            tooltype = 6;
            moRlEffects.setVisibility(View.GONE);
            subToolSize.setVisibility(View.VISIBLE);
            seekBrushSize.setMax(97);
            seekBrushSize.setProgress(raioMask - 3);
            toolsListener.onPressApagarMascara();
            toolsListener.onChangeRaioMascara(getRaioMask(), getRaioRepresentacao());
        });
        btEffect = activity.findViewById(R.id.btEffect);
        btEffect.setEnabled(false);
        btEffect.setImageResource(R.drawable.ic_menu_effect);
        btEffect.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnEffect).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btEffect.getId(), R.id.txEffect);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            isErasingMask = false;
            tooltype = 10;
            subToolSize.setVisibility(View.GONE);
            moRlEffects.setVisibility(View.VISIBLE);
            moRcvEffects.setAdapter(moEffectAdapter);
            EffectAdapter effectAdapter = moEffectAdapter;
            if (!(effectAdapter == null || effectAdapter.getSelectedEffect().getEffectId() == -1)) {
                MotionEditActivity.object.binding.gifView.setVisibility(View.VISIBLE);
                MotionEditActivity.object.binding.gifView.playAnimation();
            }
        });
        btSticker = activity.findViewById(R.id.btSticker);
        btSticker.setEnabled(false);
        btSticker.setImageResource(R.drawable.ic_menu_sticker);
        btSticker.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        activity.findViewById(R.id.btnSticker).setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            deleteSelection();
            setFerramentaAtual(btSticker.getId(), R.id.txSticker);
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(false);
            isErasingMask = false;
            tooltype = 11;
            subToolSize.setVisibility(View.GONE);
            moRlEffects.setVisibility(View.VISIBLE);
            moRcvEffects.setAdapter(moStickerAdapter);
        });
        btDelete = activity.findViewById(R.id.btDelete);
        btDelete.setVisibility(View.INVISIBLE);
        btDelete.setImageResource(R.drawable.ic_delete2);
        btDelete.setOnClickListener(view -> {
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            isErasingMask = false;
            stopPreview();
            MotionEditActivity.object.binding.customView.hideFrame(true);
            toolsListener.onPressDelete();
        });
        seekBrushSize = activity.findViewById(R.id.seekBrushSize);
        seekBrushSize.setOnSeekBarChangeListener(new C02614());
        seekVelocidadePreview = activity.findViewById(R.id.seekTimeSpeed);
        seekVelocidadePreview.setMax(8000);
        seekVelocidadePreview.incrementProgressBy(2000);
        seekVelocidadePreview.setOnSeekBarChangeListener(new C02625());
        subToolSize = activity.findViewById(R.id.subToolTamMask);
        subToolSize.setVisibility(View.INVISIBLE);
        moRlEffects = activity.findViewById(R.id.rl_effects);
        moRcvEffects = activity.findViewById(R.id.rcv_effects);
        moRlEffects.setVisibility(View.GONE);
        subToolSpeed = activity.findViewById(R.id.subToolSpeedPreview);
        subToolSpeed.setVisibility(View.INVISIBLE);
        maskController.clearMask();
        maskController.setAlpha(alphaMask);
        playingPreview = false;
    }

    @SuppressLint({"WrongConstant", "RestrictedApi"})
    public void setDeleteToolVisibility(boolean z) {
        btDelete.setVisibility(z ? 0 : 4);
    }

    public void setEffectAdapter(Bitmap bitmap) {
        moEffectlist.clear();
        moEffectlist.add(new EffectData(-1, "", "", true, R.drawable.noeffect, ""));
        moEffectlist.add(new EffectData(0, "animation0.json", "", false, R.drawable.filteranim0, ""));
        moEffectlist.add(new EffectData(1, "animation1.json", "animation1", false, R.drawable.filteranim1, ""));
        moEffectlist.add(new EffectData(2, "anim.json", "anim", false, R.drawable.filteranim12, ""));
        moEffectlist.add(new EffectData(3, "animation3.json", "animation3", false, R.drawable.filteranim3, ""));
        moEffectlist.add(new EffectData(4, "animation4.json", "animation4", false, R.drawable.filteranim4, ""));
        moEffectlist.add(new EffectData(5, "animation5.json", "animation5", false, R.drawable.filteranim5, ""));
        moEffectlist.add(new EffectData(6, "animation6.json", "", false, R.drawable.filteranim6, ""));
        moEffectlist.add(new EffectData(8, "animation8.json", "animation8", false, R.drawable.filteranim8, ""));
        moEffectlist.add(new EffectData(9, "animation9.json", "", false, R.drawable.filteranim9, ""));
        moEffectlist.add(new EffectData(10, "animation10.json", "", false, R.drawable.anim10_white_hart, ""));
        moEffectlist.add(new EffectData(12, "animation12.json", "animation12", false, R.drawable.filteranim12, ""));
        moEffectlist.add(new EffectData(13, "animation13.json", "animation13", false, R.drawable.filteranim13, ""));
        moEffectlist.add(new EffectData(14, "animation14.json", "animation14", false, R.drawable.filteranim14, ""));
        moEffectlist.add(new EffectData(16, "animation16.json", "animation16", false, R.drawable.filteranim16, ""));
        moEffectlist.add(new EffectData(17, "animation17.json", "animation17", false, R.drawable.filteranim17, ""));
        moEffectlist.add(new EffectData(18, "animation18.json", "animation18", false, R.drawable.filteranim18, ""));
        moEffectlist.add(new EffectData(19, "animation19.json", "animation19", false, R.drawable.filteranim19, ""));
        moEffectlist.add(new EffectData(26, "animation26.json", "", false, R.drawable.filteranim26, ""));
        moEffectlist.add(new EffectData(27, "animation27.json", "animation27", false, R.drawable.filteranim27, ""));
        moEffectlist.add(new EffectData(28, "animation28.json", "animation28", false, R.drawable.filteranim28, ""));
        moEffectlist.add(new EffectData(29, "animation29.json", "animation29", false, R.drawable.filteranim29, ""));
        moEffectlist.add(new EffectData(30, "animation30.json", "animation30", false, R.drawable.filteranim30, ""));
        moEffectlist.add(new EffectData(31, "animation31.json", "animation31", false, R.drawable.filteranim31, ""));
        moStickerList.clear();
        moStickerList.add(new StickerData(-1, "noSticker", "", true, R.drawable.no_sticker));
        moStickerList.add(new StickerData(0, "sticker1.json", "sticker1", false, R.drawable.sticker1));
        moStickerList.add(new StickerData(1, "sticker2.json", "", false, R.drawable.sticker2));
        moStickerList.add(new StickerData(3, "sticker4.json", "sticker4", false, R.drawable.sticker4));
        moStickerList.add(new StickerData(4, "sticker7.json", "sticker7", false, R.drawable.sticker7));
        moStickerList.add(new StickerData(5, "sticker8.json", "sticker8", false, R.drawable.sticker8));
        moStickerList.add(new StickerData(10, "sticker13.json", "sticker13", false, R.drawable.sticker13));
        moStickerList.add(new StickerData(14, "sticker14.json", "", false, R.drawable.sticker14));
        moStickerList.add(new StickerData(15, "sticker15.json", "", false, R.drawable.sticker15));
        moStickerList.add(new StickerData(16, "sticker16.json", "", false, R.drawable.sticker16));
        moStickerList.add(new StickerData(17, "sticker17.json", "sticker17", false, R.drawable.sticker17));
        moStickerList.add(new StickerData(18, "sticker18.json", "sticker18", false, R.drawable.sticker18));
        moStickerList.add(new StickerData(19, "sticker19.json", "sticker19", false, R.drawable.sticker19));
        moStickerList.add(new StickerData(20, "sticker20.json", "sticker20", false, R.drawable.sticker20));
        moStickerList.add(new StickerData(21, "sticker21.json", "sticker21", false, R.drawable.sticker21));
        moStickerList.add(new StickerData(22, "sticker22.json", "sticker22", false, R.drawable.sticker22));
        moStickerList.add(new StickerData(23, "sticker23.json", "sticker23", false, R.drawable.sticker23));
        moStickerList.add(new StickerData(25, "sticker25.json", "sticker25", false, R.drawable.sticker25));
        moStickerList.add(new StickerData(26, "sticker26.json", "sticker26", false, R.drawable.sticker26));
        moStickerList.add(new StickerData(27, "sticker27.json", "sticker27", false, R.drawable.sticker27));
        moStickerList.add(new StickerData(28, "sticker28.json", "sticker28", false, R.drawable.sticker28));
        moRcvEffects.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        Bitmap bitmap3 = bitmap;
        moEffectAdapter = new EffectAdapter(activity, moEffectlist, bitmap3, position, this::onClick);
        moStickerAdapter = new StickerAdapter(activity, moStickerList, bitmap3, (stickerData, i) -> {
            if (stickerData.getStickerId() != -1) {
                MotionEditActivity.object.binding.customView.hideFrame(false);
                MotionEditActivity.object.binding.customView.setVisibility(View.VISIBLE);
                MotionEditActivity.object.binding.customView.setAnimation(stickerData.getStickerName(), stickerData.getStickerFolderNameFPath());
                MotionEditActivity.object.binding.customView.playAnimation();

                int height = MotionEditActivity.object.binding.imageView.getHeight();
                int width = MotionEditActivity.object.binding.imageView.getWidth();
                int intrinsicHeight = MotionEditActivity.object.binding.imageView.getDrawable().getIntrinsicHeight();
                int intrinsicWidth = MotionEditActivity.object.binding.imageView.getDrawable().getIntrinsicWidth();

                int i2 = height * intrinsicWidth;
                int i3 = width * intrinsicHeight;

                if (i2 <= i3) {
                    width = i2 / intrinsicHeight;
                } else {
                    height = i3 / intrinsicWidth;
                }

                // Create LayoutParams for ConstraintLayout
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);

                // Set constraints to center the customView in rlEffectsContainer
                layoutParams.width = width; // Set the calculated width
                layoutParams.height = height; // Set the calculated height

                // Center customView within the rlEffectsContainer
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; // Align start to parent's start
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Align end to parent's end
                layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID; // Align top to parent's top
                layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // Align bottom to parent's bottom

                // Apply layout params to the customView
                MotionEditActivity.object.binding.customView.setLayoutParams(layoutParams);

                MotionEditActivity.object.binding.customView.setScaleType(); // Ensure you call a valid method to set scale type
                MotionEditActivity.object.moStickerData = stickerData;
                moStickerData = stickerData;
                playPreview();
                return;
            }

            // Hide customView if no sticker is selected
            MotionEditActivity.object.binding.customView.setVisibility(View.GONE);
            stopPreview();
        });
    }

    @SuppressLint({"WrongConstant", "RestrictedApi"})
    public void setEnabled(boolean z) {
        if (z && !enabled) {
            playingPreview = false;
            subToolSize.setVisibility(View.INVISIBLE);
            subToolSpeed.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
        }
        enabled = z;
        btEstabilizacao.setEnabled(z);
        btMovimento.setEnabled(z);
        btEffect.setEnabled(z);
        btSticker.setEnabled(z);
        btMovSequencia.setEnabled(z);
        btMask.setEnabled(z);
        btApagarMask.setEnabled(z);
        btSelect.setEnabled(z);
        btZoom.setEnabled(z);
    }

    public void setFerramentaAtual(int i, int i2) {
        ImageView imageButton = activity.findViewById(i);
        TextView textView = activity.findViewById(i2);
        if (tooltype != 0) {
            TextView textView2 = activity.findViewById(ferramentaAtualT);
            ((ImageView) activity.findViewById(ferramentaAtual)).setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
            textView2.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.white, null));
        }
        imageButton.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.secondary, null));
        textView.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.secondary, null));
        ferramentaAtual = i;
        ferramentaAtualT = i2;
    }

    public void setMascaraInicial(Bitmap bitmap) {
        if (bitmap == null) {
            maskController.clearMask();
        } else {
            maskController.setMask(bitmap);
        }
    }

    public void setMascarando(boolean z) {
        isMascarando = z;
    }

    public void setSelecionando(boolean z) {
        isSelecionando = z;
    }

    @SuppressLint({"WrongConstant"})
    public void setSubToolTamanhoPincelVisibility(boolean z) {
        subToolSize.setVisibility(z ? 0 : 4);
    }

    @SuppressLint({"WrongConstant"})
    public void setSubToolVelocidadeVisibility(boolean z) {
        subToolSpeed.setVisibility(z ? View.VISIBLE : View.INVISIBLE);
    }

    public void setToolsListener(ToolsListener toolsListener2) {
        toolsListener = toolsListener2;
    }

    @SuppressLint({"WrongConstant"})
    public void stopPreview() {
        playingPreview = false;
        subToolSpeed.setVisibility(View.INVISIBLE);
        MotionEditActivity.object.binding.gifView.setVisibility(View.GONE);

        if (toolsListener != null) {
            toolsListener.onStopPreview();
        }
    }

    public Bitmap addMask(float f, float f2, float f3) {
        xAtual = f;
        yAtual = f2;
        return maskController.addMask(f, f2, f3);
    }

    public Bitmap deleteMask(float f, float f2, float f3) {
        xAtual = f;
        yAtual = f2;
        return maskController.deleteMask(f, f2, f3);
    }

    public void setSelecionando(Point ponto, final Point ponto2) {
        selecaoInicial = ponto;
        selecaoFinal = ponto2;
        final Rect rect = new Rect(Math.round(ponto2.getXInit() < ponto.getXInit() ? ponto2.getXInit() : ponto.getXInit()), Math.round(ponto2.getYInit() < ponto.getYInit() ? ponto2.getYInit() : ponto.getYInit()), Math.round(ponto2.getXInit() > ponto.getXInit() ? ponto2.getXInit() : ponto.getXInit()), Math.round(ponto2.getYInit() > ponto.getYInit() ? ponto2.getYInit() : ponto.getYInit()));
        isSelecionando = true;
        AnimationController.getInstance().iteratePoints(ponto12 -> ponto12.setSelecionado(rect.contains(Math.round(ponto12.getXInit()), Math.round(ponto12.getYInit()))));

        if (ponto.distanciaPara(ponto2) <= 24.0d) {
            AnimationController.getInstance().iteratePoints(ponto1 -> {
                if (ponto2.naCircunferencia(ponto, 24.0d)) {
                    ponto2.setSelecionado(true);
                    btDelete.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onClick(EffectData effectData, int i) {
        if (effectData.getEffectId() != -1) {
            MotionEditActivity.object.binding.gifView.setSpeed(1.0f);
            MotionEditActivity.object.binding.gifView.setAnimation(effectData.getEffectName());

            if (!effectData.getEffectGIFPath().isEmpty()) {
                MotionEditActivity.object.binding.gifView.setImageAssetsFolder(effectData.getEffectGIFPath());
            }

            MotionEditActivity.object.binding.gifView.playAnimation();

            int height = MotionEditActivity.object.binding.imageView.getHeight();
            int width = MotionEditActivity.object.binding.imageView.getWidth();
            int intrinsicHeight = MotionEditActivity.object.binding.imageView.getDrawable().getIntrinsicHeight();
            int intrinsicWidth = MotionEditActivity.object.binding.imageView.getDrawable().getIntrinsicWidth();

            int i2 = height * intrinsicWidth;
            int i3 = width * intrinsicHeight;

            if (i2 <= i3) {
                width = i2 / intrinsicHeight;
            } else {
                height = i3 / intrinsicWidth;
            }

            // Create new LayoutParams for ConstraintLayout
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);

            // Center the rlEffectsContainer
            layoutParams.width = width; // Set the calculated width
            layoutParams.height = height; // Set the calculated height

            // Set the constraints to center it in the parent
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; // Align start to parent's start
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Align end to parent's end
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID; // Align top to parent's top
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // Align bottom to parent's bottom

            // Apply layout params to the rlEffectsContainer
            MotionEditActivity.object.binding.rlEffectsContainer.setLayoutParams(layoutParams);

            // Set ScaleType and visibility
            MotionEditActivity.object.binding.gifView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            MotionEditActivity.object.binding.gifView.setVisibility(View.VISIBLE);

            MotionEditActivity.object.moEffectData = effectData;
            moEffectData = effectData;
            playPreview();
            return;
        }
        stopPreview();
    }

    public interface ToolsListener {
        void onChangeRaioMascara(int i, Bitmap bitmap);

        void onChangeTamanhoSetaMovSequencia(int i, Bitmap bitmap);

        void onPlayPreview();

        void onPressApagarMascara();

        void onPressDelete();

        void onPressEsbilizar();

        void onPressMascara();

        void onPressSelecao();

        void onPressSetaMovSequencia();

        void onPressSetaMovimento();

        void onPressZoom();

        void onStopPreview();

        void onTempoAlterado(int i);

        void onTempoChanging(int i);
    }

    public class C02614 implements OnSeekBarChangeListener {
        public C02614() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                int access$000 = tooltype;
                if (access$000 == 3) {
                    raioMask = i + 3;
                    toolsListener.onChangeRaioMascara(raioMask, getRaioRepresentacao());
                } else if (access$000 == 5) {
                    tamMovSequencia = ToolsController.MIN_TAMANHO_MOV_SEQUENCIA + i;
                    toolsListener.onChangeTamanhoSetaMovSequencia(tamMovSequencia, getSetaRepresentacao());
                } else if (access$000 == 6) {
                    raioMask = i + 3;
                    toolsListener.onChangeRaioMascara(raioMask, getRaioRepresentacao());
                }
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            toolsListener.onChangeTamanhoSetaMovSequencia(tamMovSequencia, null);
        }
    }

    public class C02625 implements OnSeekBarChangeListener {
        public C02625() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            tempoPreview = 10000 - Math.round((((float) i) / ((float) seekBar.getMax())) * 8000.0f);
            if (z) {
                seekBar.setProgress(stepSize * Math.round((float) (i / stepSize)));
                NumberFormat instance = NumberFormat.getInstance();
                instance.setMaximumFractionDigits(1);
                Bitmap createBitmap = Bitmap.createBitmap(detalhesTopo.getWidth(), detalhesTopo.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint(1);
                paint.setFilterBitmap(true);
                paint.setColor(ResourcesCompat.getColor(activity.getResources(), R.color.secondary, null));
                paint.setStyle(Style.FILL);
                float width = (float) (createBitmap.getWidth() / 2);
                float height = (float) (createBitmap.getHeight() / 2);
                canvas.drawCircle(width, height, (float) ((canvas.getHeight() / 2) - 2), paint);
                paint.setColor(-1);
                paint.setStyle(Style.STROKE);
                paint.setStrokeWidth(4.0f);
                canvas.drawCircle(width, height, (float) ((canvas.getHeight() / 2) - 2), paint);
                Paint paint2 = new Paint(1);
                paint2.setColor(-1);
                paint2.setTextAlign(Align.CENTER);
                paint2.setTextSize(50.0f);
                paint2.setFakeBoldText(true);
                StringBuilder sb = new StringBuilder();
                sb.append(instance.format((double) (((float) tempoPreview) / 1000.0f)));
                sb.append("s");
                canvas.drawText(sb.toString(), (float) Math.round(((float) canvas.getWidth()) / 2.0f), (float) Math.round((((float) canvas.getHeight()) / 2.0f) - ((paint2.ascent() + paint2.descent()) / 2.0f)), paint2);
                detalhesTopo.setImageBitmap(createBitmap);
                toolsListener.onTempoChanging(tempoPreview);
            }
        }

        @SuppressLint({"WrongConstant"})
        public void onStartTrackingTouch(SeekBar seekBar) {
            detalhesTopo.setVisibility(View.VISIBLE);
        }

        @SuppressLint({"WrongConstant"})
        public void onStopTrackingTouch(SeekBar seekBar) {
            detalhesTopo.setVisibility(View.INVISIBLE);
            toolsListener.onTempoAlterado(tempoPreview);
        }
    }

    public class C04711 implements AnimationController.PointIterator {
        public C04711() {
        }

        public void onIterate(Point point) {
            point.setSelecionado(false);
        }
    }

    public class MaskController {
        public static final String BUNDLE_MASK = "BUNDLE_MASK";
        public Canvas canvasMask;
        public Bitmap mask;
        public Paint paintMask = new Paint(1);
        public Paint paintRepresentacaoMask = new Paint(1);

        public MaskController() {
            paintRepresentacaoMask.setAntiAlias(true);
            paintRepresentacaoMask.setFilterBitmap(true);
            paintRepresentacaoMask.setStyle(Style.FILL);
            paintRepresentacaoMask.setAlpha(150);
            paintRepresentacaoMask.setColor(-65536);
            paintRepresentacaoMask.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            paintMask.setStyle(Style.FILL);
            paintMask.setFilterBitmap(true);
            paintMask.setColor(-65536);
        }

        public void addMask(Bitmap bitmap) {
            if (mask == null) {
                mask = Bitmap.createBitmap(AnimationController.getImageWidth(), AnimationController.getImageHeight(), Config.ARGB_8888);
                canvasMask = new Canvas(mask);
            }
            paintMask.setXfermode(null);
            canvasMask.drawBitmap(bitmap, 0.0f, 0.0f, paintMask);
        }

        public void clearMask() {
            Bitmap bitmap = mask;
            if (bitmap != null) {
                bitmap.recycle();
                mask = null;
            }
        }

        public void deleteMask(Bitmap bitmap) {
            if (mask != null && bitmap != null) {
                paintMask.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
                canvasMask.drawBitmap(bitmap, 0.0f, 0.0f, paintMask);
            }
        }

        public int getAlpha() {
            return paintRepresentacaoMask.getAlpha();
        }

        public void setAlpha(int i) {
            paintRepresentacaoMask.setAlpha(i);
        }

        public int getColor() {
            return paintRepresentacaoMask.getColor();
        }

        public void setColor(int i) {
            paintRepresentacaoMask.setColor(i);
        }

        public void pintarCor(Bitmap bitmap) {
            if (mask != null) {
                new Canvas(bitmap).drawBitmap(mask, 0.0f, 0.0f, paintRepresentacaoMask);
            }
        }

        public void setMask(Bitmap bitmap) {
            mask = bitmap.copy(Config.ARGB_8888, true);
            canvasMask = new Canvas(mask);
        }

        public Bitmap deleteMask(float f, float f2, float f3) {
            Bitmap bitmap = mask;
            if (bitmap == null) {
                return null;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), mask.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float f4 = 1.0f / f3;
            canvas.drawCircle(f, f2, ((float) ToolsController.getRaioMask()) * f4, paintMask);
            paintMask.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            canvas.drawBitmap(mask, 0.0f, 0.0f, paintMask);
            paintMask.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            canvasMask.drawCircle(f, f2, ((float) ToolsController.getRaioMask()) * f4, paintMask);
            return createBitmap;
        }

        public Bitmap addMask(float f, float f2, float f3) {
            if (mask == null) {
                mask = Bitmap.createBitmap(AnimationController.getImageWidth(), AnimationController.getImageHeight(), Config.ARGB_8888);
                canvasMask = new Canvas(mask);
            }
            Bitmap createBitmap = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            paintMask.setXfermode(null);
            float f4 = 1.0f / f3;
            canvas.drawCircle(f, f2, ((float) ToolsController.getRaioMask()) * f4, paintMask);
            paintMask.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            canvas.drawBitmap(mask, 0.0f, 0.0f, paintMask);
            paintMask.setXfermode(null);
            canvasMask.drawCircle(f, f2, ((float) ToolsController.getRaioMask()) * f4, paintMask);
            return createBitmap;
        }
    }
}
