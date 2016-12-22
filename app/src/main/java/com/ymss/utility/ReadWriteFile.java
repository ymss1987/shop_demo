package com.ymss.utility;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


public class ReadWriteFile {
	 private static final String TAG = LogUtil.makeLogTag(ReadWriteFile.class);
	 public File file;
	 private Context context;  
     public ReadWriteFile(Context context) {  
        this.context = context;  
    }  
/*
 * String Name = File.getName();  //获得文件或文件夹的名称：
String parentPath = File.getParent();  获得文件或文件夹的父目录
String path = File.getAbsoultePath();//绝对路经
String path = File.getPath();//相对路经
File.createNewFile();//建立文件 
File.mkDir(); //建立文件夹 
File.isDirectory(); //判断是文件或文件夹
File[] files = File.listFiles();  //列出文件夹下的所有文件和文件夹名
File.renameTo(dest);  //修改文件夹和文件名
File.delete();  //删除文件夹或文件
*/
/**
 * 从resource中的raw文件夹中获取文件并读取数据（资源文件只能读不能写）
 */
	/*
public String ReadFromRaw(){
	String strData = ""; 
	
    try{ 
        InputStream in = getResources().openRawResource("R.raw.bbi"); 

        //在\Test\res\raw\bbi.txt,
        int length = in.available();       
        byte [] buffer = new byte[length];        
        in.read(buffer);         
        //strData = EncodingUtils.getString(buffer, "UTF-8");
        //strData = EncodingUtils.getString(buffer, "UNICODE"); 
        strData = EncodingUtils.getString(buffer, "BIG5"); 
        //依bbi.txt的编码类型选择合适的编码，如果不调整会乱码
        in.close();            
    }catch(Exception e){ 
        e.printStackTrace();         
    }
    return strData;
}*/
/**
 * 从asset中获取文件并读取数据（资源文件只能读不能写）
 * @param fileName
 * @param message
 */
/*
public String ReadFromAsset(String FileName){
    String strData = ""; 
    
    try{ 
        InputStream in = getResources().getAssets().open(FileName);
        //在\Test\res\raw\bbi.txt,
        int length = in.available();       
        byte [] buffer = new byte[length];        
        in.read(buffer);         
        //strData = EncodingUtils.getString(buffer, "UTF-8");
        //strData = EncodingUtils.getString(buffer, "UNICODE"); 
        strData = EncodingUtils.getString(buffer, "BIG5"); 
        //依bbi.txt的编码类型选择合适的编码，如果不调整会乱码
        in.close();            
    }catch(Exception e){ 
        e.printStackTrace();         
    } 
    return strData;
}*/
    /**
     * 写/data/data/<应用程序名>目录上的文件:
     * @param fileName
     */
    public void writeFileToAppData(String fileName,String path){ 
    	
        try{ 
        	File newfile = new File(path);

			if (!newfile.getParentFile().exists()) {
				newfile.getParentFile().mkdirs();
			}
			
			int status;
			//Process p = Runtime.getRuntime().exec("chmod -R 0777 " +  newfile);
           // status = p.waitFor();  
//            if (status == 0) {   
//                Log.e("TAG", "status == 0");
//            } else {   
//            	 Log.e("TAG", "status == 1");
//            } 
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
           Log.e(TAG, "fileName"+fileName);
            byte [] bytes = path.getBytes(); 
            fileOutputStream.write(bytes); 
            fileOutputStream.close(); 
        } 
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
    }

