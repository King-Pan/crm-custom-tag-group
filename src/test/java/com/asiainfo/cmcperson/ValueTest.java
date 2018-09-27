package com.asiainfo.cmcperson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.utils.FastJsonUtils;
import com.asiainfo.tag.utils.MD5RowKeyGenerator;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/9
 * Time: 下午12:34
 * Description: No Description
 */
public class ValueTest {
    @Test
    public void test() {
        String value = "IdCard|dacp::2goJ2k9AqrsjIJ55tUAeTt1/NvHwDkEMTzpmuhTY3Ls=|22";
        String[] vls = value.split("\\|");
        System.out.println(Arrays.toString(vls));
    }


    @Test
    public void testMd5(){
        String str = "dacp::kYacaklI7emGwdjiaFd1UV5c2SzZaT3sUU98WadS0SA=";
        System.out.println(MD5RowKeyGenerator.generatePrefix(str));
    }


    @Test
    public void testJson() {
        String result = "{\"cryptKey\":[{\"dataDefineId\":\"phoneNo\",\"cryptFunc\":\"aesencrypt\",\"key\":\"J9oA3u6T1M/COf34J9Tik4Kc8C2j8UyI3c1V2hhGK5SpOSVzyVqpYtjR16dSuoil0rDOXMPBaK2N50gFXILDALtVM+eKCzYVuH/zYR0EE6Cq8Ex9sTUySQ7FgdRWJfkkXko6lp5kJkBa7Wrlnn2Y6KnRXKMyw8ySKlQLQjnZwaQ=\",\"expDt\":null}],\"success\":true}";
        JSONArray jsonArray = (JSONArray) FastJsonUtils.getJsonObject(result).get("cryptKey");
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println("加密字段:" +
                    jsonObject.getString("dataDefineId") + ",类型:" +
                    jsonObject.getString("cryptFunc") + ",key:" +
                    jsonObject.getString("key")
            );
        }
    }

    @Test
    public void testStr(){
        String str = "IdCard|dacp::Ze9VQoIwg/eSap0LEqE5MSqDFAz/BmUQV5R7JvB5sPo=|22o=|2";
        String[] vls = str.split("\\|");
        System.out.println(Arrays.toString(vls));
    }
}
