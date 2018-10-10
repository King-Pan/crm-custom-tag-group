package com.asiainfo;

import com.asiainfo.tag.utils.FastJsonUtils;
import com.asiainfo.tag.utils.MD5RowKeyGenerator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/28
 * Time: 下午7:36
 * Description: No Description
 */
public class FastJsonTest {

    @Test
    public void test() {
        Map<String, String> rt = new HashMap<>(2);
        rt.put("retcode", "-2");
        rt.put("errmsg", "检查号码：[]是否在客户群中失败: ");
        System.out.println(FastJsonUtils.getBeanToJson(rt));
    }

    @Test
    public void test2(){
        MD5RowKeyGenerator md5RowKeyGenerator = new MD5RowKeyGenerator();
        System.out.println(md5RowKeyGenerator.generatePrefix("13098445304"));
    }
}
