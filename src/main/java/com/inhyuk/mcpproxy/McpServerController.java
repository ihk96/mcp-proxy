package com.inhyuk.mcpproxy;

import com.inhyuk.mcpproxy.entity.McpServer;
import com.inhyuk.mcpproxy.entity.McpServerRepository;
import com.inhyuk.mcpproxy.mcp.McpClientFactory;
import com.inhyuk.mcpproxy.mcp.McpClientService;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mcp/servers")
@RequiredArgsConstructor
public class McpServerController {

    private final McpServerService mcpServerService;

    @PostMapping("")
    public ResponseEntity addServer(@RequestBody AddRequestBody requestBody){
        McpServer server = mcpServerService.add(requestBody.name, requestBody.baseUrl, requestBody.endpoint, requestBody.transportType);

        return ResponseEntity.ok(server);
    }

    @Data
    @NoArgsConstructor
    public static class AddRequestBody {
        private String name;
        private String baseUrl;
        private String endpoint;
        private McpServer.TransportType transportType;
    }

    @GetMapping("")
    public ResponseEntity getServers(){

        return ResponseEntity.ok(mcpServerService.getAll());
    }


}
