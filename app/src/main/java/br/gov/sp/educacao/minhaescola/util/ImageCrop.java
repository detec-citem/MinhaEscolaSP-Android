package br.gov.sp.educacao.minhaescola.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;

import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;

/**
 * Created by T. Rocha on 17/07/2018.
 */

public class ImageCrop {
    private static final String TAG = "ImageCrop";


    private static final boolean REMOVE_TEMP_FILE = true;
    private static final boolean DEBUG = false;

    public static final int PICK_FROM_CAMERA = 0;
    public static final int PICK_FROM_ALBUM = 1;
    public static final int PERMISSION_FROM_CAMERA = 3;

    private static final int PHOTO_SIZE = 640;
    private static final String ACTIVITY_NAME_PHOTOS = "com.google.android.apps.photos";
    private static final String ACTIVITY_NAME_PLUS = "com.google.android.apps.plus";

    private static boolean mUseActivityPhoto = false;
    private static boolean mUseActivityPlus = false;

    private static Uri mImageCaptureUri;
    private static Bitmap mCropBitmap;
    private static String mTempImagePath;

    private static int mLastAction = PICK_FROM_CAMERA;

    private static final String CAMERA_TEMP_PREFIX = "camera_";
    private static final String CROP_TEMP_PREFIX = "crop_";
    private static final String IMAGE_EXT = ".png";

