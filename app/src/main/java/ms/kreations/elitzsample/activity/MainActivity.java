package ms.kreations.elitzsample.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ms.kreations.elitzsample.model.ModelAudioList;
import ms.kreations.elitzsample.R;
import ms.kreations.elitzsample.adapter.AdapterAudio;
import ms.kreations.elitzsample.utility.ItemMoveCallback;

public class MainActivity extends AppCompatActivity {
    public static final int RUNTIME_PERMISSION_CODE = 7;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;
    Button button;
    RecyclerView rvAudioList;
    private AdapterAudio adapterProduct;
    private List<ModelAudioList> modelAudioLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvAudioList = findViewById(R.id.rvAudioList);
        button = findViewById(R.id.button);
        rvAudioList.setHasFixedSize(true);
        rvAudioList.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        modelAudioLists = new ArrayList<>();
        androidRuntimePermission();
        button.setOnClickListener((View.OnClickListener) view -> {
            GetAllMediaMp3Files();
            adapterProduct = new AdapterAudio(modelAudioLists, MainActivity.this);
            ItemTouchHelper.Callback callback =
                    new ItemMoveCallback((ItemMoveCallback.ItemTouchHelperContract) adapterProduct);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(rvAudioList);
            rvAudioList.setAdapter(adapterProduct);
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    adapterProduct.notifyDataSetChanged();
                    startPlayback(viewHolder.getLayoutPosition());
                } else if (direction == ItemTouchHelper.LEFT) {
                    adapterProduct.notifyDataSetChanged();
                    startPlayback(viewHolder.getLayoutPosition());
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvAudioList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPlayback(int position) {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(String.valueOf(modelAudioLists.get(position).getUrl()));
            mp.prepare();
            mp.start();
            mediaPlayer = mp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetAllMediaMp3Files() {
        modelAudioLists.clear();
        contentResolver = MainActivity.this.getContentResolver();
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG);
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);
        } else {
            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int url = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String SongTitle = cursor.getString(Title);
                String urlnew = cursor.getString(url);
                ModelAudioList modelAudioList = new ModelAudioList(SongTitle, urlnew);
                modelAudioLists.add(modelAudioList);
            } while (cursor.moveToNext());
        }
    }


    public void androidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE
                            );
                        }
                    });
                    alert_builder.setNeutralButton("Cancel", null);
                    AlertDialog dialog = alert_builder.create();
                    dialog.show();
                } else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RUNTIME_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }


}