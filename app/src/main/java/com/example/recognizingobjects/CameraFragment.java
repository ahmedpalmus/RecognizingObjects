/*
package com.example.recognizingobjects;

import android.graphics.Bitmap;

import org.jetbrains.annotations.Nullable;

import kotlin.Metadata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.core.CameraSelector.Builder;
import androidx.camera.core.ImageAnalysis.Analyzer;
import androidx.camera.core.ImageProxy.PlaneProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import com.google.common.util.concurrent.ListenableFuture;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tensorflow.lite.examples.objectdetection.ObjectDetectorHelper;
import org.tensorflow.lite.examples.objectdetection.OverlayView;
import org.tensorflow.lite.examples.objectdetection.ObjectDetectorHelper.DetectorListener;
import org.tensorflow.lite.examples.objectdetection.databinding.FragmentCameraBinding;
import org.tensorflow.lite.examples.objectdetection.fragments.PermissionsFragment.Companion;


// CameraFragment$bindCameraUseCases$$inlined$also$lambda$1.java

        import android.graphics.Bitmap;
        import android.graphics.Bitmap.Config;
        import androidx.camera.core.ImageProxy;
        import androidx.camera.core.ImageAnalysis.Analyzer;
        import kotlin.Metadata;
        import kotlin.jvm.internal.Intrinsics;
        import org.jetbrains.annotations.NotNull;

final class CameraFragment$bindCameraUseCases$$inlined$also$lambda$1 implements Analyzer {
    // $FF: synthetic field
    final CameraFragment this$0;

    CameraFragment$bindCameraUseCases$$inlined$also$lambda$1(CameraFragment var1) {
        this.this$0 = var1;
    }

    public final void analyze(@NotNull ImageProxy image) {
        Intrinsics.checkNotNullParameter(image, "image");
        if (CameraFragment.access$getBitmapBuffer$li(this.this$0) == null) {
            CameraFragment var10000 = this.this$0;
            Bitmap var10001 = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Config.ARGB_8888);
            Intrinsics.checkNotNullExpressionValue(var10001, "Bitmap.createBitmap(\n   …                        )");
            CameraFragment.access$setBitmapBuffer$p(var10000, var10001);
        }

        CameraFragment.access$detectObjects(this.this$0, image);
    }
}
// CameraFragment.java

@Metadata(
        mv = {1, 6, 0},
        k = 1,
        d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0019\u001a\u00020\u001aH\u0003J\u0010\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u001aH\u0002J\u0010\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!H\u0016J$\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010'2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\b\u0010*\u001a\u00020\u001aH\u0016J\u0010\u0010+\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u0005H\u0016J0\u0010-\u001a\u00020\u001a2\u000e\u0010.\u001a\n\u0012\u0004\u0012\u000200\u0018\u00010/2\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0016J\b\u00106\u001a\u00020\u001aH\u0016J\u001a\u00107\u001a\u00020\u001a2\u0006\u00108\u001a\u00020#2\b\u0010(\u001a\u0004\u0018\u00010)H\u0017J\b\u00109\u001a\u00020\u001aH\u0002J\b\u0010:\u001a\u00020\u001aH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u00020\u00078BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006;"},
        d2 = {"Lorg/tensorflow/lite/examples/objectdetection/fragments/CameraFragment;", "Landroidx/fragment/app/Fragment;", "Lorg/tensorflow/lite/examples/objectdetection/ObjectDetectorHelper$DetectorListener;", "()V", "TAG", "", "_fragmentCameraBinding", "Lorg/tensorflow/lite/examples/objectdetection/databinding/FragmentCameraBinding;", "bitmapBuffer", "Landroid/graphics/Bitmap;", "camera", "Landroidx/camera/core/Camera;", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "cameraProvider", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "fragmentCameraBinding", "getFragmentCameraBinding", "()Lorg/tensorflow/lite/examples/objectdetection/databinding/FragmentCameraBinding;", "imageAnalyzer", "Landroidx/camera/core/ImageAnalysis;", "objectDetectorHelper", "Lorg/tensorflow/lite/examples/objectdetection/ObjectDetectorHelper;", "preview", "Landroidx/camera/core/Preview;", "bindCameraUseCases", "", "detectObjects", "image", "Landroidx/camera/core/ImageProxy;", "initBottomSheetControls", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onError", "error", "onResults", "results", "", "Lorg/tensorflow/lite/task/vision/detector/Detection;", "inferenceTime", "", "imageHeight", "", "imageWidth", "onResume", "onViewCreated", "view", "setUpCamera", "updateControlsUi", "app_debug"}
)
public final class CameraFragment  {
    private final String TAG = "ObjectDetection";
    private ObjectDetectorHelper objectDetectorHelper;
    private Bitmap bitmapBuffer;
    private Preview preview;
    private ImageAnalysis imageAnalyzer;

    private final FragmentCameraBinding getFragmentCameraBinding() {
        FragmentCameraBinding var10000 = this._fragmentCameraBinding;
        Intrinsics.checkNotNull(var10000);
        return var10000;
    }

    public void onResume() {
        super.onResume();
        Companion var10000 = PermissionsFragment.Companion;
        Context var10001 = this.requireContext();
        Intrinsics.checkNotNullExpressionValue(var10001, "requireContext()");
        if (!var10000.hasPermissions(var10001)) {
            Navigation.findNavController((Activity) this.requireActivity(), 1000074).navigate(CameraFragmentDirections.actionCameraToPermissions());
        }

    }

    private final void detectObjects(ImageProxy image) {
        AutoCloseable var2 = (AutoCloseable) image;
        Throwable var3 = (Throwable) null;

        try {
            ImageProxy it = (ImageProxy) var2;
            int var5 = false;
            Bitmap var10000 = this.bitmapBuffer;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmapBuffer");
            }

            PlaneProxy var10001 = image.getPlanes()[0];
            Intrinsics.checkNotNullExpressionValue(var10001, "image.planes[0]");
            var10000.copyPixelsFromBuffer((Buffer) var10001.getBuffer());
            Unit var11 = Unit.INSTANCE;
        } catch (Throwable var8) {
            var3 = var8;
            throw var8;
        } finally {
            AutoCloseableKt.closeFinally(var2, var3);
        }

        ImageInfo var12 = image.getImageInfo();
        Intrinsics.checkNotNullExpressionValue(var12, "image.imageInfo");
        int imageRotation = var12.getRotationDegrees();
        ObjectDetectorHelper var13 = this.objectDetectorHelper;
        if (var13 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("objectDetectorHelper");
        }

        Bitmap var14 = this.bitmapBuffer;
        if (var14 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bitmapBuffer");
        }

        var13.detect(var14, imageRotation);
    }

    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        ImageAnalysis var10000 = this.imageAnalyzer;
        if (var10000 != null) {
            PreviewView var10001 = this.getFragmentCameraBinding().viewFinder;
            Intrinsics.checkNotNullExpressionValue(var10001, "fragmentCameraBinding.viewFinder");
            Display var2 = var10001.getDisplay();
            Intrinsics.checkNotNullExpressionValue(var2, "fragmentCameraBinding.viewFinder.display");
            var10000.setTargetRotation(var2.getRotation());
        }

    }

    public void onResults(@Nullable final List results, final long inferenceTime, final int imageHeight, final int imageWidth) {
        FragmentActivity var10000 = this.getActivity();
        if (var10000 != null) {
            var10000.runOnUiThread((Runnable) (new Runnable() {
                public final void run() {
                    TextView var10000 = CameraFragment.this.getFragmentCameraBinding().bottomSheetLayout.inferenceTimeVal;
                    Intrinsics.checkNotNullExpressionValue(var10000, "fragmentCameraBinding.bo…etLayout.inferenceTimeVal");
                    StringCompanionObject var1 = StringCompanionObject.INSTANCE;
                    String var2 = "%d ms";
                    Object[] var3 = new Object[]{inferenceTime};
                    String var4 = String.format(var2, Arrays.copyOf(var3, var3.length));
                    Intrinsics.checkNotNullExpressionValue(var4, "format(format, *args)");
                    var10000.setText((CharSequence) var4);
                    OverlayView var5 = CameraFragment.this.getFragmentCameraBinding().overlay;
                    List var10001 = results;
                    if (var10001 == null) {
                        var10001 = (List) (new LinkedList());
                    }

                    var5.setResults(var10001, imageHeight, imageWidth);
                    CameraFragment.this.getFragmentCameraBinding().overlay.invalidate();
                }
            }));
        }

    }

    public void onError(@NotNull final String error) {
        Intrinsics.checkNotNullParameter(error, "error");
        FragmentActivity var10000 = this.getActivity();
        if (var10000 != null) {
            var10000.runOnUiThread((Runnable) (new Runnable() {
                public final void run() {
                    Toast.makeText(CameraFragment.this.requireContext(), (CharSequence) error, 0).show();
                }
            }));
        }

    }
}
*/
