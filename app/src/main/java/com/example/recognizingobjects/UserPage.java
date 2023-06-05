package com.example.recognizingobjects;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.recognizingobjects.ObjectDetectorHelper.DetectorListener;

import org.tensorflow.lite.task.vision.detector.Detection;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserPage extends AppCompatActivity implements DetectorListener {
    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.3f;
    public static final int TF_OD_API_INPUT_SIZE = 640;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private static final boolean TF_OD_API_IS_QUANTIZED = true;
    private static final String TF_OD_API_MODEL_FILE = "efficientdet-lite2.tflite";
    private static final String TF_OD_API_LABELS_FILE = "coco.txt";
    // Minimum detection confidence to track a detection.
    private static final boolean MAINTAIN_ASPECT = true;
    protected int previewWidth = 0;
    protected int previewHeight = 0;
    List<Detection> res;
    ImageButton start_camera;
    String[] cocoClasses = {"person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light", "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee", "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard", "tennis racket", "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse", "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase", "scissors", "teddy bear", "hair drier", "toothbrush"};
    Button logout;
    TextView object1, object2;
    LinearLayout lin1, lin2;
    ImageButton voice1, voice2, vid1, vid2;
    String video1, video2, item1="", item2="";
    TextToSpeech t1;
    String currentPhotoPath = "";
    private ObjectDetectorHelper objectDetectorHelper;
    private ObjectDetectorHelper.DetectorListener detectorListener;
    private ImageView imageView;
    private Bitmap imageBitmap;
    private Integer sensorOrientation = 90;
    private Classifier detector;
    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private Bitmap sourceBitmap;
    private Bitmap cropBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        imageView = findViewById(R.id.image_view);
        start_camera = findViewById(R.id.start_camera);
        object1 = findViewById(R.id.item1);
        object2 = findViewById(R.id.item2);
        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);
        voice1 = findViewById(R.id.voice1);
        voice2 = findViewById(R.id.voice2);
        vid1 = findViewById(R.id.vid1);
        vid2 = findViewById(R.id.vid2);

        objectDetectorHelper = new ObjectDetectorHelper(0.5f, 2, 2, 0, 0, getApplicationContext(), detectorListener);

        vid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, VideoPage.class);
                intent.putExtra("url", item1.toLowerCase(Locale.ROOT));
                startActivity(intent);
            }
        });
        vid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, VideoPage.class);
                intent.putExtra("url", item2.toLowerCase(Locale.ROOT));
                startActivity(intent);
            }
        });
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();

                }
            }
        });

        voice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(item1, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        voice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(item2, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        logout = findViewById(R.id.exit);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(UserPage.this, MainActivity.class);
                startActivity(x);
                finish();
            }
        });

        start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        initBox();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle file creation error
            }

            // Continue only if the file was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.recognizingobjects.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file path for use with ACTION_IMAGE_CAPTURE intent
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
/*            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(cropBitmap);*/
            Bitmap capturedBitmap = loadImageFromFile(currentPhotoPath);
            this.cropBitmap = Utils.processBitmap(capturedBitmap, TF_OD_API_INPUT_SIZE);
            imageView.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
            start_camera.setEnabled(false);

            objectDetectorHelper.detect(cropBitmap, 0);

            res = (List<Detection>) objectDetectorHelper.results;
            if (res.size() > 0) {
                if (res.size() == 1) {
                    item1 = res.get(0).getCategories().get(0).getLabel();
                    object1.setText(item1);
                    lin1.setVisibility(View.VISIBLE);

                    t1.speak(item1, TextToSpeech.QUEUE_FLUSH, null);

                } else if (res.size() == 2) {
                    item1 = res.get(0).getCategories().get(0).getLabel();
                    object1.setText(item1);
                    lin1.setVisibility(View.VISIBLE);


                    item2 = res.get(1).getCategories().get(0).getLabel();
                    if(!item1.equals(item2)) {
                        object2.setText(item2);
                        lin2.setVisibility(View.VISIBLE);
                        t1.speak(item1+" and "+item2, TextToSpeech.QUEUE_FLUSH, null);
                    }else{
                        lin2.setVisibility(View.GONE);
                    }
                }
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.GONE);
                Toast.makeText(UserPage.this, "Try a gain with a new Image", Toast.LENGTH_LONG).show();
            }
            start_camera.setEnabled(true);

           /* Handler handler = new Handler();

            new Thread(() -> {

                //final List<Classifier.Recognition> results = detector.recognizeImage(cropBitmap);
                handler.post(new Runnable() {
                    @Override
                    public void run() {


      *//*              for (int i = 0; i < results.size(); i++) {
                            System.out.println(cocoClasses[results.get(i).getDetectedClass()]);

                            //res.setText(cocoClasses[results.get(i).getDetectedClass()]);
                            System.out.println(results.get(i).getDetectedClass());

                        }
                        lin1.setVisibility(View.VISIBLE);

                        video1="files/chair.mp4";*//*

                    }
                });
            }).start();*/
        }
    }

    private Bitmap loadImageFromFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888; // Set the bitmap configuration

        return BitmapFactory.decodeFile(filePath, options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with camera initialization
        } else {
            Toast.makeText(UserPage.this, "The permission is required", Toast.LENGTH_LONG).show();
            // Permission denied, show a message or take appropriate action
        }
    }

    private void initBox() {
        previewHeight = TF_OD_API_INPUT_SIZE;
        previewWidth = TF_OD_API_INPUT_SIZE;
        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        try {
            detector =
                    YoloV5Classifier.create(
                            getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_IS_QUANTIZED,
                            TF_OD_API_INPUT_SIZE);
        } catch (final IOException e) {
            e.printStackTrace();
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onError(@NonNull String error) {

    }

    @Override
    public void onResults(@Nullable List<Detection> results, long inferenceTime, int imageHeight, int imageWidth) {
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");


    }
}