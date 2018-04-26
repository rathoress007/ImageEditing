package nnk.com.imageproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class ImageActivity extends AppCompatActivity {

    private String text;
    private TextView textView;
    private ImageButton imageButton;
    private ImageView imageView, imageView1, stickerImage, eyeImage;
    final public int REQUEST_CODE = 20;
    final public int RESULT_LOAD_IMG = 30;
    private Bitmap bitmap;
    private int currentColor;
    Uri imageUri;
    int IMAGE_URL_CODE = 143;
    int PERMISSION_ALL = 1;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    private  Bitmap bmap;
    private Button doneButton, galleryButton, frameButton, stickerButton, eyeButton,newButton;
    private AbsoluteLayout layout;
    private float m_oldDist = 1f, m_scale, m_oldX = 0, m_oldY = 0, m_dX, m_dY,
            m_posX, m_posY, m_prevX = 0, m_prevY = 0, m_newX, m_newY;
    private RecyclerView recyclerView;
    private int images[] = {R.drawable.frame_118, R.drawable.frame_big1, R.drawable.frame_big2, R.drawable.frame_big3, R.drawable.frame_big4
            , R.drawable.frame_big7, R.drawable.frame_big8};
    private int noses[] = {R.drawable.snap_cat_nose_01, R.drawable.snap_cat_nose_02, R.drawable.snap_cat_nose_03, R.drawable.snap_cat_nose_04
            , R.drawable.snap_cat_nose_05, R.drawable.snap_cat_nose_06, R.drawable.snap_cat_nose_07, R.drawable.snap_cat_nose_08};
    private ImageActivity imageActivity;

    private int defaultImage[] = {R.drawable.theam_big1,R.drawable.theam_big2,R.drawable.theam_big3,R.drawable.theam_big4,R.drawable.theam_big5
    ,R.drawable.theam_big6,R.drawable.theam_big7,R.drawable.theam_big8,R.drawable.theam_big9,R.drawable.theam_big10};
    private int sticker[] = {R.drawable.stck1, R.drawable.stck2, R.drawable.stck3, R.drawable.stck4, R.drawable.stck5, R.drawable.stck6
            , R.drawable.stck7, R.drawable.stck8, R.drawable.stck9, R.drawable.stck10};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image);


        imageActivity = this;
        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image_view);
        doneButton = (Button) findViewById(R.id.done_button);
        imageView1 = (ImageView) findViewById(R.id.image_view1);
        stickerImage = (ImageView) findViewById(R.id.stickerView);
        eyeImage = (ImageView) findViewById(R.id.EyeView);
        galleryButton = (Button) findViewById(R.id.gallary_button);
       newButton = (Button) findViewById(R.id.new_button);
        frameButton = (Button) findViewById(R.id.frame_button);
        stickerButton = (Button) findViewById(R.id.sticker_button);
        eyeButton = (Button) findViewById(R.id.eye);
        layout = (AbsoluteLayout) findViewById(R.id.relative_layout);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layout = (AbsoluteLayout)findViewById(R.id.relative_layout);
        layout.setBackgroundColor(Color.TRANSPARENT);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        text = bundle.getString("text");
        currentColor = bundle.getInt("color");
        textView.setText(text);
        textView.setTextSize(TextActivity.count);
      //  Toast.makeText(this, "" + TextActivity.count, Toast.LENGTH_SHORT).show();
        textView.setTextColor(currentColor);
        textView.setTypeface(TextActivity.current);
        imageButton = (ImageButton) findViewById(R.id.camera_button);
        recyclerView.setHasFixedSize(true);
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        imageView.setImageResource(R.drawable.theam_big1);
          defaultAdapter defaultAdapter = new defaultAdapter(ImageActivity.this, defaultImage, new defaultAdapter.InterfOnclick() {
              @Override
              public void click(View view, int pos) {
                  imageView.setImageResource(defaultImage[pos]);
              }
          });
        recyclerView.setAdapter(defaultAdapter);

        eyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
         //           Toast.makeText(ImageActivity.this, "eye button is clicked", Toast.LENGTH_SHORT).show();
                    EyeEffectAdapter eyeEffectAdapter = new EyeEffectAdapter(ImageActivity.this,noses, new EyeEffectAdapter.InterfOnclick() {
                        @Override
                        public void click(View view, int pos) {
                            eyeImage.setImageResource(noses[pos]);
                        }
                    });
                    recyclerView.setAdapter(eyeEffectAdapter);
                }
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
           //     Toast.makeText(ImageActivity.this, "in touch method", Toast.LENGTH_SHORT).show();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        m_oldX = event.getX();
                        m_oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        m_dX = event.getX() - m_oldX;
                        m_dY = event.getY() - m_oldY;
                        m_posX = m_prevX + m_dX;
                        m_posY = m_prevY + m_dY;
                        if (m_posX > 0 && m_posY > 0 && (m_posX + v.getWidth()) < layout.getWidth() && (m_posY + v.getHeight()) < layout.getHeight()) {
                            v.setLayoutParams(new AbsoluteLayout.LayoutParams(v.getMeasuredWidth(), v.getMeasuredHeight(), (int) m_posX, (int) m_posY));
                            m_prevX = m_posX;
                            m_prevY = m_posY;
                        }
                        break;
                }
                return true;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        stickerImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stickerImage.setClickable(true);
                eyeImage.setClickable(false);
                stickerImage = (ImageView) v;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        lastEvent = new float[4];
                        lastEvent[0] = event.getX(0);
                        lastEvent[1] = event.getX(1);
                        lastEvent[2] = event.getY(0);

                        lastEvent[3] = event.getY(1);
                        d = rotation(event);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            float dx = event.getX() - start.x;
                            float dy = event.getY() - start.y;
                            matrix.postTranslate(dx, dy);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = (newDist / oldDist);
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                            if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                                newRot = rotation(event);
                                float r = newRot - d;
                                float[] values = new float[9];
                                matrix.getValues(values);
                                float tx = values[2];
                                float ty = values[5];
                                float sx = values[0];
                                float xc = (stickerImage.getWidth() / 2) * sx;
                                float yc = (stickerImage.getHeight() / 2) * sx;
                                matrix.postRotate(r, tx + xc, ty + yc);
                            }
                        }
                        break;
                }

                stickerImage.setImageMatrix(matrix);

                bmap= Bitmap.createBitmap(stickerImage.getWidth(), stickerImage.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bmap);
                stickerImage.draw(canvas);
                return true;
            }
        });



        eyeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               /*stickerImage.setClickable(false);
                eyeImage.setClickable(true);*/
                eyeImage = (ImageView) v;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        lastEvent = new float[4];
                        lastEvent[0] = event.getX(0);
                        lastEvent[1] = event.getX(1);
                        lastEvent[2] = event.getY(0);

                        lastEvent[3] = event.getY(1);
                        d = rotation(event);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            float dx = event.getX() - start.x;
                            float dy = event.getY() - start.y;
                            matrix.postTranslate(dx, dy);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = (newDist / oldDist);
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                            if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                                newRot = rotation(event);
                                float r = newRot - d;
                                float[] values = new float[9];
                                matrix.getValues(values);
                                float tx = values[2];
                                float ty = values[5];
                                float sx = values[0];
                                float xc = (eyeImage.getWidth() / 2) * sx;
                                float yc = (eyeImage.getHeight() / 2) * sx;
                                matrix.postRotate(r, tx + xc, ty + yc);
                            }
                        }
                        break;
                }

                eyeImage.setImageMatrix(matrix);

                bmap= Bitmap.createBitmap(eyeImage.getWidth(), eyeImage.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bmap);
                eyeImage.draw(canvas);
                return true;
            }
        });

         /*stickerImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

             //   Toast.makeText(ImageActivity.this, "in sticker touch method", Toast.LENGTH_SHORT).show();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        m_oldX = event.getX();
                        m_oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        m_dX = event.getX() - m_oldX;
                        m_dY = event.getY() - m_oldY;
                        m_posX = m_prevX + m_dX;
                        m_posY = m_prevY + m_dY;
                        if (m_posX > 0 && m_posY > 0 && (m_posX + v.getWidth()) < layout.getWidth() && (m_posY + v.getHeight()) < layout.getHeight()) {
                            v.setLayoutParams(new AbsoluteLayout.LayoutParams(v.getMeasuredWidth(), v.getMeasuredHeight(), (int) m_posX, (int) m_posY));
                            m_prevX = m_posX;
                            m_prevY = m_posY;
                        }
                        break;
                }
                return true;
            }
        });*/
        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    StickerAdapter stickerAdapter = new StickerAdapter(imageActivity, sticker, new StickerAdapter.InterfOnclick() {
                        @Override
                        public void click(View view, int pos) {
                            stickerImage.setImageResource(sticker[pos]);
               //             Toast.makeText(imageActivity, "position" + pos, Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(stickerAdapter);

                }
            }
        });
        frameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    FrameAdapter stickerAdapter = new FrameAdapter(imageActivity,images, new FrameAdapter.InterfOnclick() {
                        @Override
                        public void click(View view, int pos) {
                            imageView1.setBackgroundResource(images[pos]);
                 //           Toast.makeText(imageActivity, "position" + pos, Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(stickerAdapter);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File filePhoto = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                filePhoto.delete();
                imageUri = Uri.fromFile(filePhoto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CODE);
            }

        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    Drawable bgDrawable = layout.getBackground();
                    if (bgDrawable != null)
                        bgDrawable.draw(canvas);
                    else
                        canvas.drawColor(Color.WHITE);
                    layout.draw(canvas);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/My Application");
                    myDir.mkdirs();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir + File.separator, fname);
                    if (file.exists()) file.delete();
                    try {
                        file.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(bytes.toByteArray());
                        outputStream.flush();
                        outputStream.close();
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(file));
                        sendBroadcast(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
                    startActivity(Intent.createChooser(share, "Share via"));
                    Toast.makeText(ImageActivity.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                    doneButton.setVisibility(View.GONE);
                    newButton.setVisibility(View.VISIBLE);

                }





            }


        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               imageView.setImageResource(defaultImage[0]);
                imageView.setImageResource(R.drawable.theam_big1);
                defaultAdapter defaultAdapter = new defaultAdapter(ImageActivity.this, defaultImage, new defaultAdapter.InterfOnclick() {
                    @Override
                    public void click(View view, int pos) {
                        imageView.setImageResource(defaultImage[pos]);
                    }
                });
                recyclerView.setAdapter(defaultAdapter);
                imageView1.setImageResource(0);
                stickerImage.setImageResource(0);
                eyeImage.setImageResource(0);
                doneButton.setVisibility(View.VISIBLE);
                newButton.setVisibility(View.GONE);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                               final CharSequence[] items = { "Choose from Gallery", "Search on Google",
                        "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Choose from Gallery")) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                        } else if (items[item].equals("Search on Google")) {
                           // Toast.makeText(imageActivity, "Coming soon", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(imageActivity,MyWebView.class);
                            startActivityForResult(intent,IMAGE_URL_CODE);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        /*eyeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        m_oldX = event.getX();
                        m_oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        m_dX = event.getX() - m_oldX;
                        m_dY = event.getY() - m_oldY;
                        m_posX = m_prevX + m_dX;
                        m_posY = m_prevY + m_dY;
                        if (m_posX > 0 && m_posY > 0 && (m_posX + v.getWidth()) < layout.getWidth() && (m_posY + v.getHeight()) < layout.getHeight()) {
                            v.setLayoutParams(new AbsoluteLayout.LayoutParams(v.getMeasuredWidth(), v.getMeasuredHeight(), (int) m_posX, (int) m_posY));
                            m_prevX = m_posX;
                            m_prevY = m_posY;
                        }
                        break;
                }
                return true;
            }
        });*/
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s=x * x + y * y;
        return (float)Math.sqrt(s);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == this.REQUEST_CODE) {
            Uri selectedImage = imageUri ;
            this.getContentResolver().notifyChange(selectedImage, null);
            ContentResolver cr = this.getContentResolver();
            Bitmap bitmap;
            try {
                bitmap = android.provider.MediaStore.Images.Media
                        .getBitmap(cr,imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Camera is closed", Toast.LENGTH_SHORT).show();
            }
        } else
        if(requestCode == IMAGE_URL_CODE) {
            if(data !=null) {
                if (data.hasExtra("URL")) {
                    String url = data.getExtras().getString("URL");
                  //  Toast.makeText(imageActivity, "" + IMAGE_URL_CODE, Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(imageActivity, url, Toast.LENGTH_SHORT).show();
                    new DownLoadImageTask(imageView).execute(url);
                }
            }else
            {
                Toast.makeText(imageActivity, "Image not selected", Toast.LENGTH_SHORT).show();
            }

        }
        else {

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
               // Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>
    {

        ImageView imageView;
        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(getBaseContext());
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }*/

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlOfImage = params[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}



