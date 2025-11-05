package com.inhyuk.mcpproxy;

import io.modelcontextprotocol.client.McpSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mcp/client/")
@RequiredArgsConstructor
public class McpClientController {

    private final McpClientFactory mcpClientFactory;
    private final McpClientService mcpClientService;

    @PostMapping("/init")
    public ResponseEntity init(){
        McpSyncClient client1 = mcpClientFactory.createStreamClient("http://localhost:8090","/stream");
        McpSyncClient client2 = mcpClientFactory.createStreamClient("http://localhost:8091","/stream");
        mcpClientService.addClient(client1);
        mcpClientService.addClient(client2);
        mcpClientService.getClients().forEach(mcpSyncClient -> {
            System.out.println(mcpSyncClient.getServerInfo());
            System.out.println(mcpSyncClient.getClientInfo());
            System.out.println(mcpSyncClient.listTools());
        });
        return ResponseEntity.ok().build();
    }
}
