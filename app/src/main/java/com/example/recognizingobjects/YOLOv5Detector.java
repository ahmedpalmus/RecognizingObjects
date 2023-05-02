package com.example.recognizingobjects;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.TensorFlowLite;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YOLOv5Detector {
    private Context context;
    private static final int NUM_CLASSES = 80;
    private static final int INPUT_SIZE = 640;
    private static final int CHANNELS = 3;
    Interpreter.Options options;
    private static final int BATCH_SIZE = 1;
    private static final int NUM_DETECTIONS = 100;
    private static final float CONFIDENCE_THRESHOLD = 0.5f;
    Bitmap imageBitmap;

    private static final String MODEL_FILE_NAME = "yolov5s-fp16.tflite";
    private static final String[] LABELS = new String[]{
            "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light",
            "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow",
            "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee",
            "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard",
            "tennis racket", "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple",
            "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch",
            "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse", "remote", "keyboard", "cell phone",
            "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase", "scissors", "teddy bear",
            "hair drier", "toothbrush"
    };

    private final Interpreter interpreter;

    public YOLOv5Detector(Context context,Bitmap bitmap) throws IOException {
        this.context = context;
        imageBitmap=bitmap;
        options = new Interpreter.Options();
        interpreter = new Interpreter(loadModelFile(), options);

    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("yolov5s-fp16.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public List<Recognition> detectObjects(Bitmap bitmap) {

        // Create input tensor
        final int[] inputShape = interpreter.getInputTensor(0).shape();
        final DataType inputDataType = interpreter.getInputTensor(0).dataType();
        final ByteBuffer inputTensorBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * INPUT_SIZE * INPUT_SIZE * CHANNELS * inputDataType.byteSize());


// Preprocess input image

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        for (int i = 0; i < intValues.length; ++i) {
            inputTensorBuffer.putFloat(((intValues[i] >> 16) & 0xFF) / 255.0f);
            inputTensorBuffer.putFloat(((intValues[i] >> 8) & 0xFF) / 255.0f);
            inputTensorBuffer.putFloat((intValues[i] & 0xFF) / 255.0f);
        }
        final TensorBuffer inputTensor = TensorBuffer.createFixedSize(inputShape, inputDataType);
        inputTensor.loadBuffer(inputTensorBuffer);
        // Run inference
        final float[][][] outputLocations = new float[1][NUM_DETECTIONS][4];
        final float[][] outputClasses = new float[1][NUM_DETECTIONS];
        final float[][] outputScores = new float[1][NUM_DETECTIONS];
        final float[][] outputNumDetections = new float[1][1];
        Map<Integer, Object> outputMap = new HashMap<>();
        Object[] inputArray = {inputTensorBuffer};
        System.out.println("here");

        interpreter.runForMultipleInputsOutputs(inputArray, outputMap);
        System.out.println("here2");

        System.out.println(outputMap.toString());
        System.out.println("here3");

        // Postprocess output
        final List<Recognition> detections = new ArrayList<>();
        for (int i = 0; i < NUM_DETECTIONS; ++i) {
            final float confidence = outputScores[0][i];
            if (confidence < CONFIDENCE_THRESHOLD) {
                break;
            }

            final int classIndex = (int) outputClasses[0][i];
            final float[] location = outputLocations[0][i];
            final float ymin = Math.max(0, location[0]);
            final float xmin = Math.max(0, location[1]);
            final float ymax = Math.min(1, location[2]);
            final float xmax = Math.min(1, location[3]);
            final float width = xmax - xmin;
            final float height = ymax - ymin;

            final String label = LABELS[classIndex];

            detections.add(new Recognition(label, confidence, ymin, xmin, ymax, xmax));
        }

        // Sort detections by confidence in descending order
        detections.sort(new Comparator<Recognition>() {
            @Override
            public int compare(final Recognition o1, final Recognition o2) {
                return Float.compare(o2.getConfidence(), o1.getConfidence());
            }
        });

        return detections;
    }

    public static class Recognition {
        private final String label;
        private final float confidence;
        private final float ymin;
        private final float xmin;
        private final float ymax;
        private final float xmax;

        public Recognition(final String label, final float confidence, final float ymin, final float xmin, final float ymax, final float xmax) {
            this.label = label;
            this.confidence = confidence;
            this.ymin = ymin;
            this.xmin = xmin;
            this.ymax = ymax;
            this.xmax = xmax;
        }


        public String getLabel() {
            return label;
        }

        public float getConfidence() {
            return confidence;
        }

        public float getYmin() {
            return ymin;
        }

        public float getXmin() {
            return xmin;
        }

        public float getYmax() {
            return ymax;
        }

        public float getXmax() {
            return xmax;
        }

        @Override
        public String toString() {
            return "Recognition{" +
                    "label='" + label + '\'' +
                    ", confidence=" + confidence +
                    ", ymin=" + ymin +
                    ", xmin=" + xmin +
                    ", ymax=" + ymax +
                    ", xmax=" + xmax +
                    '}';
        }
    }
}
