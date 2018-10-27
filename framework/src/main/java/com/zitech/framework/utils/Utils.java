package com.zitech.framework.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.zitech.framework.BaseApplication;
import com.zitech.framework.R;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 15;
    private static final String TEMP_IMAGE_NAME = "_temp.jpg";

    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }
    public static int getColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public static void takePhoto(Activity activity, int requestCode) {
        takePhoto(activity,requestCode,getCameraImagePath());
    }

    public static void takePhoto(Activity activity, int requestCode,File imagePath) {
        if (Utils.checkSdCardAvailable()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imagePath.exists())
                imagePath.delete();
            Uri outputFileUri = Uri.fromFile(imagePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intent, requestCode);
            }
        } else {
            ToastMaster.shortToast("请插入SD卡");
        }
    }
    public static File getCameraImagePath() {
        return new File(BaseApplication.getInstance().getUploadCacheDir(), TEMP_IMAGE_NAME);
    }
    public static void takePhoto(Fragment fragment, int requestCode) {
        takePhoto(fragment,requestCode,getCameraImagePath());
    }
    public static void takePhoto(Fragment fragment, int requestCode,File imagePath) {
        if (Utils.checkSdCardAvailable()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imagePath.exists())
                imagePath.delete();
            Uri outputFileUri = Uri.fromFile(imagePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
                fragment.startActivityForResult(intent, requestCode);
            }
        } else {
            ToastMaster.shortToast("请插入SD卡");
        }
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    @Deprecated
    public static File getDiskCachePath(String uniqueName) {

        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        final String cachePath = Utils.isExternalStorageMounted() ? Utils.getExternalCacheDir().getPath() : BaseApplication
                .getInstance().getCacheDir().getPath();
        if (uniqueName != null) {
            return new File(cachePath + File.separator + uniqueName);
        } else {
            return new File(cachePath);
        }
    }

    public static File getExistCacheDir(Context context, String uniqueName) {
        if (Utils.isExternalStorageMounted()) {
            File dir = new File(Utils.getExternalCacheDir(context), uniqueName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dir.exists() && dir.canWrite() && dir.canRead()) {
                return dir;
            }
        }

        File dir = new File(BaseApplication
                .getInstance().getCacheDir() + File.separator + uniqueName);
        dir.mkdirs();
        return dir;
    }

    public static File getExistCacheDir(String uniqueName) {
        return getExistCacheDir(BaseApplication.getInstance(), uniqueName);
    }


    public String compressNoLargePhoto(Context context, File src) {
        try {
            return Luban.with(context).load(src).get().getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return src.getPath();
        }
    }

    /*
     * 外部存储设备是否就绪
     */
    public static boolean isExternalStorageMounted() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.getExternalStorageDirectory().canWrite()) {
            // No SD card found.
            return false;
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @return The external cache dir
     */

    @SuppressLint("NewApi")
    public static File getExternalCacheDir() {
        Context context = BaseApplication.getInstance();
        return getExternalCacheDir(context);
    }

    @SuppressLint("NewApi")
    public static File getExternalCacheDir(Context context) {
        if (VersionUtils.hasFroyo()) {
            if (context != null && context.getExternalCacheDir() != null) {
                return context.getExternalCacheDir();
            }

        }
        // Before Froyo we need to construct the external cache dir ourselves

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";

        File file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static String getExternalStoragePath() {
        // 获取SdCard状态
        String state = Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.getExternalStorageDirectory().canWrite()) {
                return Environment.getExternalStorageDirectory().getPath();
            }
        }
        return null;
    }

    public static boolean checkSdCardAvailable() {

        String state = Environment.getExternalStorageState();
        File sdCardDir = Environment.getExternalStorageDirectory();
        if (Environment.MEDIA_MOUNTED.equals(state) && sdCardDir.canWrite()) {
            if (getAvailableStore(sdCardDir.toString()) > LOW_STORAGE_THRESHOLD) {
                try {
                    // File f = new
                    // File(Environment.getExternalStorageDirectory(), "_temp");
                    // f.createNewFile();
                    // f.delete();
                    return true;
                } catch (Exception e) {
                }
            }
        }
        return false;

    }

    /**
     * 获取存储卡的剩余容量，单位为字节
     *
     * @param filePath
     * @return availableSpare
     */

    private static long getAvailableStore(String filePath) {
        // 取得sdcard文件路径
        StatFs statFs = new StatFs(filePath);
        // 获取block的SIZE
        long blocSize = statFs.getBlockSize();
        // 获取BLOCK数量
        long totalBlocks = statFs.getBlockCount();
        // 可使用的Block的数量
        long availaBlock = statFs.getAvailableBlocks();
        long availableSpare = availaBlock * blocSize;
        return availableSpare;

    }

    public static String getPathFromUri(Context context, Uri uri) {
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            realPath = getRealPathFromURI_BelowAPI11(context, uri);
        }

        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            realPath = getRealPathFromURI_API11to18(context, uri);
        }

        // SDK > 19 (Android 4.4)
        else {
            realPath = getRealPathFromURI_API19(context, uri);
        }

        return realPath;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }

        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }


        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void openFile(Context context, File path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
        } else {
            intent.setDataAndType(Uri.fromFile(path), getMIMEType(path));
        }
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    public static String getPachageName(String fileName) {
        PackageManager pm = BaseApplication.getInstance().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(fileName, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName;  //得到安装包名称
            String version = info.versionName;       //得到版本信息
            //Toast.makeText(test4.this, "packageName:"+packageName+";version:"+version, Toast.LENGTH_LONG).show();
            return packageName;
        }
        return null;
    }

    public static Uri getUriFromResource(Context context, int resId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resId) + "/"
                + context.getResources().getResourceTypeName(resId) + "/"
                + context.getResources().getResourceEntryName(resId));
    }

    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }


    private final static String PATTERN = "yyyyMMddHHmmss";    // 时间戳命名

    public static File newTempFile() {
        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());
        return new File(BaseApplication.getInstance().getUploadCacheDir(), timeStamp + ".jpg");
    }

    public interface Callback {
        void callBack(boolean exist);
    }

    public static void isAppInstalled(final Context context, final String packageName, final Callback callback) {

        new AsyncTask<Void, Void, Void>() {
            private boolean result = false;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
                    List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
                    if (pinfo != null) {
                        for (int i = 0; i < pinfo.size(); i++) {
                            String pn = pinfo.get(i).packageName;
                            if (packageName.equals(pn)) {
                                result = true;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void r) {
                callback.callBack(result);
            }
        }.execute();

    }

    public static String md5(String... args) {
        StringBuilder buffer = new StringBuilder();
        for (String arg : args) {
            buffer.append(arg);
        }
        return getMD5(buffer.toString());

    }

    /**
     * 生成md5
     *
     * @param message
     * @return
     */
    public static String getMD5(String message) {
        String md5str = "";
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2 将消息变成byte数组
            byte[] input = message.getBytes();

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toLowerCase();
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法描述：判断某一Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service的全路径： 包名 + service的类名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
