package photo.photomotion.motion.photomotionlivewallpaper.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import photo.photomotion.motion.photomotionlivewallpaper.ApplicationClass;
import photo.photomotion.motion.photomotionlivewallpaper.callback.OnProgressReceiver;
import photo.photomotion.motion.photomotionlivewallpaper.callback.onVideoSaveListener;
import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.ToolsController;
import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.Utils;
import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.VideoSaver;
import photo.photomotion.motion.photomotionlivewallpaper.photoAlbum.VideoPreviewActivity;
import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Project;
import photo.photomotion.motion.photomotionlivewallpaper.utils.DatabaseHandler;

import java.io.File;
import java.text.NumberFormat;

public class SavingActivity extends Activity {
    public static String INTENT_PROJETO = "PROJETO";
    public static int MAX_RESOLUTION_SAVE = 1080;
    public static int MIN_RESOLUTION_SAVE = 600;
    static int total = MIN_RESOLUTION_SAVE + MAX_RESOLUTION_SAVE;
    public static int MAX_RESOLUTION_SAVE_FREE = (total / 2);
    public DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    public NumberFormat f147nf;
    public Project projetoToSave;
    public float resolucaoOriginal;
    public SeekBar seekResolucao;
    public TextView txResolucao;
    public TextView txTempoSave;
    public VideoSaver videoSaver;
    public int miHeight;
    public int miWidth;
    VideoSaver videoSaver2;
    ApplicationClass application;
    private Button btSave, btClose;
    private SeekBar seekTempo;
    private LinearLayout rel_process;
    private ConstraintLayout rel_save;


