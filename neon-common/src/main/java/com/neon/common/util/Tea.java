package com.neon.common.util;

import org.apache.commons.codec.binary.Base64;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Tea算法 每次操作可以处理8个字节数据 KEY为16字节,应为包含4个int型数的int[]，一个int为4个字节
 * 加密解密轮数应为8的倍数，推荐加密轮数为64轮
 */
public class Tea {

    private static final int endelta = 0x9E3779B9; // 这是算法标准给的值
    private static final int dedelta = 0xC6EF3720; // 这是算法标准给的值
    private static final Logger logger = LoggerFactory.getLogger(Tea.class);

    static int[] appkey = new int[4];

    static {
        appkey[0] = 0xFAA5;//64933
        appkey[1] = 0xD5BE;//54606
        appkey[2] = 0xFC0A;//64512
        appkey[3] = 0xB59A;//46426
    }

//    public static void main(String[] args) throws IOException {
//        logger.info(encryptForAppKey("300492"));
//        logger.info(encryptForAppKey("301869"));
//        logger.info(decryptForAppKey("pXl\\/JvxFGWc="));
//        //logger.info(JSONObject.toJSONString(appkey));
//        //logger.info(encryptForAppKey(JacksonUtil.BeanToJson(new UserToken("ff2296ca4f8ace795ac71335a634d594", "channel_id", "", System.currentTimeMillis()))));
//        //logger.info(encryptForAppKey("{\"code\":0,\"msg\":\"ok\",\"data\":{\"name\":\"极简TV\",\"description\":\"极简TV\",\"phone\":\"联系电话\",\"email\":\"123456@qq.com\",\"wechat\":\"客服微信\",\"app_update\":{\"version_code\":\"2\",\"update_url\":\"http://www.baidu.com\",\"title\":\"title\",\"content\":\"测试更新Android\",\"is_forced\":0}}}"));
//        //logger.info(decryptForAppKey("+cDHfuEDMgmUOafmo5xRTpf/HXXlW2Qh5mvAW3Jtl28yoT140Zqsgsj+zvzVBkmonPkEpo4OBhBbtKSnboo7OZpOzg4m0Md/ftzMATUm4K9NDhZVoYzeSBEE+WbOwWovpQa/35o6tfVhKR6K7SJSNh4SmtwsAbj4RUVUxB5BbEBWRPyGXWp5cLb+6EdkomwT3lTcbBmNPFU="));
//        //for(int i=101064;i<102086;i++){
//        //logger.info(URLEncoder.encode(encryptForAppKey(System.currentTimeMillis()+"|"+i)));
//        //}
//    }

//	public static String generatorKey(){
//		String key = PropertiesUtil.getProperty("app.key");
//		String[] keys = key.split("\\|");
//		logger.info(Arrays.toString(keys));
//		StringBuffer sb = new StringBuffer();
//		for (int i=0;i<keys.length;i++) {
//			appkey[i] = Integer.parseInt(keys[i],16);
////			logger.info("使用Key---"+keys[i]+"temp:"+appkey[i]);
//			keys[i]=Integer.toHexString(new Random().nextInt(8999)+10000).toUpperCase()+keys[i];
//			sb.append(keys[i]);
//		}
////		logger.info("随机key--->"+sb.toString());
////		logger.info(Arrays.toString(keys));
////		logger.info("随机keyint--->"+Arrays.toString(appkey));
//		return sb.toString();
////		StringBuffer sb = new StringBuffer();
////		for (int i = 0; i < 8; i++) {
////			int temp = new Random().nextInt(8999)+10000;
////			if(i%2==1){
////				appkey[i%2] = temp;
////				logger.info("使用Key----i->"+i+",key"+i%1 +",temp:"+temp+"->"+Integer.toHexString(temp));
////			}
////			logger.info("i->"+i+",key"+i%1 +",temp:"+temp+"->"+Integer.toHexString(temp));
////			sb.append(Integer.toHexString(temp));
////		}
////		String mi = encryptForAppKey("hhhhhhhhh");
////		logger.info("加密后->"+mi);
////		for(int i=0;i<4;i++){
////			
////		}
////		logger.info("解密后->"+decryptForAppKey(mi));
////		logger.info(Arrays.toString(appkey));
////		logger.info(sb.toString());
////		return sb.toString();
//	}

    //加密
    private static byte[] encrypt(byte[] content, int offset, int[] key, int times) {//times为加密轮数
        int[] tempInt = byteToInt(content, offset);
        for (int j = 0; j < tempInt.length; j += 2) {
            int y = tempInt[j], z = tempInt[j + 1], sum = 0, i;
            int a = key[0], b = key[1], c = key[2], d = key[3];

            for (i = 0; i < times; i++) {
                sum += endelta;
                y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
                z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
            }
            tempInt[j] = y;
            tempInt[j + 1] = z;
        }

        return intToByte(tempInt, 0);
    }

