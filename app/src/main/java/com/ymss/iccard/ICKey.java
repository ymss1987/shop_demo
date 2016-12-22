package com.ymss.iccard;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class ICKey {
	
	// 算法名称/加密模式/填充方式
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
	static String DES = "DES/ECB/NoPadding";  
    static String TriDes = "DESede/ECB/NoPadding";
	/**
	 * DES算法，加密
	 * 
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws InvalidAlgorithmParameterException
	 * @throws Exception
	 */
	public static byte[] desEncode(byte[] key, byte[] data) {
		try {  
            KeySpec ks = new DESKeySpec(key);  
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");  
            SecretKey ky = kf.generateSecret(ks);  
  
            Cipher c = Cipher.getInstance(DES);  
            c.init(Cipher.ENCRYPT_MODE, ky);  
            return c.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
	}

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish
	
	 /**
     *   3DES加密                                                
     * @param keybyte  加密密钥
     * @param src     字节数组(根据给定的字节数组构造一个密钥。 )
     * @return
     */
    public static byte[] encryptMode(byte key[], byte data[]) { 
	    try {  
	        byte[] k = new byte[24];  
	        
	        int len = data.length;  
	        if(data.length % 8 != 0){  
	            len = data.length - data.length % 8 + 8;  
	        }  
	        byte [] needData = null;  
	        if(len != 0)  
	            needData = new byte[len];  
	          
	        for(int i = 0 ; i< len ; i++){  
	            needData[i] = 0x00;  
	        }  
	          
	        System.arraycopy(data, 0, needData, 0, data.length);  
	          
	        if (key.length == 16) {  
	            System.arraycopy(key, 0, k, 0, key.length);  
	            System.arraycopy(key, 0, k, 16, 8);  
	        } else {  
	            System.arraycopy(key, 0, k, 0, 24);  
	        }  
	
	        KeySpec ks = new DESedeKeySpec(k);  
	        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");  
	        SecretKey ky = kf.generateSecret(ks);  
	
	        Cipher c = Cipher.getInstance(TriDes);  
	        c.init(Cipher.ENCRYPT_MODE, ky);  
	        return c.doFinal(needData);  
	   } catch (Exception e) {  
       e.printStackTrace();  
       return null;  
    }  }
 
    /**
     * 3DES解密
     * @param keybyte 密钥
     * @param src       需要解密的数据
     * @return
     */
    public static byte[] decryptMode(byte key[], byte data[]) {
	    try {  
	        byte[] k = new byte[24];  
	        
	        int len = data.length;  
	        if(data.length % 8 != 0){  
	            len = data.length - data.length % 8 + 8;  
	        }  
	        byte [] needData = null;  
	        if(len != 0)  
	            needData = new byte[len];  
	          
	        for(int i = 0 ; i< len ; i++){  
	            needData[i] = 0x00;  
	        }  
	          
	        System.arraycopy(data, 0, needData, 0, data.length);  
	          
	        if (key.length == 16) {  
	            System.arraycopy(key, 0, k, 0, key.length);  
	            System.arraycopy(key, 0, k, 16, 8);  
	        } else {  
	            System.arraycopy(key, 0, k, 0, 24);  
	        }  
	        KeySpec ks = new DESedeKeySpec(k);  
	        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");  
	        SecretKey ky = kf.generateSecret(ks);  
	
	        Cipher c = Cipher.getInstance(TriDes);  
	        c.init(Cipher.DECRYPT_MODE, ky);  
	        return c.doFinal(needData);  
	   } catch (Exception e) {  
       e.printStackTrace();  
       return null;  
       }
	  }
}
