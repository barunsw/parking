package com.hyundai_mnsoft.vpp.biz.http.service;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class CronService {
    private static Logger LOGGER = Logger.getLogger(CronService.class);

//    @Autowired
//    ControlServerService controlServerService;

    private final long WORK_INTERVAL = 60000;

//    @Scheduled(fixedRate = WORK_INTERVAL) // 1분마다 실행
//    public void getData() {
//        controlServerService.reloadLaneInfoStatus();
//        controlServerService.reloadParkingUse();
//    }
}
