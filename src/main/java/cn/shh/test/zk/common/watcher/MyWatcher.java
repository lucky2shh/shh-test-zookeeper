package cn.shh.test.zk.common.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.proto.WatcherEvent;
import org.springframework.stereotype.Component;

/**
 * KeeperState：
 *      case -1: KeeperState.Unknown;
 *      case 0: KeeperState.Disconnected;
 *      case 1: KeeperState.NoSyncConnected;
 *      case 3: KeeperState.SyncConnected;
 *      case 4: KeeperState.AuthFailed;
 *      case 5: KeeperState.ConnectedReadOnly;
 *      case 6: KeeperState.SaslAuthenticated;
 *      case 7: KeeperState.Closed;
 *      case -112: KeeperState.Expired;
 *
 * EventType：
 *      None(-1),
 *      NodeCreated(1),
 *      NodeDeleted(2),
 *      NodeDataChanged(3),
 *      NodeChildrenChanged(4),
 *      DataWatchRemoved(5),
 *      ChildWatchRemoved(6),
 *      PersistentWatchRemoved (7);
 */
@Slf4j
@Component
public class MyWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
        log.info("================ 事件通知开始 ===============");

        String eventPath = event.getPath();
        Event.KeeperState eventState = event.getState();
        Event.EventType eventType = event.getType();
        WatcherEvent eventWrapper = event.getWrapper();
        log.info("事件路径：【{}】，事件状态：【{}】，事件类型：【{}】，事件包装：【{}】。", eventPath, eventState, eventType, eventWrapper);

        // 事件状态
        if (eventState == Event.KeeperState.Unknown){                   // -1
            log.info("Event.KeeperState.Unknown");
        }else if (eventState == Event.KeeperState.Disconnected){        // 0
            log.info("Event.KeeperState.Disconnected");
        }else if (eventState == Event.KeeperState.NoSyncConnected){     // 1
            log.info("Event.KeeperState.NoSyncConnected");
        }else if (eventState == Event.KeeperState.SyncConnected){       // 3
            log.info("Event.KeeperState.SyncConnected");
        }else if (eventState == Event.KeeperState.AuthFailed){          // 4
            log.info("Event.KeeperState.AuthFailed");
        }else if (eventState == Event.KeeperState.ConnectedReadOnly){   // 5
            log.info("Event.KeeperState.ConnectedReadOnly");
        }else if (eventState == Event.KeeperState.SaslAuthenticated){   // 6
            log.info("Event.KeeperState.SaslAuthenticated");
        }else if (eventState == Event.KeeperState.Closed){              // 7
            log.info("Event.KeeperState.Closed");
        }else if (eventState == Event.KeeperState.Expired){             // -112
            log.info("Event.KeeperState.Expired");
        }

        // 事件类型
        if(Event.EventType.None == eventType){                  // -1
            log.info("连接成功！");
        }else if(Event.EventType.NodeCreated == eventType){     // 1
            log.info("节点【{}】新增成功！", eventPath);
        }else if(Event.EventType.NodeDeleted == eventType){     // 2
            log.info("节点【{}】删除成功！", eventPath);
        }else if(Event.EventType.NodeDataChanged == eventType){ // 3
            log.info("节点【{}】数据修改成功！", eventPath);
        }else if(Event.EventType.NodeDataChanged == eventType){ // 4
            log.info("节点【{}】子节点修改成功！", eventPath);
        }else if(Event.EventType.NodeDataChanged == eventType){ // 5
            log.info("数据事件删除成功！");
        }else if(Event.EventType.NodeDataChanged == eventType){ // 6
            log.info("子节点事件删除成功！");
        }else if(Event.EventType.NodeDataChanged == eventType){ // 7
            log.info("PersistentWatchRemoved成功！");
        }

        log.info("================ 事件通知结束 ===============");
    }
}
