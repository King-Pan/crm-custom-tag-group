package com.asiainfo.cmcperson;

import com.asiainfo.dacp.sdk.HttpHelper;
import com.asiainfo.tag.utils.FastJsonUtils;
import com.asiainfo.tag.utils.MD5RowKeyGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/10
 * Time: 下午3:37
 * Description: No Description
 */
public class TestSensitive {

    private static String restUrl = "http://10.31.100.19/dataos/open/datams/sign/sensitiveTranslate.json";
    //替换成自己团队对应key值
    private final static String key = "8287qafva1oj387";
    //替换成自己的团队编码
    private final static String user = "T0000";

    @Test
    public void testSensitive() throws Exception {
        //String phoneNo="22839841306";
        Map<String, Object> crypt = new HashMap<String, Object>();
        crypt.put("tenantId", "T0000");
        crypt.put("dsId", "hive2");
        //aesdecrypt 解密 aesencrypt 加密
        crypt.put("operType", "aesdecrypt");
        crypt.put("defineId", "certificateNo");
        List<String> phonelist = new ArrayList<String>();

        //dacp::ZFNFt+8qOsoEsoh9ZHOAqExOvoBbFaocv43bcpUenmQ=
        //dacp::ZFNFt+8qOsoEsoh9ZHOAqExOvoBbFaocv43bcpUenmQ=
        //421281199806030014

        //420921198003125139
        //dacp::ZSAZLnWBjfupyPzidHnZn7ObCVocwbK/YnmlqFyAEAU=
        phonelist.add("dacp::kYacaklI7emGwdjiaFd1UV5c2SzZaT3sUU98WadS0SA=");

        crypt.put("content", phonelist);
        String result = HttpHelper.post(user, key, restUrl, crypt);
        System.out.println(result);
    }

    @Test
    public void testSensitive2() throws Exception {
        //String phoneNo="22839841306";
        Map<String, Object> crypt = new HashMap<String, Object>();
        crypt.put("tenantId", "T0000");
        crypt.put("dsId", "hive2");
        //aesdecrypt 解密 aesencrypt 加密
        crypt.put("operType", "aesencrypt");
        crypt.put("defineId", "certificateNo");
        List<String> phonelist = new ArrayList<String>();

        //dacp::ZFNFt+8qOsoEsoh9ZHOAqExOvoBbFaocv43bcpUenmQ=
        //dacp::ZFNFt+8qOsoEsoh9ZHOAqExOvoBbFaocv43bcpUenmQ=
        //421281199806030014

        //420921198003125139
        //dacp::ZSAZLnWBjfupyPzidHnZn7ObCVocwbK/YnmlqFyAEAU=
        phonelist.add("420222198201135424");

        crypt.put("content", phonelist);
        String result = HttpHelper.post(user, key, restUrl, crypt);
        System.out.println(result);
        // 000dacp::+9lSlZpHc+Lw2rMfvfJBEJWzkXk/c1xs3Y+9GuhybfM=
        String content = FastJsonUtils.getJsonObject(result).getString("toContent");
        System.out.println(MD5RowKeyGenerator.generatePrefix(MD5RowKeyGenerator.getMD5(content)));
    }
}