    public static void checkPackages(Activity context, Intent intentPhoto) {
        final PackageManager pm = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        mUseActivityPhoto = false;
        mUseActivityPlus = false;

        final List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);

        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(ACTIVITY_NAME_PHOTOS)) {

                final List<ResolveInfo> photoInfos = pm.queryIntentActivities(intentPhoto, PackageManager.MATCH_ALL);
                for (ResolveInfo photoInfo : photoInfos) {
                    if (photoInfo.activityInfo.packageName.equals(ACTIVITY_NAME_PHOTOS)) {
                        Log.d(TAG, "mUseActivityPhoto TRUE");
                        mUseActivityPhoto = true;
                        break;
                    }
                }

            } else if (info.activityInfo.packageName.equals(ACTIVITY_NAME_PLUS)) {

                final List<ResolveInfo> photoInfos = pm.queryIntentActivities(intentPhoto, PackageManager.MATCH_ALL);
                for (ResolveInfo photoInfo : photoInfos) {
                    if (photoInfo.activityInfo.packageName.equals(ACTIVITY_NAME_PLUS)) {
                        Log.d(TAG, "mUseActivityPlus TRUE");
                        mUseActivityPlus = true;
                        break;
                    }
                }
            }
        }
    }

    public static void takeCameraAction(Activity context) {

        ImageCrop.doTakeCameraAction(context);
    }

    public static void takeAlbumAction(Activity context) {

        ImageCrop.doTakeAlbumAction(context);
    }

    private static void doTakeCameraAction(Activity context) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File file = new File(Environment.getExternalStorageDirectory(), imageFileName);

        mTempImagePath = file.getAbsolutePath();
        mImageCaptureUri = Uri.fromFile(file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        context.startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private static void doTakeAlbumAction(Activity context) {
        if (DEBUG)
            Log.d(TAG, "doTakeAlbumAction");

        final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        checkPackages(context, intent);

        if (mUseActivityPhoto) {

            if (DEBUG)
                Log.d(TAG, "doTakeAlbumAction setPackage ACTIVITY_NAME_PHOTOS");
            intent.setPackage(ACTIVITY_NAME_PHOTOS);
        }
        else if (mUseActivityPlus) {

            if (DEBUG)
                Log.d(TAG, "doTakeAlbumAction setPackage ACTIVITY_NAME_PLUS");
            intent.setPackage(ACTIVITY_NAME_PLUS);
        }

        context.startActivityForResult(intent, ImageCrop.PICK_FROM_ALBUM);
    }

    private static void removeTempFile() {

        if (mImageCaptureUri != null) {
            final String capturePath = mImageCaptureUri.getPath();
            if (capturePath != null) {
                Log.w(TAG, "removeTempFile capturePath=" + capturePath);

                final File captureFile = new File(capturePath);
                if (captureFile != null) {
                    if (captureFile.getAbsoluteFile().exists()) {
                        captureFile.delete();
                    }
                }
            }
        }


        if (mTempImagePath != null) {
            Log.w(TAG, "removeTempFile mTempImagePath=" + mTempImagePath);

            final File tempFile = new File(mTempImagePath);
            if (tempFile != null) {
                if (tempFile.getAbsoluteFile().exists()) {
                    tempFile.delete();
                }
            }
        }
    }

    private static void removeDataFile(Intent data) {
        if (data == null) {
            Log.w(TAG, "removeDataFile data == null");
            return;
        }
        if (data.getData() == null) {
            Log.w(TAG, "removeDataFile data.getData() == null");
            return;
        }

        final String dataPath = data.getData().getPath();
        if (dataPath == null) {
            Log.w(TAG, "removeDataFile dataPath == null");
            return;
        }
        Log.w(TAG, "removeDataFile dataPath=" + dataPath);

        final File dataFile = new File(dataPath);
        if (dataFile == null) {
            Log.w(TAG, "removeDataFile dataFile == null");
            return;
        }

        if (dataFile.getAbsoluteFile().exists()) {
            dataFile.delete();
        }
    }

    private static File cropFileFromPhotoData(Activity context, Intent data) {
        if (DEBUG)
            Log.d(TAG, "cropFileFromPhotoData");

        if (data.getData() == null) {

            if(IS_DEBUG){

                Log.e(TAG, "cropFileFromPhotoData data.getData() == null");
            }
            return null;
        }

        final String dataPath = data.getData().getPath();
        if (dataPath == null) {
            if(IS_DEBUG){

                Log.e(TAG, "cropFileFromPhotoData dataPath == null");
            }

            return null;
        }

        File dataFile = null;

        if (dataPath.startsWith("/external")) {
            final Uri dataUri = Uri.parse("content://media" + dataPath);
            final String dataFilePath = getRealPathFromURI(context, dataUri);
            dataFile = new File(dataFilePath);
            boolean exist = dataFile.exists();
            long length = dataFile.length();
            if (DEBUG)
                Log.d(TAG, "cropFileFromPhotoData dataFilePath=" + dataFilePath + " exist=" + exist + " length=" + length);
        } else {
            dataFile = new File(dataPath);
            boolean exist = dataFile.exists();
            long length = dataFile.length();
            if (DEBUG)
                Log.d(TAG, "cropFileFromPhotoData dataPath=" + dataPath + " exist=" + exist + " length=" + length);
        }

        return dataFile;
    }

    private static File cropFileFromPhotoExtra(Activity context, Intent data) {
        if (DEBUG)
            Log.d(TAG, "cropFileFromPhotoExtra");

        final Bundle extras = data.getExtras();
        if (extras == null) {
            if (DEBUG)
                Log.d(TAG, "cropFileFromPhotoExtra extra == null");
            return null;
        }

        mCropBitmap = extras.getParcelable("data");

        File dataFile = null;
        final String dataBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CROP_TEMP_PREFIX + String.valueOf(System.currentTimeMillis()) + IMAGE_EXT;
        if (dataBitmapPath != null) {
            dataFile = new File(dataBitmapPath);
        }
        try {
            final FileOutputStream out = new FileOutputStream(dataFile);
            mCropBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return dataFile;
    }

    public static File cropFileFromPhoto(Activity context, Intent data) {

        if (data == null) {
            if(IS_DEBUG){

                Log.e(TAG, "cropFileFromPhoto data == null");
            }

            removeTempFile();
            return null;
        }

        File dataFile = null;

        dataFile = cropFileFromPhotoData(context, data);
        if (dataFile == null) {
            dataFile = cropFileFromPhotoExtra(context, data);
        }

        if (REMOVE_TEMP_FILE)
            removeTempFile();
        return dataFile;
    }

    public static Bitmap cropBitmapFromPhoto(Activity context, Intent data) {

        if (data == null) {
            if(IS_DEBUG){

                Log.e(TAG, "cropBitmapFromPhoto data == null");
            }

            return null;
        }

        final Bundle extras = data.getExtras();
        if (extras != null) {
            if (DEBUG)
                Log.d(TAG, "cropBitmapFromPhoto extra");
            mCropBitmap = extras.getParcelable("data");
        } else {
            if (DEBUG)
                Log.d(TAG, "cropBitmapFromPhoto extra == null");
            mCropBitmap = BitmapFactory.decodeFile(data.getData().getPath());
        }

        if (REMOVE_TEMP_FILE)
            removeTempFile();
        removeDataFile(data);
        return mCropBitmap;
    }

    public static void pickFromCamera(Activity context, Intent data) {
        if (DEBUG)
            Log.d(TAG, "pickFromCamera => launchCropActivity");

        if (mTempImagePath == null || mTempImagePath.isEmpty()) {
            Log.e(TAG, "pickFromCamera mTempImagePath error");
            return;
        }

        launchCropActivity(context, mTempImagePath);
    }

    public static void pickFromCamera2(Activity context, Uri imagePath) {

        launchCropActivity2(context, imagePath);
    }

    public static void pickFromAlbum(Activity context, Intent data) {
        if (data == null) {
            Log.e(TAG, "pickFromAlbum data == null");
            return;
        }

        mImageCaptureUri = data.getData();

        launchCropActivity(context, mImageCaptureUri);
    }

    private static void launchCropActivity(Activity context, Uri srcUri) {

        final File out = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
        final Uri destUri = Uri.fromFile(out);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        options.setToolbarTitle("Editar foto");
        options.setToolbarColor(ContextCompat.getColor(context, R.color.azul_carteirinha));
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.azul_carteirinha));
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.azul_carteirinha));
        options.setRootViewBackgroundColor(ContextCompat.getColor(context, R.color.azul_carteirinha));
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorWhite));
        options.setLogoColor(ContextCompat.getColor(context, R.color.azul_carteirinha));

        UCrop.of(srcUri, destUri)
                .withAspectRatio(3, 4)
                .withMaxResultSize(PHOTO_SIZE, PHOTO_SIZE)
                .withOptions(options)
                .start(context);
    }

    public static void launchCropActivity(Activity context, File in) {

        if (!in.exists()) {

            Log.e(TAG, "launchCropActivity !in.exists()");
            return;
        }

        final Uri srcUri = Uri.fromFile(in);
        launchCropActivity(context, srcUri);
    }

    public static void pickFromAlbum2(Activity context, Uri filepath) {

        mImageCaptureUri = filepath;

        launchCropActivity(context, mImageCaptureUri);
    }

    private static void launchCropActivity(Activity context, String path) {

        final File in = new File(path);
        launchCropActivity(context, in);
    }

    public static void launchCropActivity2(Activity context, Uri path) {

        launchCropActivity(context, path);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {

        Cursor cursor = null;
        final String[] proj = {MediaStore.Images.Media.DATA};
        String ret = null;

        try {

            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            ret = cursor.getString(column_index);
        }
        catch (Exception e) {

            e.printStackTrace();
            Log.e(TAG, "getRealPathFromURI exception");
            return null;
        }

        if (cursor != null) {

            cursor.close();
        }

        return ret;
    }

    private static boolean checkPermissions(Activity context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {

                context.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, PERMISSION_FROM_CAMERA);
                return false;
            }
        }
        return true;
    }

    public static void onRequestPermissionsResult(Activity context, int requestCode, String[] permissions, int[] grantResults) {
        if (DEBUG)
            Log.d(TAG, "onRequestPermissionsResult requestCode=" + requestCode);

        final String read = Manifest.permission.READ_EXTERNAL_STORAGE;
        final String write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        final String camera = Manifest.permission.CAMERA;

        boolean readEnable = false;
        boolean writeEnable = false;
        boolean cameraEnable = false;

        for (int i = 0; i < permissions.length; i++) {
            if (DEBUG)
                Log.d(TAG, "onRequestPermissionsResult permissions=" + permissions[i] + " grantResults=" + grantResults[i]);

            if (read.equals(permissions[i]) && grantResults[i] == 0)
                readEnable = true;
            if (write.equals(permissions[i]) && grantResults[i] == 0)
                writeEnable = true;
            if (camera.equals(permissions[i]) && grantResults[i] == 0)
                cameraEnable = true;
        }

        if(readEnable && writeEnable && cameraEnable){

            switch (mLastAction) {

                case ImageCrop.PICK_FROM_CAMERA:
                    if (DEBUG)
                        Log.d(TAG, "doTakeCameraAction");
                    ImageCrop.doTakeCameraAction(context);
                    break;

                case ImageCrop.PICK_FROM_ALBUM:
                    if (DEBUG)
                        Log.d(TAG, "doTakeAlbumAction");
                    ImageCrop.doTakeAlbumAction(context);
                    break;
            }
        }
        else {
            if (!readEnable)
                Log.e(TAG, "READ_EXTERNAL_STORAGE not found");
            if (!writeEnable)
                Log.e(TAG, "WRITE_EXTERNAL_STORAGE not found");
            if (!cameraEnable)
                Log.e(TAG, "CAMERA not found");
        }
    }
}