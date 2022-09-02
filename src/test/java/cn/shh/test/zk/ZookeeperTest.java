package cn.shh.test.zk;

import cn.shh.test.zk.common.config.ZKProperties;
import cn.shh.test.zk.controller.ZookeeperController;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ZookeeperTest {
    @Autowired
    private ZKProperties zkProperties;
    @Autowired
    private ZookeeperController zookeeperController;

    @Test
    public void m1(){
        System.out.println(zkProperties.toString());
    }

    @Test
    public void createNode(){
        boolean res = zookeeperController.createNode("/path1/p1", "data1");
        System.out.println(res);
    }

    @Test
    public void updateNode(){
        boolean res = zookeeperController.updateNode("/path1", "data2");
        System.out.println(res);
    }

    @Test
    public void exists(){
        Stat stat = zookeeperController.exists("/path1/p1", true);
        System.out.println(stat.toString());
    }

    @Test
    public void exists2(){
        Stat stat = zookeeperController.exists("/path1/p1");
        System.out.println(stat.toString());
    }

    @Test
    public void getData(){
        String data = zookeeperController.getData("/path1");
        System.out.println(data);
    }

    @Test
    public void getChildren(){
        List<String> children = null;
        try {
            children = zookeeperController.getChildren("/path1", true);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        children.forEach(s -> System.out.println(s));
    }

    @Test
    public void getChildren2(){
        List<String> children = null;
        try {
            children = zookeeperController.getChildren2("/path1");
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        children.forEach(s -> System.out.println(s));
    }
}
