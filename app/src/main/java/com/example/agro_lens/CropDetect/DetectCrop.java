package com.example.agro_lens.CropDetect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agro_lens.Dashboard;
import com.example.agro_lens.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class DetectCrop extends AppCompatActivity {
    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageBuffer;
    private  int imageSizeX;
    private  int imageSizeY;
    private TensorBuffer outputProbabilityBuffer;
    private TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap;
    private List<String> labels;
    ImageView imageView;
    Uri imageuri;
    Button buclassify;
    TextView classitext;
    TextView Crop,accuracy;
    Button next;
    BottomSheetDialog bottomSheetDialog;
    String cropName;
    String accuracyText;
    String one,two,three;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_crop);
        imageView=(ImageView)findViewById(R.id.detect_Image);
        buclassify=(Button)findViewById(R.id.detect_but);
        classitext=(TextView)findViewById(R.id.crop_result);
        bottomSheetDialog=new BottomSheetDialog(DetectCrop.this);
        bottomSheetDialog.setContentView(R.layout.detect_bottom);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        Crop=bottomSheetDialog.findViewById(R.id.croptext);

        accuracy=bottomSheetDialog.findViewById(R.id.accuracytext);
        next=bottomSheetDialog.findViewById(R.id.nextbut);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(DetectCrop.this,DetectResult.class);
                intent.putExtra("crop",cropName);
                // ByteArrayOutputStream bs = new ByteArrayOutputStream();
                // bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
                //  byte[] byteArray = bs.toByteArray();
                //   intent.putExtra("bitmap",byteArray);
                intent.putExtra("imageUri", imageuri.toString());
                startActivity(intent);


            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog4=new BottomSheetDialog(DetectCrop.this,R.style.BottomSheetDialog);
                bottomSheetDialog4.setContentView(R.layout.bottom_sheet_dialog);
                CardView camera=bottomSheetDialog4.findViewById(R.id.cardcamera);
                CardView gallery=bottomSheetDialog4.findViewById(R.id.cardgallery);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.android.tflitecamerademo");
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                            bottomSheetDialog4.dismiss();
                        }
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),12);
                        bottomSheetDialog4.dismiss();

                    }
                });
                bottomSheetDialog4.setCanceledOnTouchOutside(true);
                bottomSheetDialog4.show();

            }
        });

        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }

        buclassify.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (imageuri!=null){
                    int imageTensorIndex = 0;
                    int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
                    imageSizeY = imageShape[1];
                    imageSizeX = imageShape[2];
                    DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

                    int probabilityTensorIndex = 0;
                    int[] probabilityShape =
                            tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
                    DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

                    inputImageBuffer = new TensorImage(imageDataType);
                    outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
                    probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                    inputImageBuffer = loadImage(bitmap);

                    tflite.run(inputImageBuffer.getBuffer(),outputProbabilityBuffer.getBuffer().rewind());
                    showresult();
                }
                else {
                    final BottomSheetDialog bottomSheetDialog4=new BottomSheetDialog(DetectCrop.this,R.style.BottomSheetDialog);
                    bottomSheetDialog4.setContentView(R.layout.bottom_sheet_dialog);
                    CardView camera=bottomSheetDialog4.findViewById(R.id.cardcamera);
                    CardView gallery=bottomSheetDialog4.findViewById(R.id.cardgallery);
                    camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.android.tflitecamerademo");
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                                bottomSheetDialog4.dismiss();
                            }
                        }
                    });
                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"),12);
                            bottomSheetDialog4.dismiss();

                        }
                    });
                    bottomSheetDialog4.setCanceledOnTouchOutside(true);
                    bottomSheetDialog4.show();
                }


            }
        });



    }

    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("model.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showresult(){

        try{
            labels = FileUtil.loadLabels(this,"dict.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));


        float probabilityVal1 = maxValueInMap * ((float) 1);
        String name1 = String.format("%.2f",probabilityVal1);

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                cropName=entry.getKey();
                one=cropName+" - "+name1+"%";
                float temp;
                temp = labeledProbability.replace(cropName, (float) 0);
            }
        }

        float maxValueInMap1 =(Collections.max(labeledProbability.values()));
        String beta ;
        float probabilityVal2 = maxValueInMap1 * ((float) 1);
        String name2 = String.format("%.2f",probabilityVal2);
        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue() == maxValueInMap1) {
                beta = entry.getKey();
                two=beta+" - "+name2+"%";
                float anothertemp;
                anothertemp= labeledProbability.replace(beta , (float) 0);
            }
        }

        float maxValueInMap2 =(Collections.max(labeledProbability.values()));
        float probabilityVal3 = maxValueInMap2 * ((float)  1);
        String name3 = String.format("%.2f",probabilityVal3);
        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue() == maxValueInMap2) {
                three=entry.getKey()+" - "+name3+"%";
            }
        }
        Crop.setText(cropName);

        accuracyText=one+"\n"+two+"\n"+three;
        accuracy.setText(accuracyText);
        bottomSheetDialog.show();

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetectCrop.this, Dashboard.class));
        finish();
    }
}