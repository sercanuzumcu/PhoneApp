package com.merhabaandroid.sercan.photoapp;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    private SurfaceView yuzey;
    private SurfaceHolder yuzeyKalibi;
    private Camera kamera;
    private ImageView fotoCekmeDugmesi;
    public  static final String fotoKlasöradi = "fotograf_dizini";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kamera_onizleme);
        yuzey = (SurfaceView) findViewById(R.id.kamera_onizleme);
        yuzeyKalibi = yuzey.getHolder();
        yuzeyKalibi.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
       yuzeyKalibi.addCallback(new SurfaceHolder.Callback() {
                                   @Override
                                   public void surfaceCreated(SurfaceHolder holder) {
                                       kamera = Camera.open();
                                       try {

                                           kamera.setPreviewDisplay(holder);
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                           ;
                                       }
                                       kamera.startPreview();
                                   }

                                   @Override
                                   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                                   }

                                   @Override
                                   public void surfaceDestroyed(SurfaceHolder holder) {
                                       kamera.stopPreview();
                                       kamera.release();
                                   }
                               }
       );

        fotoCekmeDugmesi =(ImageView) findViewById(R.id.foto_cekme_dugmesi);
        fotoCekmeDugmesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kamera.takePicture(null,null,null, new Camera.PictureCallback(){
                    public void onPictureTaken(byte[] data, Camera camera){
                        fotografKaydet(data);
                  Intent i = new Intent(getApplicationContext(),FotografGalerisi.class);
                        startActivity(i);
                    }
                });
            }
        });

        }

    private void fotografKaydet(byte[] data){
        File fileDir = getFilesDir();
        File fotografKlasoru = new File(fileDir.getAbsolutePath(), fotoKlasöradi);
        if (fotografKlasoru.exists() == false){
            fotografKlasoru.mkdir();
        }
        Calendar calendar = Calendar.getInstance();
        String fileName = calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.get(Calendar.HOUR) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND) + ".jpg";
        File fotograf = new File(fotografKlasoru.getAbsolutePath(),fileName);
        Log.e("path", fotografKlasoru.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(fotograf);
            fos.write(data);
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
