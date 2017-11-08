package com.pangpang.endpoint;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

/**
 * Created by leewake on 2017/9/11 0011.
 */

//自定义endpoint，进行内存监控
@Configuration
@EnableAutoConfiguration
public class CustomizeEndPoint {

    /**
     * REFERENCE http://ipm.lvmama.com/index.php?m=task&f=view&task=68146
     * 其实不需要加这个静态内部类，加的话，其实应单独写成一个config类
     */
    public static class EndPointAutoConfig {

        private List<MemStatus> status = new ArrayList<MemStatus>();

        @Bean
        public MyEndPoint myEndPoint() {
            return new MyEndPoint(status);
        }

        @Bean
        public MemCollector memCollector() {
            return new MemCollector(status);
        }
    }

    public static class MemStatus {

        public MemStatus(Date date, Map<String, Object> status) {
            this.date = date;
            this.status = status;
        }

        private Date date;

        private Map<String, Object> status;

        public Date getDate() {
            return date;
        }

        public Map<String, Object> getStatus() {
            return status;
        }

    }

    public static class MemCollector {

        private int maxSize = 5;

        private List<MemStatus> status;

        public MemCollector(List<MemStatus> status) {
            this.status = status;
        }

        @Scheduled(cron = "0/5 * * * * ? ")
        public void collect() {
            Runtime runtime = Runtime.getRuntime();
            Long maxMemory = runtime.maxMemory();
            Long totalMemory = runtime.totalMemory();
            Map<String, Object> memoryMap = new HashMap<>(2, 1);
            Date date = Calendar.getInstance().getTime();
            memoryMap.put("maxMemory", maxMemory);
            memoryMap.put("totalMemory", totalMemory);
            if (status.size() > maxSize) {
                status.remove(0);
                status.add(new MemStatus(date, memoryMap));
            } else {
                status.add(new MemStatus(date, memoryMap));
            }
        }
    }

    public static class MyEndPoint extends AbstractEndpoint implements Endpoint {

        private List<MemStatus> status;

        public MyEndPoint(List<MemStatus> status) {
            super("my",false);
            this.status = status;
        }

        public Object invoke() {
            if (status == null || status.isEmpty()) {
                return "hello world";
            }
            Map<String, List<Map<String, Object>>> result = new HashMap<>();
            for (MemStatus memStatus : status) {
                for (Map.Entry<String, Object> entry : memStatus.status.entrySet()) {
                    List<Map<String, Object>> collectList = result.get(entry.getKey());
                    if (collectList == null) {
                        collectList = new LinkedList<>();
                        result.put(entry.getKey(), collectList);
                    }
                    Map<String, Object> soloCollect = new HashMap<>();
                    soloCollect.put("date", memStatus.getDate());
                    soloCollect.put(entry.getKey(), entry.getValue());
                    collectList.add(soloCollect);
                }
            }
            return result;
        }
    }
}
