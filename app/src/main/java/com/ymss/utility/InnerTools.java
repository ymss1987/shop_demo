package com.ymss.utility;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ymss on 2016/5/10.
 */
public class InnerTools {
    public static final int REQUESTCODE = 10000;
    public static final int IMG_PIC_CODE = 10001;
    public static final int IMG_CAPTURE = 10002;
    public static final int IMG_CAMCUTPIC = 10003;
    public static final int IMG_CAMCUTRES = 10004;
    public static final int IMG_SYSIMGCUTPIC = 10005;
    public static final int IMG_SYSIMGCUTRES = 10006;
    /*
   * RN callback ids
   */
    //拍照可能带有裁剪的回调
    public static final int ID_IMG_CAMTOCUT_CALLBACK = 18;
    //系统相册选择照片裁剪
    public static final int ID_IMG_SYSIMGCUT_CALLBACK = 19;
    public static Context mContext;

    private static String mPackageName;
    private static float density = 1.5f;
    private static Point screenSize;
    private static final String TAG= "BerTools";

    static {
        screenSize = new Point(480, 800);
    }

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }
    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }

    public static Point getScreenSize() {
        return screenSize;
    }

    public static void setScreenSize(int width, int height) {
        screenSize.x = width;
        screenSize.y = height;
    }

    public static void setDensity(float den) {
        density = den;
    }

    public static void setAppDir(String name) {
        mPackageName = name;
    }

    public static String getAppDir() {
        return mPackageName;
    }
