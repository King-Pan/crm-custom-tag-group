package com.asiainfo;

import com.asiainfo.tag.utils.MD5RowKeyGenerator;
import org.junit.Test;

/**
 * @author king-pan
 * @date 2018/12/10
 * @Description ${DESCRIPTION}
 */
public class RowKeyTest {

    private MD5RowKeyGenerator md5RowKeyGenerator = new MD5RowKeyGenerator();

    @Test
    public void test(){
        String tel = "13971334853";
        String groupid = "KHQ99900026766";
        String rowKey = md5RowKeyGenerator.generatePrefix(tel) + tel + "|" + groupid;
        System.out.println(rowKey);
        //01513971334853|KHQ99900026766
        //01513971334853|00026767

        String groupId = String.format("%08d", Long.valueOf(groupid.substring(6, groupid.length())) + 1);
        StringBuilder sb = new StringBuilder();
        sb.append(md5RowKeyGenerator.generatePrefix(tel)).append(tel).append("|");
        sb.append(groupid.substring(0, 6)).append(groupId);
        System.out.println(sb.toString());
    }
}
