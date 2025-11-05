package com.inhyuk.mcpproxy;

import com.inhyuk.mcpproxy.entity.McpServerRepository;
import com.inhyuk.mcpproxy.mcp.McpClientFactory;
import com.inhyuk.mcpproxy.mcp.McpClientService;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mcp/servers")
@RequiredArgsConstructor
public class McpServerController {

    private final McpClientFactory mcpClientFactory;
    private final McpClientService mcpClientService;
    private final McpServerRepository mcpServerRepository;

    @PostMapping("")
    public ResponseEntity addServer(){

        return ResponseEntity.ok(mcpClientFactory.createStreamClient("http://localhost:8090","/stream"));
    }

    @GetMapping("")
    public ResponseEntity getServers(){

        return ResponseEntity.ok(mcpServerRepository.findAll());
    }


}