    /**
     * 写/data/data/<应用程序名>目录上的文件:
     * @param fileName
     */
    public void writeFileToMsiFile(String fileName,String path,String data){ 
    	
        try{ 
        	File newfile = new File(path);

			if (!newfile.getParentFile().exists()) {
				newfile.getParentFile().mkdirs();
			}
			
			int status;
			Process p = Runtime.getRuntime().exec("chmod -R 0777 " +  newfile);
            status = p.waitFor();  
            if (status == 0) {   
                Log.e("TAG", "status == 0");
            } else {   
            	 Log.e("TAG", "status == 1");
            } 
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
           Log.e(TAG, "fileName"+fileName);
            byte [] bytes = data.getBytes(); 
            fileOutputStream.write(bytes); 
            fileOutputStream.close(); 
        } 
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
    }
    /**
     * 读/data/data/<应用程序名>目录上的文件:
     * @param fileName
     * @return
     */
    public String readFileFromAppData(String fileName){
        String strData=""; 

        try{ 
            FileInputStream fileInputStream = context.openFileInput(fileName);
            int length = fileInputStream.available(); 
            byte [] buffer = new byte[length]; 
            fileInputStream.read(buffer);     
            strData = EncodingUtils.getString(buffer, "UTF-8"); 
            fileInputStream.close();     
        } 
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
        return strData; 
    }
    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean fileIsExists(String filePath, String fileName){
        try{
            File f=new File(filePath, fileName);
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * 写入SD卡上的文件
     * @param fileName
     * @param write_str
     */
    public void writeFileSdcardFile(String fileName,String write_str){
        String sdStatus = Environment.getExternalStorageState();
        Log.e(TAG, "writeFileSdcardFile");
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
    	 try{
    		 Log.e(TAG, "FileOutputStream");
    	       FileOutputStream fout = new FileOutputStream(fileName);
    	       Log.e(TAG, "fout"+fout);
    	       byte [] bytes = write_str.getBytes();
    	       fout.write(bytes);
    	       Log.e(TAG, "writeFileSdcardFile1");
    	       fout.close();
    	     }
    	      catch(Exception e){
    	        e.printStackTrace();
    	       }
    	   }
    /**
     * 读SD中的文件
     * @param fileName
     * @return
     */
	public String readFileSdcardFile(String fileName){
	  String res="";
      String sdStatus = Environment.getExternalStorageState();
      if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
          Log.d("TestFile", "SD card is not avaiable/writeable right now.");
          return null;
      }
	  try{
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);    
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();    
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
	}
	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
	    boolean flag = false;
	    file = new File(sPath);
	    // 路径为文件且不为空则进行删除
	    if (file.isFile() && file.exists()) {
	        file.delete();
	        flag = true;
	    }
	    return flag;
	}
	/**
	 *  根据路径删除指定的目录或文件，无论存在与否
	 *@param sPath  要删除的目录或文件
	 *@return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String sPath) {
	    boolean flag = false;
	    file = new File(sPath);
	    // 判断目录或文件是否存在
	    if (!file.exists()) {  // 不存在返回 false
	        return flag;
	    } else {
	        // 判断是否为文件
	        if (file.isFile()) {  // 为文件时调用删除文件方法
	            return deleteFile(sPath);
	        } else {  // 为目录时调用删除目录方法
	            return deleteDirectory(sPath);
	        }
	    }
	}
	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符
	    if (!sPath.endsWith(File.separator)) {
	        sPath = sPath + File.separator;
	    }
	    File dirFile = new File(sPath);
	    //如果dir对应的文件不存在，或者不是一个目录，则退出
	    if (!dirFile.exists() || !dirFile.isDirectory()) {
	        return false;
	    }
	    boolean flag = true;
	    //删除文件夹下的所有文件(包括子目录)
	    File[] files = dirFile.listFiles();
	    for (int i = 0; i < files.length; i++) {
	        //删除子文件
	        if (files[i].isFile()) {
	            flag = deleteFile(files[i].getAbsolutePath());
	            if (!flag) break;
	        } //删除子目录
	        else {
	            flag = deleteDirectory(files[i].getAbsolutePath());
	            if (!flag) break;
	        }
	    }
	    if (!flag) return false;
	    //删除当前目录
	    if (dirFile.delete()) {
	        return true;
	    } else {
	        return false;
	    }
	}
	

		//读取配置文件 
	public static Properties loadConfig(Context context, String file) {
		Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return properties;
	}
		//保存配置文件
	public static boolean saveConfig(Context context, String file, Properties properties) {
		try {
			File fil=new File(file);
			if(!fil.exists())
			fil.createNewFile();
			FileOutputStream s = new FileOutputStream(fil);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
	public static void DeleteLogFile(String DirectPath){
		final String path = InnerTools.getResourceAbsolutePath(DirectPath);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				long millionSeconds;
				long currentSeconds;
				long sevenDaySes = 604800;
				currentSeconds = System.currentTimeMillis()/1000;
				File file = new File(path);
		        if (file == null) {
					return;
				}
		        
		        File[] files = file.listFiles();
		        if (files == null) {
					return;
				}

		        for (int i = 0; i < files.length; i++) {
		        	final File fileListPath = files[i];
					final String fileName = fileListPath.getName();
					int index = fileName.lastIndexOf(".");
					String tempPath = fileName.substring(0, index);
					String pathString = tempPath.replace("-", "");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
					try {
						millionSeconds = sdf.parse(pathString).getTime()/1000;
						if (currentSeconds - millionSeconds > sevenDaySes) {
							fileListPath.delete();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
			}
		});
		thread.start();
	}*/
}
