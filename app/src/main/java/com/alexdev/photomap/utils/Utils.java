package com.alexdev.photomap.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.alexdev.photomap.R;
import com.alexdev.photomap.utils.exceptions.DirectoryCreationNotPermittedException;
import com.alexdev.photomap.utils.exceptions.ThisDrawableNotSupportedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public final class Utils {

    public static final String FILE_NAME_FORMAT = "%d.jpg";
    public static final String TEMPORARY_FILE_NAME = "temporary_file.jpg";

    private Utils() {

    }

    @NonNull
    public static File saveDrawableToFile(Context context, Drawable drawable, boolean isTemporaryFile)
            throws IOException, DirectoryCreationNotPermittedException, ThisDrawableNotSupportedException {
        if (!(drawable instanceof BitmapDrawable)) throw new ThisDrawableNotSupportedException();

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        File storageDirectory = Environment.getExternalStorageDirectory();
        File appDirectory = new File(storageDirectory.getAbsolutePath() + File.separator +
                context.getString(R.string.app_name));
        if (!appDirectory.exists()) {
            if (!appDirectory.mkdirs()) {
                throw new DirectoryCreationNotPermittedException();
            }
        }

        String fileName = isTemporaryFile ?
                TEMPORARY_FILE_NAME :
                String.format(Locale.getDefault(), FILE_NAME_FORMAT, System.currentTimeMillis());

        File outFile = new File(appDirectory, fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    context.getResources().getInteger(R.integer.picture_downloading_quality),
                    fileOutputStream
            );
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new IOException(e);
        }

        return outFile;
    }

    @MainThread
    public static boolean checkAndRequestPermissions(@NonNull Fragment fragment,
                                                     int permissionRequestCode,
                                                     @NonNull String... permissions) {
        boolean isPermissionsGranted = true;
        for (String permission : permissions) {
            isPermissionsGranted = isPermissionsGranted
                    && (ActivityCompat.checkSelfPermission(fragment.getContext(), permission)
                    == PackageManager.PERMISSION_GRANTED);
        }

        if (!isPermissionsGranted) {
            fragment.requestPermissions(permissions, permissionRequestCode);
        }

        return isPermissionsGranted;
    }

    @MainThread
    public static boolean checkAndRequestPermissions(@NonNull Activity activity,
                                                     int permissionRequestCode,
                                                     @NonNull String... permissions) {
        boolean isPermissionsGranted = true;
        for (String permission : permissions) {
            isPermissionsGranted = isPermissionsGranted
                    && (ActivityCompat.checkSelfPermission(activity, permission)
                    == PackageManager.PERMISSION_GRANTED);
        }

        if (!isPermissionsGranted) {
            ActivityCompat.requestPermissions(activity, permissions, permissionRequestCode);
        }

        return isPermissionsGranted;
    }

}