    //解密
    private static byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times) {
        int[] tempInt = byteToInt(encryptContent, offset);
        for (int j = 0; j < tempInt.length; j += 2) {
            int y = tempInt[j], z = tempInt[j + 1], sum = dedelta, i;
            int a = key[0], b = key[1], c = key[2], d = key[3];

            for (i = 0; i < times; i++) {
                z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
                y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
                sum -= endelta;
            }
            tempInt[j] = y;
            tempInt[j + 1] = z;
        }

        return intToByte(tempInt, 0);
    }

    // byte[]型数据转成int[]型数据
    private static int[] byteToInt(byte[] content, int offset) {

        int[] result = new int[content.length >> 2]; // 除以2的n次方 == 右移n位 即
        // content.length / 4 ==
        // content.length >> 2
        for (int i = 0, j = offset; j < content.length; i++, j += 4) {
            result[i] = transform(content[j + 3]) | transform(content[j + 2]) << 8 | transform(content[j + 1]) << 16
                    | (int) content[j] << 24;
        }
        return result;

    }

    // int[]型数据转成byte[]型数据
    private static byte[] intToByte(int[] content, int offset) {
        byte[] result = new byte[content.length << 2]; // 乘以2的n次方 == 左移n位 即
        // content.length * 4 ==
        // content.length << 2
        for (int i = 0, j = offset; j < result.length; i++, j += 4) {
            result[j + 3] = (byte) (content[i] & 0xff);
            result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
            result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
            result[j] = (byte) ((content[i] >> 24) & 0xff);
        }
        return result;
    }

    // 若某字节被解释成负的则需将其转成无符号正数
    private static int transform(byte temp) {
        int tempInt = (int) temp;
        if (tempInt < 0) {
            tempInt += 256;
        }
        return tempInt;
    }

    /**
     * 通过TEA算法加密信息
     *
     * @param info
     * @param key
     * @return
     */
    public static byte[] encryptByTea(String info, int[] key) throws UnsupportedEncodingException {
        byte[] temp = info.getBytes("UTF-8");// "UTF-8");
        int n = 8 - temp.length % 8;// 若temp的位数不足8的倍数,需要填充的位数
        byte[] encryptStr = new byte[temp.length + n];
        encryptStr[0] = (byte) n;
        System.arraycopy(temp, 0, encryptStr, n, temp.length);
        byte[] result = new byte[encryptStr.length];
        byte[] tempEncrpt = Tea.encrypt(encryptStr, 0, key, 32);
//		logger.info("a " + encryptStr.length + " b" + tempEncrpt.length);
        System.arraycopy(tempEncrpt, 0, result, 0, encryptStr.length);
        return result;
    }

    /**
     * 通过TEA算法加密信息
     *
     * @param info
     * @param keys
     * @return
     */
    public static String encryptByTea(String info, String keys) throws UnsupportedEncodingException {
        int[] key = initKey(keys);
        byte[] bytes = encryptByTea(info, key);
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 通过TEA算法解密信息
     *
     * @param msg
     * @param keys
     * @return
     */
    public static String decryptByTea(String msg, String keys) {
        byte[] secretInfo = Base64.decodeBase64(msg);
        int[] key = initKey(keys);
        try {
            return decryptByTea(secretInfo, key);
        } catch (UnsupportedEncodingException e) {
//			logger.error("{}",e);
        }
        return "";

    }

    private static int[] initKey(String keys) {
        int[] key = new int[4];
        key[0] = Integer.parseInt(keys.substring(0, 4), 16);
        key[1] = Integer.parseInt(keys.substring(4, 8), 16);
        key[2] = Integer.parseInt(keys.substring(8, 12), 16);
        key[3] = Integer.parseInt(keys.substring(12, 16), 16);
        return key;
    }

    public static String encryptForAppKey(String info) throws UnsupportedEncodingException {
        byte[] bytes = encryptByTea(info, appkey);
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 通过TEA算法解密信息
     *
     * @param secretInfo
     * @param key
     * @return
     */
    public static String decryptByTea(byte[] secretInfo, int[] key) throws UnsupportedEncodingException {
        byte[] decryptStr = null;
        byte[] tempDecrypt = new byte[secretInfo.length];
        decryptStr = Tea.decrypt(secretInfo, 0, key, 32);
        System.arraycopy(decryptStr, 0, tempDecrypt, 0, decryptStr.length);
        int n = tempDecrypt[0];
        return new String(tempDecrypt, n, decryptStr.length - n, "UTF-8");
    }

    public static String decryptForAppKey(String msg) throws IOException {
        byte[] secretInfo = Base64.decodeBase64(msg);
        return decryptByTea(secretInfo, appkey);
    }

}