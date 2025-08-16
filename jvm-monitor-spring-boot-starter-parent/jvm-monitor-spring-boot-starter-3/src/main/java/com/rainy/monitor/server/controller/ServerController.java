package com.rainy.monitor.server.controller;

import com.rainy.monitor.server.service.ServerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 服务器监控
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/orion/api/monitor/server")
public class ServerController {

    @GetMapping()
    public HashMap getInfo() throws Exception {
        ServerService server = new ServerService();
        server.copyTo();
        HashMap<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "");
        res.put("data", server);
        return res;
    }
}