    public void onCreate(Bundle bundle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
        setContentView(R.layout.activity_save);

        rel_process = findViewById(R.id.rel_process);
        rel_save = findViewById(R.id.rel_save);
        rel_process.setVisibility(View.GONE);
        rel_save.setVisibility(View.VISIBLE);

        this.setFinishOnTouchOutside(false);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        projetoToSave = getIntent().getParcelableExtra(INTENT_PROJETO);
        projetoToSave = databaseHandler.getProjeto(projetoToSave.getId());
        projetoToSave.reloadBitmapUri(this, databaseHandler);
        btClose = findViewById(R.id.btClose);
        btClose.setOnClickListener(new SaveCloseClick());
        seekTempo = findViewById(R.id.seekTempoSave);
        seekTempo.setMax(8000);
        txTempoSave = findViewById(R.id.txTempoSave);
        seekTempo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                projetoToSave.setTempoSave(10000 - Math.round((((float) i) / ((float) seekBar.getMax())) * 8000.0f));
                TextView access$200 = txTempoSave;
                StringBuilder sb = new StringBuilder();
                sb.append(f147nf.format((double) (((float) projetoToSave.getTempoSave()) / 1000.0f)));
                access$200.setText(sb.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                projetoToSave.updateProjeto(databaseHandler);
            }
        });
        f147nf = NumberFormat.getInstance();
        f147nf.setMaximumFractionDigits(1);
        int tempoSave = projetoToSave.getTempoSave();
        if (tempoSave < 6000) {
            tempoSave = 6000;
        }
        seekTempo.setProgress(1);
        seekTempo.setProgress(2);
        seekTempo.setProgress(10000 - tempoSave);
        seekResolucao = (SeekBar) findViewById(R.id.seekResolucaoSave);
        seekResolucao.setMax(MAX_RESOLUTION_SAVE - MIN_RESOLUTION_SAVE);
        txResolucao = findViewById(R.id.txResolucaoSave);
        seekResolucao.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String str;
                int round = Math.round((((float) i) / ((float) seekBar.getMax())) * ((float) (SavingActivity.MAX_RESOLUTION_SAVE - SavingActivity.MIN_RESOLUTION_SAVE))) + SavingActivity.MIN_RESOLUTION_SAVE;
                if (round % 2 != 0) {
                    round++;
                }
                if (projetoToSave.getWidth() > projetoToSave.getHeight()) {
                    miWidth = round;
                    SavingActivity saveActivity = SavingActivity.this;
                    float f = (float) round;
                    saveActivity.miHeight = Math.round((((float) saveActivity.projetoToSave.getHeight()) / ((float) projetoToSave.getWidth())) * f);
                    StringBuilder sb = new StringBuilder();
                    sb.append(round);
                    sb.append("x");
                    sb.append(Math.round((((float) projetoToSave.getHeight()) / ((float) projetoToSave.getWidth())) * f));
                    str = sb.toString();
                } else {
                    SavingActivity saveActivity2 = SavingActivity.this;
                    float f2 = (float) round;
                    saveActivity2.miWidth = Math.round((((float) saveActivity2.projetoToSave.getWidth()) / ((float) projetoToSave.getHeight())) * f2);
                    miHeight = round;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(Math.round((((float) projetoToSave.getWidth()) / ((float) projetoToSave.getHeight())) * f2));
                    sb2.append(" x ");
                    sb2.append(round);
                    str = sb2.toString();
                }
                txResolucao.setText(str);
                pintarTextoIdeal(round);
                projetoToSave.setResolucaoSave(round);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                projetoToSave.updateProjeto(databaseHandler);
            }
        });
        resolucaoOriginal = (float) Math.min(Math.max(projetoToSave.getHeight(), projetoToSave.getWidth()), MAX_RESOLUTION_SAVE);
        float f = resolucaoOriginal;
        if (f % 2.0f != 0.0f) {
            f += 1.0f;
        }
        resolucaoOriginal = f;
        int resolucaoSave = projetoToSave.getResolucaoSave();
        if (resolucaoSave > MAX_RESOLUTION_SAVE_FREE) {
            seekResolucao.setProgress(1);
            seekResolucao.setProgress(2);
            seekResolucao.setProgress(resolucaoSave - MIN_RESOLUTION_SAVE);
            (findViewById(R.id.txTamanhoIdeal)).setOnClickListener(new tamanhoIdealOnClickListener());
            btSave = findViewById(R.id.btSalvar);
            btSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    rel_process.setVisibility(View.VISIBLE);
                    rel_save.setVisibility(View.GONE);
                    salvarVideo(false);
                }
            });
            setFinishOnTouchOutside(false);
            videoSaver2 = (VideoSaver) getLastNonConfigurationInstance();
        } else {
            seekResolucao.setProgress(1);
            seekResolucao.setProgress(2);
            seekResolucao.setProgress(resolucaoSave - MIN_RESOLUTION_SAVE);
            (findViewById(R.id.txTamanhoIdeal)).setOnClickListener(new tamanhoIdealOnClickListener());
            btSave = findViewById(R.id.btSalvar);

            btSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    rel_process.setVisibility(View.VISIBLE);
                    rel_save.setVisibility(View.GONE);
                    salvarVideo(false);
                }
            });
            setFinishOnTouchOutside(false);
            videoSaver2 = (VideoSaver) getLastNonConfigurationInstance();
        }
        if (videoSaver2 != null) {
            videoSaver = videoSaver2;
            videoSaver.setContext(this);
            if (videoSaver.isSalvando()) {
                setFinishOnTouchOutside(false);
            }
        }


        application = new ApplicationClass();
        application.setOnProgressReceiver(new OnProgressReceiver() {
            @Override
            public void onImageProgressFrameUpdate(final float progress) {
                int p = (int) ((25.0f * progress) / 100.0f);
                Log.e("TAG", "Image Progress = " + ((int) progress) + " || P = " + p);
                int ddd = (int) progress;
            }

        });
    }

    public void pintarTextoIdeal(int i) {
        TextView textView = findViewById(R.id.txTamanhoIdeal);
        if (i == ((int) resolucaoOriginal)) {
            textView.setTextColor(Utils.getColor(this, R.color.text_theme));
        } else {
            textView.setTextColor(Utils.getColor(this, R.color.secondary));
        }
    }

    @SuppressLint({"WrongConstant"})
    public void salvarVideo(boolean z) {
        setFinishOnTouchOutside(false);
        setRequestedOrientation(5);
//        getWindow().setFlags(16, 16);
        videoSaver = new VideoSaver(this, this.projetoToSave, this.miWidth, this.miHeight);
        videoSaver.setTempoAnimacao(projetoToSave.getTempoSave());
        videoSaver.setResolucao(projetoToSave.getResolucaoSave());
        videoSaver.setComLogo(z);
        getResources().getString(R.string.app_name);
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.getPublicAlbumStorageDir(getResources().getString(R.string.app_name)).getPath());
        sb.append("/");
        sb.append(projetoToSave.getTitulo());
        sb.append(".mp4");
        File file = new File(sb.toString());
        videoSaver.setSaveListener(new onVideoSaveListener() {
            @Override
            public void onError(String str) {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"WrongConstant"})
                    public void run() {
                        Toast.makeText(SavingActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                });
                setFinishOnTouchOutside(false);
                setRequestedOrientation(4);
                finish();
            }

            @Override
            public void onSaved(String filePath) {
                videoSaver = null;
//                getWindow().clearFlags(16);
                setFinishOnTouchOutside(false);
                setRequestedOrientation(4);
                ToolsController object = ToolsController.getObject();
                object.deleteSelection();
                object.setTooltype(0);
                ToolsController.init(MotionEditActivity.object);


                Intent intent2 = new Intent(SavingActivity.this, VideoPreviewActivity.class);
                intent2.putExtra("video_path", filePath);


                startActivity(intent2);
                finish();
                if (MotionEditActivity.object != null) {
                    MotionEditActivity.object.finish();
                }
                if (AlbumListActivity.activity != null) {
                    AlbumListActivity.activity.finish();
                }
            }

            @Override
            public void onSaving(int i) {

            }

            @Override
            public void onStartSave(int i) {

            }
        });
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.nome_logo, null);


        videoSaver.execute(file.getPath(), decodeResource);
    }

    class tamanhoIdealOnClickListener implements OnClickListener {
        public void onClick(View view) {
            seekResolucao.setProgress(((int) resolucaoOriginal) - SavingActivity.MIN_RESOLUTION_SAVE);
        }
    }


    class SaveCloseClick implements OnClickListener {
        public void onClick(View view) {
            finish();
        }
    }
}
