package photo.photomotion.motion.photomotionlivewallpaper.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.AnimationController;
import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.HistoryController;
import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.ToolsController;
import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.Utils;
import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.callback.OnBitmapListCreated;
import photo.photomotion.motion.photomotionlivewallpaper.customView.CustomAnimationView;
import photo.photomotion.motion.photomotionlivewallpaper.customView.ShareClass;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Project;
import photo.photomotion.motion.photomotionlivewallpaper.databinding.ActivityPhotoEditBinding;
import photo.photomotion.motion.photomotionlivewallpaper.model.EffectData;
import photo.photomotion.motion.photomotionlivewallpaper.model.StickerData;
import photo.photomotion.motion.photomotionlivewallpaper.utils.BitmapHelper;
import photo.photomotion.motion.photomotionlivewallpaper.utils.DatabaseHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class MotionEditActivity extends AppCompatActivity {


    public static DisplayMetrics metrics;
    public static MotionEditActivity object;

    public ActivityPhotoEditBinding binding;
    public int position;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public AnimationController animationController;
    public DatabaseHandler db = DatabaseHandler.getInstance(this);
    public HistoryController historyController;
    public boolean imageLoaded = false;
    public Bitmap imageRepresentation;
    public Menu secondaryMenu;
    public List<Bitmap> moBitmapList = new ArrayList();
    public ProgressDialog moDialog;
    public EffectData moEffectData = null;
    public OnBitmapListCreated moOnBitmapListCreated;
    public StickerData moStickerData = null;
    public boolean mouseDragging = false;
    public Point startingPoint;
    public List<Point> pointsSequence = new CopyOnWriteArrayList();
    public Project currentProject;
    public boolean showDetails = false;
    public ToolsController toolsController;
    public ListenerPermission lastPermissionRequest;
    public float xInitMouse;
    public float yInitMouse;
    public List<EffectData> moEffectlist = new ArrayList();

    @SuppressLint({"WrongConstant"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        binding = ActivityPhotoEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        position = getIntent().getIntExtra("effect_position", 0);


        object = this;
        binding.btnNext.setOnClickListener(v -> binding.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT));
        metrics = getResources().getDisplayMetrics();
        binding.menuBar.setTitle("");
        setSupportActionBar(binding.menuBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar();
        Utils.getDrawable(this, R.drawable.nome_topo, -1);

        binding.toolBar.setVisibility(View.INVISIBLE);
        binding.imageZoom.setVisibility(View.INVISIBLE);
        binding.imageView.setLayerType(2, null);
        binding.btPlayPause.setOnClickListener(new playPauseClick());
        historyController = HistoryController.getInstance();
        toolsController = ToolsController.init(this, position);
        toolsController.setToolsListener(new toolsListener());
        binding.imageView.setScaleListener(new simpleOnScaleGestureListener());
        binding.imageView.setOnTouchListener(new onTouchListener());
        binding.customView.init1(this);
        binding.imageView.setZoomActivated(toolsController.getTooltype() == 7);

        if (bundle == null) {
            Intent intent = getIntent();
            intent.getAction();
            intent.getType();
            Log.e("savedInstanceState", "onCreate: in loop");
            historyController.clear();
            loadImage(intent.getData());
            return;
        }
        lastPermissionRequest = new C04676();
        setParentEffect(position);

    }

    public void setParentEffect(int position){
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
        toolsController.onClick(moEffectlist.get(position), position);
//        if (moEffectlist.get(position).getEffectId() != -1) {
//            binding.gifView.setSpeed(1.0f);
//            binding.gifView.setAnimation(moEffectlist.get(position).getEffectName());
//
//            if (!moEffectlist.get(position).getEffectGIFPath().isEmpty()) {
//                binding.gifView.setImageAssetsFolder(moEffectlist.get(position).getEffectGIFPath());
//            }
//
//            binding.gifView.playAnimation();
//
//            int height = binding.imageView.getHeight();
//            int width = binding.imageView.getWidth();
//            int intrinsicHeight = binding.imageView.getDrawable().getIntrinsicHeight();
//            int intrinsicWidth = binding.imageView.getDrawable().getIntrinsicWidth();
//
//            int i2 = height * intrinsicWidth;
//            int i3 = width * intrinsicHeight;
//
//            if (i2 <= i3) {
//                width = i2 / intrinsicHeight;
//            } else {
//                height = i3 / intrinsicWidth;
//            }
//
//            // Create new LayoutParams for ConstraintLayout
//            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);
//
//            // Center the rlEffectsContainer
//            layoutParams.width = width; // Set the calculated width
//            layoutParams.height = height; // Set the calculated height
//
//            // Set the constraints to center it in the parent
//            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; // Align start to parent's start
//            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Align end to parent's end
//            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID; // Align top to parent's top
//            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // Align bottom to parent's bottom
//
//            // Apply layout params to the rlEffectsContainer
//            binding.rlEffectsContainer.setLayoutParams(layoutParams);
//
//            // Set ScaleType and visibility
//            binding.gifView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            binding.gifView.setVisibility(View.VISIBLE);
//
//            moEffectData = moEffectlist.get(position);
//            toolsController.playPreview();
//            binding.subToolSpeedPreview.setVisibility(View.VISIBLE);
//            if (toolsController.toolsListener != null) {
//                toolsController.toolsListener.onPlayPreview();
//            }
//        }
    }

    public void openNewProject() {
        toolsController.stopPreview();
        binding.imageView.restartZoom();
        lastPermissionRequest = new C04688();
    }

    @SuppressLint({"WrongConstant"})
    public void openLastProject() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", 0);
        String str = "idUltimoProjeto";
        long j = sharedPreferences.getLong(str, -1);
        if (j != -1) {
            Project projeto = db.getProjeto(j);
            if (projeto != null) {
                loadProject(projeto);
                return;
            }
            sharedPreferences.edit().remove(str).commit();
        }
        Project ultimoProjeto = db.getUltimoProjeto();
        if (ultimoProjeto != null) {
            loadProject(ultimoProjeto);
        }
    }

    public void updateProjectMaskInDatabase(Bitmap bitmap) {
        if (bitmap != null) {
            currentProject.setMask(Bitmap.createScaledBitmap(bitmap, Math.round(currentProject.getDisplayProportion() * ((float) bitmap.getWidth())), Math.round(currentProject.getDisplayProportion() * ((float) bitmap.getHeight())), true));
            new UpdateProjectTask().execute();
        }
    }

    public void loadMenus(Menu menu) {
        if (menu != null) {
            MenuItem findItem = menu.findItem(R.id.action_save);
            findItem.setIcon(Utils.getDrawable(this, R.drawable.ic_done_black_24dp, -1));
            findItem.setVisible(currentProject != null);

            MenuItem findItem1 = menu.findItem(R.id.action_detalhes);
            findItem1.setIcon(Utils.getDrawable(this, R.drawable.detalhes_inativo, -1));

            MenuItem findItem2 = menu.findItem(R.id.action_undo);
            findItem2.setIcon(Utils.getDrawable(this, R.drawable.ic_rotate_left_black_24dp, (currentProject == null || !historyController.andUndo()) ? Utils.getColor(this, R.color.white) : -1));
            findItem2.setEnabled(currentProject != null && historyController.andUndo());
            int i = -1;
            if (currentProject == null || !historyController.andRedo()) {
                i = Utils.getColor(this, R.color.white);
            }

            MenuItem findItem3 = menu.findItem(R.id.action_redo);
            findItem3.setIcon(Utils.getDrawable(this, R.drawable.ic_rotate_right_black_24dp, i));
            boolean z = true;
            if (currentProject == null || !historyController.andRedo()) {
                z = false;
            }
            findItem3.setEnabled(z);
        }
    }

    private void loadProject(Project projeto) {
        restartDefinitions();
        if (projeto.getImagem() != null || projeto.reloadBitmapUri(this, db)) {
            Editor edit = getSharedPreferences("MySharedPref", 0).edit();
            edit.putLong("idUltimoProjeto", projeto.getId());
            edit.commit();
            currentProject = projeto;
            imageLoaded = true;
            toolsController.setEnabled(true);
            if (projeto.getMascara() != null) {
                toolsController.setMascaraInicial(Bitmap.createScaledBitmap(projeto.getMascara(), Math.round((1.0f / projeto.getDisplayProportion()) * ((float) projeto.getMascara().getWidth())), Math.round((1.0f / projeto.getDisplayProportion()) * ((float) projeto.getMascara().getHeight())), true));
            }
            binding.toolBar.setVisibility(View.VISIBLE);
            loadMenus(secondaryMenu);
            animationController = AnimationController.init(currentProject);
            animationController.setOnAnimateListener(new C04699());
            uploadImageRepresentation();
            return;
        }
        projeto.deleteProjeto(db);
    }

    public void repositionMagnifyingGlass(float f, float f2) {
        int width = binding.imageZoom.getWidth() / 2;
        if (f > ((float) ((binding.imageView.getWidth() - binding.imageZoom.getWidth()) - width)) && f2 < binding.imageView.getY() + ((float) binding.imageZoom.getHeight()) + ((float) width)) {

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imageZoom.getLayoutParams();
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;  // Equivalent to removeRule for ConstraintLayout
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;  // Equivalent to addRule(RelativeLayout.ALIGN_PARENT_START)
            binding.imageZoom.setLayoutParams(layoutParams);

        }
        if (f < ((float) (binding.imageZoom.getWidth() + width)) && f2 < binding.imageView.getY() + ((float) binding.imageZoom.getHeight()) + ((float) width)) {
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) binding.imageZoom.getLayoutParams();

            layoutParams2.startToStart = ConstraintLayout.LayoutParams.UNSET;
            layoutParams2.endToEnd = ConstraintLayout.LayoutParams.UNSET;

            layoutParams2.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;  // Equivalent to addRule(21)
            binding.imageZoom.setLayoutParams(layoutParams2);
        }
    }

    private void restartDefinitions() {
        AnimationController animationController2 = animationController;
        if (animationController2 != null) {
            animationController2.stopAnimation(this);
            toolsController.stopPreview();
        }
        imageLoaded = false;
        toolsController.restartDefinitions();
        toolsController.setEnabled(false);
        binding.imageView.restartZoom();
    }

    @SuppressLint({"WrongConstant"})
    public void uploadImageRepresentation() {
        if (imageLoaded) {
            imageRepresentation = animationController.getImageRepresentation(showDetails, binding.imageView.getZoomScale());
            toolsController.pintar(imageRepresentation, binding.imageView.getZoomScale());
            if (toolsController.getTooltype() == 3 || toolsController.getTooltype() == 6 || toolsController.getTooltype() == 2 || toolsController.getTooltype() == 1 || toolsController.getTooltype() == 5) {
                binding.imageZoom.setImageBitmap(imageRepresentation, binding.imageView.getZoomScale());
            }
            binding.imageView.setImageBitmap(imageRepresentation);
        }
    }

    public void getBitmapList(OnBitmapListCreated onBitmapListCreated) {
        moOnBitmapListCreated = onBitmapListCreated;
        binding.gifView.setVisibility(View.VISIBLE);
        binding.customView.setVisibility(View.VISIBLE);
        moBitmapList = new ArrayList();
        binding.customView.pauseAnimation();
        binding.gifView.pauseAnimation();
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                new BitmapTask().execute(new Void[0]);
            }
        }, 1000);
    }

    @SuppressLint({"WrongConstant"})
    public void loadImage(Uri uri) {
        imageLoaded = false;
        historyController.clear();
        Options options = new Options();
        options.inJustDecodeBounds = true;
        try {
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            int i = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
            if (i > SavingActivity.MAX_RESOLUTION_SAVE) {
                i = SavingActivity.MAX_RESOLUTION_SAVE;
            }
            options.inSampleSize = Utils.calculateInSampleSize(options, i, i);
            options.inJustDecodeBounds = false;
            InputStream openInputStream2 = getContentResolver().openInputStream(uri);
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream2, null, options);
            toolsController.setEffectAdapter(decodeStream);
            openInputStream2.close();
            StringBuilder sb = new StringBuilder();
            sb.append(getResources().getString(R.string.app_name));
//            sb.append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
            sb.append((int) (Math.random() * 1000000.0d));
            String sb2 = sb.toString();
            Project projeto = new Project(sb2, decodeStream.copy(Config.ARGB_8888, true), Utils.writeImageAndGetPathUri(this, decodeStream, sb2));
            projeto.setResolucaoSave(i);
            projeto.addProjeto(db);
            loadProject(projeto);
            // Toast.makeText(this, getResources().getText(R.string.load_image_fail), 1).show();
        } catch (Exception unused) {
            Toast.makeText(this, "Error loading image!", Toast.LENGTH_LONG);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (intent != null) {
            String str = "idProjeto";
            if (intent.hasExtra(str)) {
                historyController.clear();
                loadProject(db.getProjeto(intent.getLongExtra(str, -1)));
            } else if (i == 1 && i2 == -1) {
                historyController.clear();
                loadImage(intent.getData());
            }
        }
        Project projeto = currentProject;
        if (projeto != null && db.getProjeto(projeto.getId()) == null) {
            historyController.clear();
            openLastProject();
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        AlbumListActivity albumImagesActivity = AlbumListActivity.activity;
        if (albumImagesActivity != null) {
            albumImagesActivity.finish();
        }
        Utils.BackButtonDialog(this, toolsController, historyController);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

   

    public boolean onCreateOptionsMenu(Menu menu) {
        secondaryMenu = menu;
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        loadMenus(menu);
        return true;
    }

    public boolean onMenuOpened(int i, Menu menu) {
        return super.onMenuOpened(i, menu);
    }

    @SuppressLint({"WrongConstant"})
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }




    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_detalhes:
                toolsController.stopPreview();
                showDetails = !showDetails;
                menuItem.setChecked(showDetails);
                if (showDetails) {
                    menuItem.setIcon(Utils.getDrawable(this, R.drawable.ic_detalhes_ativo, -1));
                } else {
                    menuItem.setIcon(Utils.getDrawable(this, R.drawable.detalhes_inativo, -1));
                }
                uploadImageRepresentation();
                return true;
            case R.id.action_redo:
                toolsController.stopPreview();
                for (HistoryController.ObjectHistory objectHistory : historyController.redo()) {
                    if (objectHistory.isChanged()) {
                        toolsController.addMask(objectHistory.getMask());
                        updateProjectMaskInDatabase(objectHistory.getMask());
                    } else if (!objectHistory.hasMask()) {
                        Point ponto = objectHistory.getPoint();
                        currentProject.deletePonto(db, ponto);
                        animationController.deletePoint(ponto);
                    } else {
                        Point ponto2 = objectHistory.getPoint();
                        currentProject.addPonto(db, ponto2);
                        animationController.addPoint(ponto2);
                    }
                }
                uploadImageRepresentation();
                loadMenus(secondaryMenu);
                return true;
            case R.id.action_save:
                if (ToolsController.controller.getCurrentStickerPosition() == -1 && ToolsController.controller.getSelecetdEffect().getEffectId() == -1) {
                    ShareClass.foBitmapList = new ArrayList();
//                    getWindow().setFlags(16, 16);
                    toolsController.deleteSelection();
                    toolsController.stopPreview();
                    binding.imageView.restartZoom();
                    toolsController.setTooltype(0);
                    Intent intent = new Intent(MotionEditActivity.this, SavingActivity.class);
                    intent.putExtra(SavingActivity.INTENT_PROJETO, currentProject);


                        startActivity(intent);

                } else {
                    binding.customView.hideFrame(true);
                    moDialog = new ProgressDialog(this);
                    moDialog.setMessage("Loading...");
                    moDialog.setCancelable(false);
                    moDialog.show();
                    getBitmapList(list -> {
                        ShareClass.foBitmapList = list;
                        getWindow().setFlags(16, 16);
                        toolsController.deleteSelection();
                        toolsController.stopPreview();
                        binding.imageView.restartZoom();
                        toolsController.setTooltype(0);
                        Intent intent = new Intent(MotionEditActivity.this, SavingActivity.class);
                        intent.putExtra(SavingActivity.INTENT_PROJETO, currentProject);
                        startActivity(intent);
                        moDialog.dismiss();
                    });
                }
                return true;
            case R.id.action_undo:
                toolsController.stopPreview();
                List<HistoryController.ObjectHistory> desfazer = historyController.undo();
                CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
                for (HistoryController.ObjectHistory objectHistory2 : desfazer) {
                    if (!objectHistory2.isChanged()) {
                        Point ponto3 = objectHistory2.getPoint();
                        if (objectHistory2.hasMask()) {
                            copyOnWriteArrayList.add(ponto3);
                        } else if (!animationController.existsPoint(ponto3)) {
                            currentProject.addPonto(db, ponto3);
                            animationController.addPoint(ponto3);
                        } else {
                            currentProject.addPonto(db, ponto3);
                            animationController.addPoint(ponto3);
                        }
                    } else {
                        toolsController.addMask(objectHistory2.getMask());
                        updateProjectMaskInDatabase(objectHistory2.getMask());
                    }
                }
                if (!copyOnWriteArrayList.isEmpty()) {
                    currentProject.deletePontos(db, copyOnWriteArrayList);
                    animationController.deletePoints(copyOnWriteArrayList);
                }
                uploadImageRepresentation();
                loadMenus(secondaryMenu);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void onPause() {
        super.onPause();
        toolsController.stopPreview();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 2) {
            int length = iArr.length;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    z = true;
                    break;
                } else if (iArr[i2] != 0) {
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                lastPermissionRequest.onPermissionGranted();
            }
        }
    }

    public void onResume() {
        if (binding.transparentLayout.getVisibility() == View.VISIBLE) {
            binding.transparentLayout.setVisibility(View.GONE);
        }
        getWindow().clearFlags(16);

        if (ToolsController.controller != null) {
            if (ToolsController.controller.getCurrentStickerPosition() != -1) {
                binding.customView.hideFrame(false);
            }

            if (ToolsController.controller.getSelecetdEffect() != null ) {
                if (ToolsController.controller.getSelecetdEffect().getEffectId() != -1) {
                    binding.gifView.setVisibility(View.VISIBLE);
                    binding.gifView.playAnimation();
                }
            }

        }


        super.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void resetSelection() {
        toolsController = ToolsController.init(this, position);
        toolsController.setEnabled(true);
        toolsController.deleteSelection();
        toolsController.setTooltype(0);
    }

    public interface ListenerPermission {
        void onPermissionGranted();
    }

    @SuppressLint({"StaticFieldLeak"})
    public class BitmapTask extends AsyncTask<Void, Void, Boolean> {

        public void onPreExecute() {
            super.onPreExecute();
        }

        public Boolean doInBackground(Void... voidArr) {
            int i = 0;
            while (i < 60) {
                try {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        public final void run() {
                            int i2 = finalI + 1;
                            binding.gifView.setFrame(i2);
                            binding.customView.setFrame(i2);
                            moBitmapList.add(BitmapHelper.getBitmap2(binding.rlEffectsContainer));
                        }
                    });
                    Thread.sleep(100);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.valueOf(false);
                }
            }
            return Boolean.valueOf(true);
        }

        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            moOnBitmapListCreated.onBitmapReceived(moBitmapList);
            StringBuilder sb = new StringBuilder();
            sb.append("onPostExecute: ");
            sb.append(moBitmapList.size());
            Log.e("Test", sb.toString());
        }
    }

    public class simpleOnScaleGestureListener extends SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            uploadImageRepresentation();
            return super.onScale(scaleGestureDetector);
        }
    }

    public class onTouchListener implements OnTouchListener {
        @SuppressLint({"WrongConstant"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (imageLoaded) {
                Rect bounds = binding.imageView.getDrawable().getBounds();
                int width = (binding.imageView.getWidth() - bounds.right) / 2;
                int height = (binding.imageView.getHeight() - bounds.bottom) / 2;
                if (!toolsController.isPlayingPreview()) {
                    int actionIndex = motionEvent.getActionIndex();
                    float[] fArr = {motionEvent.getX(actionIndex), motionEvent.getY(actionIndex)};
                    Matrix matrix = new Matrix();
                    binding.imageView.getImageMatrix().invert(matrix);
                    matrix.postTranslate((float) binding.imageView.getScrollX(), (float) binding.imageView.getScrollY());
                    matrix.mapPoints(fArr);
                    float f = fArr[0];
                    float f2 = fArr[1];
                    Point ponto = new Point(f, f2, true);
                    toolsController.setSubToolTamanhoPincelVisibility(false);
                    toolsController.setSubToolVelocidadeVisibility(false);
                    Paint paint = new Paint(1);
                    paint.setStyle(Style.FILL);
                    paint.setFilterBitmap(true);
                    paint.setColor(-65536);
                    if (motionEvent.getAction() == 0) {
                        mouseDragging = true;
                        xInitMouse = f;
                        yInitMouse = f2;
                        MotionEditActivity editActivity = MotionEditActivity.this;
                        editActivity.startingPoint = new Point(editActivity.xInitMouse, yInitMouse, true);
                        switch (toolsController.getTooltype()) {
                            case 1:
                                MotionEditActivity editActivity2 = MotionEditActivity.this;
                                editActivity2.startingPoint = new Point(editActivity2.xInitMouse, yInitMouse, false);
                                if (!animationController.existsPoint(startingPoint)) {
                                    currentProject.addPonto(db, startingPoint);
                                    animationController.addPoint(startingPoint);
                                    break;
                                }
                                break;
                            case 2:
                                MotionEditActivity editActivity3 = MotionEditActivity.this;
                                editActivity3.repositionMagnifyingGlass(editActivity3.xInitMouse, yInitMouse);
                                uploadImageRepresentation();
                                binding.imageZoom.setPosicaoLupa(f, f2);
                                binding.imageZoom.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                repositionMagnifyingGlass(f, f2);
                                toolsController.setMascarando(true);
                                toolsController.addMask(xInitMouse, yInitMouse, binding.imageView.getZoomScale());
                                uploadImageRepresentation();
                                binding.imageZoom.setPosicaoLupa(f, f2);
                                binding.imageZoom.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                toolsController.deleteSelection();
                                break;
                            case 5:
                                MotionEditActivity editActivity4 = MotionEditActivity.this;
                                editActivity4.startingPoint = new Point(editActivity4.xInitMouse, yInitMouse, false);
                                if (!animationController.existsPoint(startingPoint)) {
                                    currentProject.addPonto(db, startingPoint);
                                    animationController.addPoint(startingPoint);
                                    pointsSequence = new CopyOnWriteArrayList();
                                    pointsSequence.add(startingPoint);
                                    break;
                                }
                                break;
                            case 6:
                                repositionMagnifyingGlass(f, f2);
                                toolsController.setMascarando(true);
                                toolsController.deleteMask(xInitMouse, yInitMouse, binding.imageView.getZoomScale());
                                uploadImageRepresentation();
                                binding.imageZoom.setPosicaoLupa(f, f2);
                                binding.imageZoom.setVisibility(View.VISIBLE);
                                break;
                        }
                        uploadImageRepresentation();
                        return true;
                    } else if (motionEvent.getAction() == 2) {
                        if (mouseDragging) {
                            switch (toolsController.getTooltype()) {
                                case 1:
                                    startingPoint.setDestino(f, f2);
                                    break;
                                case 2:
                                    repositionMagnifyingGlass(motionEvent.getX(), motionEvent.getY());
                                    uploadImageRepresentation();
                                    binding.imageZoom.setPosicaoLupa(f, f2);
                                    break;
                                case 3:
                                    repositionMagnifyingGlass(motionEvent.getX(), motionEvent.getY());
                                    toolsController.addMask(f, f2, binding.imageView.getZoomScale());
                                    uploadImageRepresentation();
                                    binding.imageZoom.setPosicaoLupa(f, f2);
                                    break;
                                case 4:
                                    toolsController.setSelecionando(startingPoint, ponto);
                                    if (animationController.hasSelectedPoint()) {
                                        toolsController.setDeleteToolVisibility(true);
                                        break;
                                    } else {
                                        toolsController.setDeleteToolVisibility(false);
                                        break;
                                    }
                                case 5:
                                    startingPoint.setDestino(f, f2);
                                    if (startingPoint.distanciaPara(ponto) >= ((double) (((float) toolsController.getTamMovSequencia()) / binding.imageView.getZoomScale()))) {
                                        currentProject.updatePonto(db, startingPoint);
                                        startingPoint = new Point(f, f2, false);
                                        currentProject.addPonto(db, startingPoint);
                                        animationController.addPoint(startingPoint);
                                        pointsSequence.add(startingPoint);
                                        break;
                                    }
                                    break;
                                case 6:
                                    repositionMagnifyingGlass(motionEvent.getX(), motionEvent.getY());
                                    toolsController.deleteMask(f, f2, binding.imageView.getZoomScale());
                                    uploadImageRepresentation();
                                    binding.imageZoom.setPosicaoLupa(f, f2);
                                    break;
                            }
                            uploadImageRepresentation();
                        }
                        return true;
                    } else if (motionEvent.getAction() == 1) {
                        mouseDragging = false;
                        binding.imageZoom.setVisibility(View.INVISIBLE);
                        switch (toolsController.getTooltype()) {
                            case 1:
                                startingPoint.setDestino(f, f2);
                                currentProject.updatePonto(db, startingPoint);
                                historyController.addHistory(startingPoint, true);
                                break;
                            case 2:
                                currentProject.addPonto(db, ponto);
                                animationController.addPoint(ponto);
                                historyController.addHistory(ponto, true);
                                break;
                            case 3:
                                toolsController.setMascarando(false);
                                updateProjectMaskInDatabase(ToolsController.getMask());
                                Paint paint2 = new Paint(1);
                                paint2.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
                                paint2.setFilterBitmap(true);
                                paint2.setStyle(Style.FILL);
                                break;
                            case 4:
                                toolsController.setSelecionando(startingPoint, ponto);
                                toolsController.setSelecionando(false);
                                break;
                            case 5:
                                startingPoint.setDestino(f, f2);
                                currentProject.updatePonto(db, startingPoint);
                                historyController.addHistory(pointsSequence, true);
                                break;
                            case 6:
                                toolsController.setMascarando(false);
                                updateProjectMaskInDatabase(ToolsController.getMask());
                                break;
                        }
                        uploadImageRepresentation();
                        MotionEditActivity editActivity5 = MotionEditActivity.this;
                        editActivity5.loadMenus(editActivity5.secondaryMenu);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public class UpdateProjectTask extends AsyncTask {
        public UpdateProjectTask() {
        }

        public Object doInBackground(Object[] objArr) {
            currentProject.updateProjeto(db);
            return null;
        }
    }

    public class toolsListener implements ToolsController.ToolsListener {
        @SuppressLint({"WrongConstant"})
        public void onChangeRaioMascara(int i, Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap createBitmap = Bitmap.createBitmap(imageRepresentation.getWidth(), imageRepresentation.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float width = (((float) createBitmap.getWidth()) / 2.0f) - (((float) bitmap.getWidth()) / 2.0f);
                float height = (((float) createBitmap.getHeight()) / 2.0f) - (((float) bitmap.getHeight()) / 2.0f);
                Paint paint = new Paint(1);
                paint.setFilterBitmap(true);
                paint.setAlpha(ToolsController.getAlphaMask());
                canvas.drawBitmap(bitmap, width, height, paint);
            }
        }

        @SuppressLint({"WrongConstant"})
        public void onChangeTamanhoSetaMovSequencia(int i, Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap createBitmap = Bitmap.createBitmap(imageRepresentation.getWidth(), 50, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(Color.argb(ToolsController.getAlphaMask(), 0, 0, 0));
                canvas.drawBitmap(bitmap, (float) ((createBitmap.getWidth() / 2) - (bitmap.getWidth() / 2)), (float) (createBitmap.getHeight() / 2), null);
                return;
            }
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        @SuppressLint({"WrongConstant"})
        public void onPlayPreview() {
            binding.imageView.restartZoom();
            if (toolsController.getTooltype() == 7) {
                binding.imageView.setZoomActivated(false);
            }
            currentProject.refreshTempoResolucao(db);
            toolsController.setTempoPreview(currentProject.getTempoSave());
            animationController.setTimeAnimation(currentProject.getTempoSave());
            animationController.startAnimation();
            binding.ibPlayPause.setImageResource(R.drawable.ic_menu_stop);
        }

        public void onPressApagarMascara() {
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        public void onPressDelete() {
            List listaPontosSelecionados = animationController.getListSelectedPoints();
            if (listaPontosSelecionados != null && !listaPontosSelecionados.isEmpty()) {
                currentProject.deletePontos(db, listaPontosSelecionados);
                animationController.deleteSelectedPoints();
                historyController.addHistory(listaPontosSelecionados, false);
            }
            toolsController.setDeleteToolVisibility(false);
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
            MotionEditActivity editActivity = MotionEditActivity.this;
            editActivity.loadMenus(editActivity.secondaryMenu);
        }

        public void onPressEsbilizar() {
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        public void onPressMascara() {
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        public void onPressSelecao() {
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        public void onPressSetaMovSequencia() {
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        public void onPressSetaMovimento() {
            binding.imageView.setZoomActivated(false);
            uploadImageRepresentation();
        }

        public void onPressZoom() {
            binding.imageView.setZoomActivated(true);
            uploadImageRepresentation();
        }

        public void onStopPreview() {
            if (toolsController.getTooltype() == 7) {
                binding.imageView.setZoomActivated(true);
            }
            if (animationController != null) {
                animationController.stopAnimation(MotionEditActivity.this);
            }
            binding.ibPlayPause.setImageResource(R.drawable.ic_menu_play);
            uploadImageRepresentation();
        }

        public void onTempoAlterado(int i) {
            animationController.setTimeAnimation(i);
            currentProject.setTempoSave(i);
            currentProject.updateProjeto(db);
        }

        public void onTempoChanging(int i) {
            animationController.setTimeAnimation(i);
        }
    }

    public class C04676 implements ListenerPermission {
        public C04676() {
        }

        public void onPermissionGranted() {
            Log.i("INFO", "LOADING ULTIMO PROJETO ....");
            openLastProject();
        }
    }

    public class C04688 implements ListenerPermission {
        public C04688() {
        }

        public void onPermissionGranted() {
            Intent intent = new Intent("android.intent.action.PICK", Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                MotionEditActivity editActivity = MotionEditActivity.this;
                editActivity.startActivityForResult(Intent.createChooser(intent, "Select an image"), 1);
            }
        }
    }

    public class C04699 implements AnimationController.AnimateListener {
        public C04699() {
        }

        public void onAnimate(Bitmap bitmap) {
            if (imageLoaded && toolsController.isPlayingPreview()) {
                binding.imageView.setImageBitmap(bitmap);
            }
        }
    }

    public class playPauseClick implements OnClickListener {
        public playPauseClick() {
        }

        public void onClick(View view) {
            toolsController.deleteSelection();
            if (toolsController.isPlayingPreview()) {
                toolsController.stopPreview();
                CustomAnimationView customAnimationView = binding.customView;
                if (customAnimationView != null) {
                    customAnimationView.setVisibility(View.GONE);
                    return;
                }
                return;
            }
            toolsController.playPreview();
            if (toolsController.getSelecetdEffect().getEffectId() != -1) {
                toolsController.playPreview();
                binding.gifView.setSpeed(1.0f);
                MotionEditActivity editActivity = MotionEditActivity.this;
                editActivity.binding.gifView.setAnimation(editActivity.moEffectData.getEffectName());
                if (!moEffectData.getEffectGIFPath().isEmpty()) {
                    MotionEditActivity editActivity2 = MotionEditActivity.this;
                    editActivity2.binding.gifView.setImageAssetsFolder(editActivity2.moEffectData.getEffectGIFPath());
                }
                binding.gifView.playAnimation();
                binding.gifView.setVisibility(View.VISIBLE);
            }
            if (toolsController.getCurrentStickerPosition() != -1) {
                binding.customView.setVisibility(View.VISIBLE);
            } else {
                binding.customView.setVisibility(View.GONE);
            }
        }
    }
}