/*
    public static String getResourceAbsolutePath(String path) {
        if (null == path) {
            return null;
        }

        String osPath = path.replace("\\", "/");

        String rootPath = lbtjni.GetSDCardPath();// Environment.getExternalStorageDirectory().getAbsolutePath();
        if (osPath.startsWith(rootPath)) {
            return osPath;
        }

        return rootPath + File.separator + mPackageName + "/" + osPath;
    }*/

    public static String toGB2312String(String src) {
        String newStr = null;

        if (null != src) {
            try {
                newStr = new String(src.getBytes(), "gb2312");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return newStr;
    }

    public static boolean copyFile(String newPath, Context context, InputStream inputStream) {

        boolean isok = true;
        if (newPath == null) {
            isok = false;
        }
        Log.i(TAG, "copyFile " + newPath);

        @SuppressWarnings("unused")
        int bytesum = 0;
        int byteread = 0;

        InputStream inStream = inputStream;
        File newfile = new File(newPath);

        if (!newfile.getParentFile().exists()) {
            newfile.getParentFile().mkdirs();
            newfile.getParentFile().setWritable(true);
        }

        try {
            FileOutputStream fs = new FileOutputStream(newPath);

            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // �ֽ��� �ļ���С
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            fs.close();

            inStream.close();
        } catch (Exception e) {
            Log.i("exc", "excepiton" + e.getMessage());
            isok = false;
        }
        return isok;

    }

    public static boolean copyFile(String oldPath, String newPath) {
        if(null == oldPath){
            return false;
        }

        if(oldPath.equals(newPath)){
            return true;
        }

        boolean res = false;
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists() && oldfile.isFile()) {
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024 * 8];
                int length;
                while ((length = inStream.read(buffer)) > 0) {
                    fs.write(buffer, 0, length);
                }
                inStream.close();
                fs.close();
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
     * 为了适应不同屏幕，应用传递过来的像素应该理解成dip然后进行换算, 应用是按照640X960的屏幕进行布局。
     */
    public static int dp2px(int dp) {
        // return Math.round(dp*density/2.0f);
        return dp;
    }

    // 获取系统时间
    public static String getSystemTime() {
        String systemTime = "";
        Time t = new Time(); // or Time t=new Time("GMT+8");

        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        systemTime = "" + year + "-" + month + "-" + date + "-" + hour + "-"
                + minute + "-" + second;
        return systemTime;
    }

    // 转换从内核传入的RGB颜色为平台可识别颜色,如果传输数据错误，缺省颜色为白色
    public static int getAndroidRGBColor(int color) {

        int nr = Color.red(color);
        int ng = Color.green(color);
        int nb = Color.blue(color);
        if (Color.argb(255, nr, ng, nb) == 0) {
            return Color.parseColor("#ffffff");
        } else {
            return Color.argb(255, nr, ng, nb);
        }

    }

    // 设置控件点击变色
    public static void setViewBackColorTouch(final Button btn,
                                             final int colorback, final int coloract) {
        btn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(colorback);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(coloract);
                }
                return false;
            }
        });
    }

    // 压缩图片返回BitMap
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    // 设置TextView背景图片
    @SuppressWarnings("deprecation")
    public static void setTextViewBackPic(final TextView textView,
                                          final String backPath, final int viewWith, final int viewHight) {
        if ((android.os.Build.VERSION.SDK_INT) < 16) {
            textView.setBackgroundDrawable(Drawable.createFromPath(backPath));
        } else {
            textView.setBackground(Drawable.createFromPath(backPath));
        }

    }

    // 设置按钮点击时背景图片的变化
    public static void setBtnBackPicTouch(final Button btn,
                                          final String backPath, final String actPath) {
        btn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((android.os.Build.VERSION.SDK_INT) < 16) {
                        btn.setBackgroundDrawable(Drawable
                                .createFromPath(backPath));
                    } else {
                        btn.setBackground(Drawable.createFromPath(backPath));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if ((android.os.Build.VERSION.SDK_INT) < 16) {
                        btn.setBackgroundDrawable(Drawable
                                .createFromPath(actPath));
                    } else {
                        btn.setBackground(Drawable.createFromPath(actPath));
                    }
                }
                return false;
            }
        });
    }

    // 存储预览图片
    public static void saveBitmap(Bitmap bitMap, String filePath) {
        Log.e("", "save start!");
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitMap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            bitMap.recycle();
            Log.i("", "save finish!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("", e.toString());
        }
    }

    /**
     * 压缩图片：将原有的图片压缩。
     *
     * @param picPath
     *            需压缩的图片路径
     * @param quity
     *            压缩质量0~100,0为质量最低（图片会严重失真），100为不压缩，建议压缩值最小为10。
     * @param x
     *            修改图片宽（根据该值计算宽度压缩比）
     * @param y
     *            修改图片高（根据该值计算高度压缩比）
     * @param picChangePath
     *            压缩后存放路径（该值可为空，如果该值为空，则将原来的图片用压缩后的图片覆盖）
     * @return 是否压缩成功
     */
    public static boolean compressPic(String picPath, int quity, int x, int y,
                                      int screenWith, int screenHeight, String picChangePath) {
        BitmapFactory.Options options = null;
        int changeInSampleSize = 2;
        Log.e(TAG, "x= "+x);
        Log.e(TAG, "y= "+y);
        int angle = 0;
        boolean isNeedChange = true;
        String tempChangePath = null;
        String tempChangeName = null;
        String temp = "ceshi";
        // 进入时先处理照片的宽高
        int trueWidth = 0;
        int trueHeight = 0;
        // 取图缩放比例
        float scaleAllSzie = 0;
        // 获取原来图片的信息
        ImageInfoClass tempInfo = new ImageInfoClass();
        ExifInterface readExifInterface;
        try {
            readExifInterface = new ExifInterface(picPath);
            tempInfo.setGpsLatInfo(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            tempInfo.setLatInfoRef(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            tempInfo.setGpsLongInfo(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            tempInfo.setLongInfoRef(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
            tempInfo.setImageTime(readExifInterface
                    .getAttribute(ExifInterface.TAG_DATETIME));
            if(ExifInterface.ORIENTATION_ROTATE_90 == readExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED) ||
                    ExifInterface.ORIENTATION_ROTATE_270 == readExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)){
                int tmpSize = screenWith;
                screenWith = screenHeight;
                screenHeight = tmpSize;
                tmpSize = x;
                x = y;
                y = tmpSize;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // 如果图片的路径为空，或者压缩质量大于0或者压缩质量小于零，那么都返回false；
        if (picPath == null || picPath.equals("") || quity < 0 || quity > 100) {
            return false;
        }
        // 如果压缩后存放图片路径为空。那就将需压缩的图片路径复制给压缩后存放路径。覆盖原来的图片。
        if (picChangePath == null || picChangePath.equals("")) {
            picChangePath = picPath;
        }

        int index = picChangePath.lastIndexOf("/");
        tempChangeName = picChangePath.substring(index+1);
        tempChangeName = temp +tempChangeName;
        tempChangePath = picChangePath.substring(0, index) + "/" +tempChangeName;

        // 获取图片生成bitmap
        // 使用decodeFile方法得到图片的宽和高
        Bitmap firstBitmap = null;
        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            firstBitmap = BitmapFactory.decodeFile(picPath, options);
            /**
             * 首先应该判断照片是不是超出屏幕范围，如果超出屏幕范围，就先按照比例做成屏幕适应的最大尺寸图。
             * 之后判断应用传递的尺寸是否超出屏幕范围，如果超出屏幕范围，则直接使用适应屏幕最大尺寸的图。
             * 如果尺寸小于屏幕范围，则对图片进行缩放，缩放到应用给出的分辨率。
             */
            //如果照片的大小大于了屏幕大小的1.5倍，先缩小尺寸
            if (options.outWidth*options.outHeight - screenWith*screenHeight*3/2 > 0) {
                float scaleSize = ((float) options.outWidth)
                        / (float) options.outHeight;

                if (options.outWidth >= screenWith
                        && options.outHeight >= screenHeight) {
                    // 两个都比屏幕大时,如果以屏高和图片比例计算的宽度比屏宽小或者等于屏宽，那么即取屏高为真实高度，计算宽度为真实宽度。
                    int tempWith = (int) (scaleSize * (float) screenHeight);
                    int tempHeight = (int) ((float) screenWith / scaleSize);
                    if (tempWith <= screenWith) {
                        trueWidth = tempWith;
                        trueHeight = screenHeight;
                        scaleAllSzie = (float) options.outHeight
                                / (float) trueHeight;
                        if (scaleAllSzie > 1.0f && scaleAllSzie < 2.0f) {
                            scaleAllSzie = 2.0f;
                        }
                    } else {
                        trueWidth = screenWith;
                        trueHeight = tempHeight;
                        scaleAllSzie = (float) options.outWidth
                                / (float) trueWidth;
                        if (scaleAllSzie > 1.0f && scaleAllSzie < 2.0f) {
                            scaleAllSzie = 2.0f;
                        }
                    }
                } else {
                    //只有一个比屏幕大时，那么取大的那个值计算另一个的值，结果组即为世界值。
                    if (options.outWidth >= options.outHeight) {
                        trueWidth = screenWith;
                        trueHeight = (int) ((float) (screenWith) / scaleSize);
                        scaleAllSzie = (float) options.outWidth
                                / (float) trueWidth;
                        if (scaleAllSzie > 1.0f && scaleAllSzie < 2.0f) {
                            scaleAllSzie = 2.0f;
                        }
                    } else {
                        trueHeight = screenHeight;
                        trueWidth = (int) ((float) (screenHeight) * scaleSize);
                        scaleAllSzie = (float) options.outHeight
                                / (float) trueHeight;
                        if (scaleAllSzie > 1.0f && scaleAllSzie < 2.0f) {
                            scaleAllSzie = 2.0f;
                        }
                    }
                }
            } else {
                // 如果照片的实际尺寸比屏幕的1.5倍小，那么就以原图的宽高为真实值，不缩放
                trueWidth = options.outWidth;
                trueHeight = options.outHeight;
                scaleAllSzie = 1.0f;
            }
            // 得到处理过好的宽高和需要缩放的比例
            options.inSampleSize = (int) scaleAllSzie;
            options.inJustDecodeBounds = false;
            firstBitmap = BitmapFactory.decodeFile(picPath, options);
            if (firstBitmap == null) {
                Log.i("", "compressPic is null");
                return false;
            }
        } catch (OutOfMemoryError e) {
            changeInSampleSize = (int) scaleAllSzie;
            int i = 0;
            while (isNeedChange) {
                i++;
                firstBitmap.recycle();
                Log.e("", "compressPic Exception is " + e.toString());
                options = new BitmapFactory.Options();
                changeInSampleSize = (int) scaleAllSzie
                        + (int) Math.pow((double) 2, (double) i);
                options.inSampleSize = changeInSampleSize;
                options.inJustDecodeBounds = false;
                try {
                    firstBitmap = BitmapFactory.decodeStream(
                            new FileInputStream(picPath), null, options);
                    isNeedChange = false;
                } catch (OutOfMemoryError e2) {
                    // TODO: handle exception
                    isNeedChange = true;
                } catch (FileNotFoundException e3) {
                    return false;
                }
            }
        }
        // 以上即是对去bitmap的异常处理
        /**
         * 下面计算图片分辨率压缩比。
         */
        Matrix matrix = new Matrix();
        float xScale = 0.0f;
        float yScale = 0.0f;
        if (x > screenWith || y > screenHeight) {
            xScale = 1.0f;
            yScale = 1.0f;
        } else {
            // 宽高比例压缩
            // x,y均为0时不对分辨率做处理；x，y有一个不为0，以不为0的比例为准
            if (x == 0 && y == 0) {
                xScale = 1.0f;
                yScale = 1.0f;
            } else if (x == 0 && y != 0) {
                yScale = (float) y / (firstBitmap.getHeight());
                xScale = yScale;
            } else if (x != 0 && y == 0) {
                xScale = (float) x / (firstBitmap.getWidth());
                yScale = xScale;
            } else {
                xScale = (float) x / (firstBitmap.getWidth());
                yScale = (float) y / (firstBitmap.getHeight());
            }
        }

        Log.e(TAG, "xScale= "+xScale);
        Log.e(TAG, "yScale= "+yScale);
        // 宽高按比例缩放
        ExifInterface exInterface = null;
        try {
            exInterface = new ExifInterface(picPath);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if(ExifInterface.ORIENTATION_ROTATE_90 == exInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)){
            angle = 90;
        }else if(ExifInterface.ORIENTATION_ROTATE_180 == exInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)){
            angle = 180;
        }else if(ExifInterface.ORIENTATION_ROTATE_270 == exInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)){
            angle = 270;
        }

        matrix.postScale(xScale, yScale);
        try {
            firstBitmap = Bitmap.createBitmap(firstBitmap, 0, 0,
                    firstBitmap.getWidth(), firstBitmap.getHeight(), matrix,
                    true);
        } catch (OutOfMemoryError e2) {
            // TODO: handle exception
            return false;
        }
        // 质量压缩，并保存图片。
        Log.e("", "save compresspic start!");
        File f = new File(tempChangePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            firstBitmap.compress(Bitmap.CompressFormat.JPEG, quity, out);
            out.flush();
            out.close();
            // 将原来信息写入到压缩后的文件中去
            ExifInterface saveExifInterface = new ExifInterface(tempChangePath);
            if(tempInfo.getGpsLatInfo() != null){
                saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                        tempInfo.getGpsLatInfo());
            }
            if(tempInfo.getLatInfoRef() != null ){
                saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,
                        tempInfo.getLatInfoRef());
            }
            if(tempInfo.getGpsLongInfo() != null){
                saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                        tempInfo.getGpsLongInfo());
            }
            if(tempInfo.getLongInfoRef() != null){
                saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,
                        tempInfo.getLongInfoRef());
            }
            if(tempInfo.getImageTime() != null){
                saveExifInterface.setAttribute(ExifInterface.TAG_DATETIME,
                        tempInfo.getImageTime());
            }

            saveExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(exInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)));

            if (!firstBitmap.isRecycled()) {
                firstBitmap.recycle();
                System.gc();
            }

