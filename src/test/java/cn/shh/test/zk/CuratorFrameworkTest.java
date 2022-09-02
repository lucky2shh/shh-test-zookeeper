package cn.shh.test.zk;

import cn.shh.test.zk.controller.CuratorController;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class CuratorFrameworkTest {
    @Autowired
    private CuratorController curatorController;

    @Test
    public void m1(){
        System.out.println(curatorController);
    }

    @Test
    public void createZnode(){
        System.out.println(curatorController.createZnode("/test-curator", "test"));
    }

    @Test
    public void createAsyncZnode(){
        System.out.println(curatorController.createAsyncZnode("/async-node", "data1"));
    }

    @Test
    public void selectZnode(){
        JSONObject jsonObject = curatorController.selectZnode("/test-curator");
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void selectChildrenZnode(){
        Map<String, String> znodes = curatorController.selectChildrenZnode("/test-curator");
        znodes.forEach((k, v) -> {
            System.out.println("k/v: " + k + "/" + v);
        });
    }

    @Test
    public void setdata(){
        JSONObject jo = curatorController.setData("/test-curator", "data1", 1);
        System.out.println(jo.toJSONString());
    }

    @Test
    public void delete(){
        JSONObject jo = curatorController.delete("/test-curator", 1, 0);
        System.out.println(jo.toJSONString());
    }

    @Test
    public void transactionDisabled(){
        String res = curatorController.transactionDisabled("/test-curator", "data1", "async-node", "data1");
        System.out.println(res);
    }

    @Test
    public void transactionEnabled(){
        String res = curatorController.transactionEnabled("/test-curator", "data1", "async-node", "data1");
        System.out.println(res);
    }

    @Test
    public void lock(){
        String res = null;
        try {
            res = curatorController.lock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(res);
    }

    @Test
    public void wlock(){
        String res = null;
        try {
            res = curatorController.wlock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(res);
    }

    @Test
    public void rlock(){
        String res = null;
        try {
            res = curatorController.rlock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(res);
    }
}