//			if ((angle == 90  || angle == 180 || angle == 270)&& tempChangePath!=null) {
//				Bitmap bitmap = BitmapFactory.decodeFile(tempChangePath);
//				File file = new File(tempChangePath);
//
//				FileOutputStream outputStream = new FileOutputStream(file);
//				Matrix mat = new Matrix();
//				mat.setRotate(angle);
//				bitmap = Bitmap.createBitmap(bitmap, 0, 0,
//						bitmap.getWidth(), bitmap.getHeight(), mat,
//						true);
//				bitmap.compress(Bitmap.CompressFormat.JPEG, quity, outputStream);
//				outputStream.flush();
//				outputStream.close();
//				if (!bitmap.isRecycled()) {
//					bitmap.recycle();
//					System.gc();
//				}
//			}
            //修改旋转后没有照片属性
            if (tempInfo.getGpsLatInfo() != null || tempInfo.getLatInfoRef() != null
                    || tempInfo.getGpsLongInfo() != null|| tempInfo.getLongInfoRef() != null || tempInfo.getImageTime() != null) {
                try {
                    saveExifInterface.saveAttributes();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("", "ExifInterfacess:"+e.toString());
                }
            }


            Log.i("", "save compresspic end!");
            if(picPath.equals(picChangePath)){
                File picFile = new File(picPath);
                if (picFile.exists()) {
                    picFile.delete();
                }
            }

            File tempFile = new File(tempChangePath);
            tempFile.renameTo(new File(picChangePath));
            return true;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("", "ExifInterfacess:"+e.toString());
            return false;
        }
    }
    // 预存储预安装文件
    public static boolean copyApkFromAssets(Context context, String fileName,
                                            String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     *
     * @param imagePath
     *            需要修改时间的图片路径
     * @param imageTime
     *            需要修改成的时间
     * @return 成功或失败
     */
    public static boolean setImageTime(String imagePath, String imageTime) {
        // TODO setImageTime
        // 如果路径为空，或者字符空，或者该文件就不存在。
        if (imagePath == null || imagePath.equals("")
                || !new File(imagePath).exists()) {
            return false;
        }

        ExifInterface saveExifInterface;
        try {
            saveExifInterface = new ExifInterface(imagePath);
            // 如果修改时间不为null并且不为字符空，才进行时间设置
            if (imageTime != null && !imageTime.equals("")) {
                // 修改照片时间
                saveExifInterface.setAttribute(ExifInterface.TAG_DATETIME,
                        imageTime);
                saveExifInterface.saveAttributes();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    /**
     *
     * @param imagePath
     * @param Latitude
     * @param Lontitude
     * @return 是否设置成功
     */
    public static boolean setImageGPSInfo(String imagePath, double Latitude,
                                          double Lontitude) {
        // TODO setImageGPS info
        if (imagePath == null || imagePath.equals("")
                || !new File(imagePath).exists()) {
            return false;
        }
        try {
            ExifInterface saveExifInterface = new ExifInterface(imagePath);
            // 设置经度
            saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                    decimalToDMS(Latitude));
            // 设置南北半球
            saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,
                    Latitude > 0 ? "N" : "S");
            // 设置纬度
            saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                    decimalToDMS(Lontitude));
            // 设置东西半球
            saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,
                    Lontitude > 0 ? "E" : "W");
            // 设置完毕后保存设置
            saveExifInterface.saveAttributes();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取照片时间
     *
     * @param imagePath
     *            照片路径
     * @return 返回照片路径
     */
    public static String getImageTime(String imagePath) {
        // TODO getImageTime
        // 文件路径为空，或者文件不存在是，返回null值
        if (imagePath == null || imagePath.equals("")
                || !new File(imagePath).exists()) {
            return null;
        }
        try {
            // 读取照片时间信息，并返回
            ExifInterface readExifInterface = new ExifInterface(imagePath);
            Log.i("",
                    "getImageTime test"
                            + readExifInterface
                            .getAttribute(ExifInterface.TAG_DATETIME));
            return readExifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取GPS中的经度
     *
     * @param imagePath
     * @return 获取经度的值
     */
    public static double getImageGpsLatitude(String imagePath) {
        // TODO getImageGPSLatitude
        // 如果文件路径为空或者文件不存在。那么就返回值就为空
        if (imagePath == null || imagePath.equals("")
                || !new File(imagePath).exists()) {
            return 0.00;
        }
        try {
            // 获取GPS经度
            ExifInterface readExifInterface = new ExifInterface(imagePath);
            double latitude = dmsToDecimal(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            // 如果为北半球那么经度为正，如果在南半球，那么经度为负
            String temp = (readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            if (temp.equals("S")) {
                latitude *= -1;
            }
            return latitude;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("", "setGpsInfo exception" + e.toString());
            return 0.00;
        }
    }

    /**
     * 获取GPS中的纬度
     *
     * @param imagePath
     * @return 获取纬度的值
     */
    public static double getImageGpsLongtude(String imagePath) {
        // TODO getImageGpsLongtude
        // 如果文件路径为空或者文件不存在。那么就返回值就为空
        if (imagePath == null || imagePath.equals("")
                || !new File(imagePath).exists()) {
            return 0.00;
        }
        try {
            // 获取GPS经度
            ExifInterface readExifInterface = new ExifInterface(imagePath);
            double longtude = dmsToDecimal(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            // 如果为北半球那么经度为正，如果在南半球，那么经度为负
            if ((readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF))
                    .equals("W")) {
                longtude *= -1;
            }
            return longtude;
        } catch (Exception e) {
            // TODO: handle exception
            return 0.00;
        }
    }

    /**
     *
     * @param coord
     *            double度数转时分秒角度
     * @return
     */
    public static String decimalToDMS(double coord) {
        // TODO decimalToDms
        String output, degrees, minutes, seconds;

        // gets the modulus the coordinate divided by one (MOD1).
        // in other words gets all the numbers after the decimal point.
        // e.g. mod := -79.982195 % 1 == 0.982195
        //
        // next get the integer part of the coord. On other words the whole
        // number part.
        // e.g. intPart := -79

        double mod = coord % 1;
        int intPart = (int) coord;

        // set degrees to the value of intPart
        // e.g. degrees := "-79"

        degrees = String.valueOf(intPart);

        // next times the MOD1 of degrees by 60 so we can find the integer part
        // for minutes.
        // get the MOD1 of the new coord to find the numbers after the decimal
        // point.
        // e.g. coord := 0.982195 * 60 == 58.9317
        // mod := 58.9317 % 1 == 0.9317
        //
        // next get the value of the integer part of the coord.
        // e.g. intPart := 58

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;
        if (intPart < 0) {
            // Convert number to positive if it's negative.
            intPart *= -1;
        }
        // set minutes to the value of intPart.
        // e.g. minutes = "58"
        minutes = String.valueOf(intPart);

        // do the same again for minutes
        // e.g. coord := 0.9317 * 60 == 55.902
        // e.g. intPart := 55
        coord = mod * 60;
        intPart = (int) (coord * 100000.0);
        // intPart = (int)coord;
        if (intPart < 0) {
            // Convert number to positive if it's negative.
            intPart *= -1;
        }

        // set seconds to the value of intPart.
        // e.g. seconds = "55"
        seconds = String.valueOf(intPart);

        // I used this format for android but you can change it
        // to return in whatever format you like
        // e.g. output = "-79/1,58/1,56/1"
        output = degrees + "/1," + minutes + "/1," + seconds + "/100000";

        // Standard output of D°M′S″
        // output = degrees + "°" + minutes + "'" + seconds + "\"";

        return output;
    }

    /**
     *
     * @param dmsString
     * @return 时分秒角度转 double度数
     */
    public static double dmsToDecimal(String dmsString) {
        // TODO dmsToDECImal
        //文件中的经纬度格式为:a.x/b.y,c.x/d.y,e.x/f.y;
        // 经纬度小数部分
        double mod = 0.0;
        // 整体经纬度
        double decimal = 0.0;
        // 定义度分秒
        String degress = "", minutes = "", seconds = "";
        // 定义度分秒后面的单位
        String degreeUnit = "", minutesUnit = "", secondsUnit = "";
        // 判断获取的值是否为空
        if (dmsString == null || dmsString.equals("")
                || TextUtils.isEmpty(dmsString)) {

        } else {
            // 获取度
            degress = dmsString.substring(0, dmsString.indexOf("/"));
            // 获取度的单位
            degreeUnit = dmsString.substring(dmsString.indexOf("/") + 1,
                    dmsString.indexOf(","));
            // 判断","是否存在
            if (dmsString.indexOf(",") > -1) {
                // 截取"，"之后的字符
                String temp = dmsString.substring(dmsString.indexOf(",") + 1);
                // 获取分
                minutes = temp.substring(0, temp.indexOf("/"));
                // 获取分的单位
                minutesUnit = temp.substring(temp.indexOf("/") + 1,
                        temp.indexOf(","));
                // 判断剩余的字符中是否含有","
                if (temp.indexOf(",") > -1) {
                    // 截取","之后的字符
                    String temp2 = temp.substring(temp.indexOf(",") + 1);
                    // 获取秒
                    seconds = temp2.substring(0, temp2.indexOf("/"));
                    // 获取秒的单位
                    secondsUnit = temp2.substring(temp2.indexOf("/") + 1);
                }
            }

        }
        // 度分秒只要有一个获取不到为字符空，那么即为获取异常。则全部归零
        if (seconds.equals("") || minutes.equals("") || degress.equals("")) {
            mod = 0.0;
            decimal = 0.0;
        } else {
            // 计算获得秒的小数
            mod = ((double) Integer.valueOf(seconds) / (double) Integer
                    .valueOf(secondsUnit)) / (double) 60.0;
            // 计算获取的整个小数部分
            mod = ((double) Integer.valueOf(minutes)
                    / (double) Integer.valueOf(minutesUnit) + mod)
                    / (double) 60.0;
            // 计算获取整个
            decimal = mod + (double) Integer.valueOf(degress)
                    / (double) Integer.valueOf(degreeUnit);
        }
        Log.i("", "dmsToDecimal decimal:" + decimal);
        return decimal;
    }

    public static void setButtonChangePic(final Button button,
                                          final String imagePicPath, final String imageChangePicPath) {
        // 如果路径中有一个值不存在，那么就不对背景变换进行设置
        if (imagePicPath == null || imagePicPath.isEmpty()
                || !new File(imagePicPath).exists()
                || imageChangePicPath == null || imageChangePicPath.isEmpty()
                || !new File(imageChangePicPath).exists()) {

        } else {
            button.setOnTouchListener(new View.OnTouchListener() {

                @SuppressWarnings("deprecation")
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub
                    switch (arg1.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if ((android.os.Build.VERSION.SDK_INT) < 16) {
                                button.setBackgroundDrawable(Drawable
                                        .createFromPath(imageChangePicPath));
                            } else {
                                button.setBackground(Drawable
                                        .createFromPath(imageChangePicPath));
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                            if ((android.os.Build.VERSION.SDK_INT) < 16) {
                                button.setBackgroundDrawable(Drawable
                                        .createFromPath(imagePicPath));
                            } else {
                                button.setBackground(Drawable
                                        .createFromPath(imagePicPath));
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

    }

  /*  public static RegInfoClass[] getSignInfo(String dataString,String patternString,int pOptions){
        List<RegInfoClass> tempList = new ArrayList<RegInfoClass>();
        Pattern p ;
        try {
            switch (pOptions) {
                case 0:
                    p = Pattern.compile(patternString,Pattern.DOTALL);
                    break;
                case 1:
                    p = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);
                    break;
                case 2:
                    p = Pattern.compile(patternString,Pattern.MULTILINE);
                    break;
                default:
                    p = Pattern.compile(patternString);
                    break;
            }
        } catch (PatternSyntaxException e) {
            // TODO: handle exception
            return null;
        }
        Matcher temp = p.matcher(dataString);
        String testString = dataString;
        while(temp.find()){
            RegInfoClass tempRegInfo = new RegInfoClass();
            try {
                int indexSign = testString.substring(0, temp.start()).getBytes("gb2312").length;
                int dataStringLenth = temp.group().getBytes("gb2312").length;
                tempRegInfo.setStringAlign(indexSign);
                tempRegInfo.setStringLenth(dataStringLenth);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tempList.add(tempRegInfo);
        }
        RegInfoClass[] results = new RegInfoClass[tempList.size()];
        tempList.toArray(results);
        return results;
    }
*/
    public static boolean isMatch(String pData, String pPattern){
        Pattern p;
        try {
            p = Pattern.compile(pPattern);
        } catch (PatternSyntaxException e) {
            // TODO: handle exception
            return false;
        }
        Matcher temp = p.matcher(pData);
        return temp.find();
    }
    /**
     *
     * @param data 需要计算的数据
     * @param type 获取字符数的类型,0：数字，1：字母，2：汉字和字符
     * @return 对应字符类型的字符数
     */
    public static int getSignCountByType(String data,int type){
        int unicodeCount = 0;
        int szCount = 0;
        int zmCount = 0;
        int result=0;
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c >= '0' && c <= '9') {
                szCount++;
            }else if((c >= 'a' && c<='z') || (c >= 'A' && c<='Z')){
                zmCount++;
            }else{
                unicodeCount++;
            }
        }
        switch (type) {
            case 0:
                result = szCount;
                break;
            case 1:
                result = zmCount;
                break;
            case 2:
                result = unicodeCount;
                break;
            default:
                result = szCount;
                break;
        }
        return result;
    }
    /**
     * 图片信息类
     * @author Administrator sjl
     *
     */
    public static class ImageInfoClass {
        private String gpsLatInfo = "", gpsLongInfo = "";
        private String imageTime = "";
        private String latInfoRef = "", longInfoRef = "";
        private String orientation = "";

        public String getLatInfoRef() {
            return latInfoRef;
        }

        public void setLatInfoRef(String latInfoRef) {
            this.latInfoRef = latInfoRef;
        }

        public String getLongInfoRef() {
            return longInfoRef;
        }

        public void setLongInfoRef(String longInfoRef) {
            this.longInfoRef = longInfoRef;
        }

        public String getGpsLatInfo() {
            return gpsLatInfo;
        }

        public void setGpsLatInfo(String gpsLatInfo) {
            this.gpsLatInfo = gpsLatInfo;
        }

        public String getGpsLongInfo() {
            return gpsLongInfo;
        }

        public void setGpsLongInfo(String gpsLongInfo) {
            this.gpsLongInfo = gpsLongInfo;
        }

        public String getImageTime() {
            return imageTime;
        }

        public void setImageTime(String imageTime) {
            this.imageTime = imageTime;
        }

        public void setOrientation(String orientation){
            this.orientation = orientation;
        }

        public String getOrientation(){
            return this.orientation;
        }
    }

    public static HashMap<String, String> getAppCfg(Context ctx, int id){
        HashMap<String, String> cfg = new HashMap<String, String>();

        InputStream inputStream = ctx.getResources().openRawResource(id);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String readStr = null;
        try{
            while((readStr=br.readLine()) != null){
                String []kv = readStr.split("=");
                if (readStr.startsWith("#")) {
                    continue;
                }
                if(null == kv || kv.length != 2){
                    continue;
                }

                cfg.put(kv[0].trim(), kv[1].trim());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return cfg;
    }

    public static void hideInputMethod(Context userContext) {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) userContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        if(imm!=null){
            imm.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
        }
    }

    public static void hideInputMethod(Context userContext,View view) {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) userContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        if(imm!=null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    //读取图片头信息
    public static ImageInfoClass getPicInfos(String picPath){
        // 获取原来图片的信息
        ImageInfoClass tempInfo = new ImageInfoClass();
        ExifInterface readExifInterface;
        try {
            readExifInterface = new ExifInterface(picPath);
            tempInfo.setGpsLatInfo(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            tempInfo.setLatInfoRef(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            tempInfo.setGpsLongInfo(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            tempInfo.setLongInfoRef(readExifInterface
                    .getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
            tempInfo.setImageTime(readExifInterface
                    .getAttribute(ExifInterface.TAG_DATETIME));
            tempInfo.setOrientation(readExifInterface
                    .getAttribute(ExifInterface.TAG_ORIENTATION));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
            Log.i(TAG, "getPicInfos ->>>>"+e1.toString());
            tempInfo=null;
        }
        return tempInfo;
    }

    //写入图片的基本信息
    public static boolean setPicInfos(String picPath,ImageInfoClass tempInfo){
        try {
            ExifInterface saveExifInterface = new ExifInterface(picPath);
            if (tempInfo.getGpsLatInfo() != null) {
                saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                        tempInfo.getGpsLatInfo());
            }
            if (tempInfo.getLatInfoRef() != null) {
                saveExifInterface.setAttribute(
                        ExifInterface.TAG_GPS_LATITUDE_REF,
                        tempInfo.getLatInfoRef());
            }
            if (tempInfo.getGpsLongInfo() != null) {
                saveExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                        tempInfo.getGpsLongInfo());
            }
            if (tempInfo.getLongInfoRef() != null) {
                saveExifInterface.setAttribute(
                        ExifInterface.TAG_GPS_LONGITUDE_REF,
                        tempInfo.getLongInfoRef());
            }
            if (tempInfo.getImageTime() != null) {
                saveExifInterface.setAttribute(ExifInterface.TAG_DATETIME,
                        tempInfo.getImageTime());
            }

            if (tempInfo.getOrientation() != null && tempInfo.getOrientation().length() > 0){
                saveExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,
                        tempInfo.getOrientation());
            }

            if (tempInfo.getGpsLatInfo() != null
                    || tempInfo.getLatInfoRef() != null
                    || tempInfo.getGpsLongInfo() != null
                    || tempInfo.getLongInfoRef() != null
                    || tempInfo.getImageTime() != null) {
                try {
                    saveExifInterface.saveAttributes();
                    return true;
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("", "ExifInterfacess:" + e.toString());
                    return false;
                }
            } else
                return false;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, "setImageInfo->>>>>" + e.toString());
            return false;
        }
    }
    //拍照调用函数
    public static Uri openCamToCut(String savePath, boolean isNeedWirthGpsInfo, final int result_ID, Context context){
        mContext = context;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri output = null;
        Uri outPath = null;
        if(null != savePath){
            File outFile = new File(savePath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }else{
                outFile.delete();
            }
            output = Uri.fromFile(outFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);

            int index = savePath.lastIndexOf("/");
            if(index > 0){
                String name = savePath.substring(index + 1);
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, name);
                values.put(MediaStore.Images.Media.DATA, savePath);
                values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                int del = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA+"=?",
                        new String[]{savePath});
                outPath = context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            }

        }else{
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);

            output = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);

            outPath = output;
        }

        ((Activity)context).startActivityForResult(intent, result_ID);
        /*if (isNeedWirthGpsInfo) {
            ((Activity)context).startActivityForResult(intent, result_ID);
            //创建gps实例
            GPSState temp = new GPSState(context);
            if (!temp.getGpsState()) {
                Intent gpsIntent = new Intent(context, GpsDialog.class);
                ((Activity)context).startActivity(gpsIntent);
            }
        } else {
            ((Activity)context).startActivityForResult(intent, result_ID);
        }*/

        return outPath;
    }
    public static void sysImgCut(String imgPath,final int result_ID,Context context){
        Intent innerIntent = new Intent(Intent.ACTION_PICK); // "android.intent.action.GET_CONTENT"
        mContext = context;

        innerIntent.setType("image/*"); // 查看类型
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);

        ((Activity)context).startActivityForResult(wrapperIntent,result_ID);
    }
/*
    public static void saveGpsInfoUseThread(final Context useActivity,final CallbackInfo callback){
        //
        GPSState gpsstate = new GPSState();
        ((lbtjni)useActivity).openGPS(gpsstate, 0);
        // 创建一个识别GPS信息是否取到的线程
        new Thread(new Runnable() {

            @Override
            public void run() {
                int i=0;
                while(i<3){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    BDLocation myBDLocation = null;
                    if (null != ((lbtjni)useActivity).mLocClient) {
                        myBDLocation = ((lbtjni)useActivity).mLocClient
                                .getLastKnownLocation();
                    }
                    if (myBDLocation == null) {
                        if (i >= 2) {
                            ((lbtjni)useActivity).imageCaptureResult(
                                    (String) callback.tag3,
                                    callback.pCallback,
                                    callback.pCallbackParams);
                            ((lbtjni)useActivity).closeGPS(((lbtjni)useActivity).gpsState);
                        }
                        Log.i(TAG,
                                "When colseCamear,get GPSInfo is null!");
                    } else {
                        Log.i(TAG,
                                "When colseCamear,get GPSInfo Latitude ="
                                        + myBDLocation
                                        .getLatitude()
                                        + ";Longliutate = "
                                        + myBDLocation
                                        .getLongitude());
                        InnerTools.setImageGPSInfo(
                                (String) callback.tag,
                                myBDLocation.getLatitude(),
                                myBDLocation.getLongitude());
                        ((lbtjni)useActivity).imageCaptureResult((String) callback.tag3,
                                callback.pCallback,
                                callback.pCallbackParams);
                        ((lbtjni)useActivity).closeGPS(((lbtjni)useActivity).gpsState);
                    }
                    i++;
                }
            }
        }).start();
    }*/

    public static boolean cropPic(String filePath,int nWidth,int nHeight,Context context,final int result_ID){
        File temp = new File(filePath);
        if(temp.exists()&&temp.isFile()){
            final Intent intents = new Intent(
                    "com.android.camera.action.CROP");
            intents.setDataAndType(Uri.fromFile(temp), "image/*");
            if(nHeight > 320 || nWidth > 320){
                float scale = (float)nHeight/nWidth;
                if(scale > 1){
                    nHeight = 320;
                    nWidth = (int)(320 / scale);
                }else{
                    nWidth = 320;
                    nHeight = (int)(320 * scale);
                }
            }
            intents.putExtra("aspectX", nWidth);
            intents.putExtra("aspectY", nHeight);
            intents.putExtra("outputX", nWidth);
            intents.putExtra("outputY", nHeight);
            intents.putExtra("scaleUpIfNeeded", true);
            intents.putExtra("scale", true);
            intents.putExtra("crop", "true");
            intents.putExtra("noFaceDetection", true);
            intents.putExtra("return-data", true);
            ((Activity)context).startActivityForResult(intents, result_ID);
            return true;
        }else{
            return false;
        }
    }





    public static int getImageRatation(String path){
        ExifInterface exInterface = null;
        int angle = 0;
        try {
            exInterface = new ExifInterface(path);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        int imgAngle = exInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        if(ExifInterface.ORIENTATION_ROTATE_90 == imgAngle){
            angle = 90;
        }else if(ExifInterface.ORIENTATION_ROTATE_180 == imgAngle){
            angle = 180;
        }else if(ExifInterface.ORIENTATION_ROTATE_270 == imgAngle){
            angle = 270;
        }


        return angle;
    }

    public static Bitmap ratationBitmap(int angle, Bitmap bmp){
        if(null == bmp){
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        return bitmap;
    }

    public static String getRealPathFromURI(Context ctx, Uri contentUri) {
        if ("file".equalsIgnoreCase(contentUri.getScheme())) {
            return contentUri.getPath();
        }else {
            String res = null;
            String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE};
            Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                res = cursor.getString(column_index);
                cursor.close();
            }
            return res;
        }
    }
    //计算Move两点间距离
    public static int twoPointRange(Point point1,Point point2){
        int range = 0;
        double tempDouble = Math.pow(point1.x-point2.x,2)+Math.pow(point1.y-point2.y, 2);
        range = (int)Math.sqrt(tempDouble);
        return range;
    }

    public static void readProcessInputStream(Process proc){
        if(null == proc){
            Log.i(TAG, "readProcessInputStream process is null");
            return;
        }

        final BufferedReader inputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String str = null;
        try {
            while ((str = inputStream.readLine()) != null) {
                Log.i(TAG, "process output + " + str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 删除文件
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     * @return boolean
     */
    public static void delFile(String filePathAndName)
    {
        try
        {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();
        }
        catch (Exception e)
        {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     * @param folderPath String
     */
    public static void delFolder(String folderPath)
    {
        try
        {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        }
        catch (Exception e)
        {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     * @param path String 文件夹路径 如 c:/fqf
     */
    public static void delAllFile(String path)
    {
        File file = new File(path);
        if (!file.exists())
        {
            return;
        }
        if (!file.isDirectory())
        {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++)
        {
            if (path.endsWith(File.separator))
            {
                temp = new File(path + tempList[i]);
            }
            else
            {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile())
            {
                temp.delete();
            }
            if (temp.isDirectory())
            {
                delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文件
                delFolder(path+"/"+ tempList[i]);//再删除空文件夹
            }
        }
    }



    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void disableHardwareAccelerated(View v){
        String phoneModel = android.os.Build.MODEL;
        if( "M040".equals(phoneModel) ||
                "GT-I9100G".equals(phoneModel) ||
                "Lenovo K860".equals(phoneModel)){
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public static int getImageTypeFromName(String filePath){
        int index = filePath.lastIndexOf(".");
        if (index > 0) {
            String fileName = filePath.substring(index).toLowerCase();
            if (fileName.equals(".jpg") || fileName.equals(".jpeg") || fileName.equals(".JPEG") || fileName.equalsIgnoreCase(".bmp")) {
                return 1;
            }else if(fileName.equals(".png")){
                return 2;
            }
        }
        return 0;
    }

    //如果目录不存在，则创建目录
    public static void createPathDir(String newPath){
        if (newPath != null) {
            File newfile = new File(newPath);
            if (!newfile.getParentFile().exists()) {
                newfile.getParentFile().mkdirs();
                newfile.getParentFile().setWritable(true);
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String GetSDCardPath(Context context) {
        File sdDir = null;
        String InstallPath = null;
        StringBuilder stringBuilder = new StringBuilder();
        @SuppressWarnings("unused")
        int i = 0;

        ReadWriteFile readWriteFile = new ReadWriteFile(context);
        String pathString = context.getFilesDir().getAbsolutePath();
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        //没有sd卡，搜索其他挂载的目录
        String otherPath = null;
        if(!sdCardExist){
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            try {
                String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class[0]).invoke(sm, new Object[0]);
                for(String path : paths){
                    StatFs fs = new StatFs(path);
                    int blocks = fs.getAvailableBlocks();
                    if(blocks > 0){
                        otherPath = path;
                        break;
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            sdDir = Environment.getExternalStorageDirectory();// ??????????????????
            // 为了不让图片搜索软件可以看到应用的图片，我们把目录建立在android目录下
            stringBuilder.append(sdDir.toString());
        }

        if(null != otherPath){
            sdCardExist = true;
            stringBuilder.append(otherPath);
        }

        if (readWriteFile.fileIsExists(pathString, "firstInstallPath.txt")) {
            InstallPath = readWriteFile
                    .readFileFromAppData("firstInstallPath.txt");

            return InstallPath;
        } else {
            if (sdCardExist) {
                stringBuilder.append("/Android/data");
                File file = new File(stringBuilder.toString());
                if (file.exists() && file.isDirectory()) {
                    InstallPath = stringBuilder.toString();
                } else {
                    file.mkdirs();
                    InstallPath = stringBuilder.toString();
                }
            } else {
                Log.i(TAG, "pathString2" + pathString);
                InstallPath = pathString;
            }
            readWriteFile.writeFileToAppData("firstInstallPath.txt",
                    InstallPath);

            return InstallPath;
        }
    }


}
