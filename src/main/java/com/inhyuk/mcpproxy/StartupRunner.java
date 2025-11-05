package com.inhyuk.mcpproxy;

import com.inhyuk.mcpproxy.entity.McpServer;
import com.inhyuk.mcpproxy.entity.McpServerRepository;
import com.inhyuk.mcpproxy.mcp.McpClientFactory;
import com.inhyuk.mcpproxy.mcp.McpClientService;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

    private final McpClientService mcpClientService;
    private final McpClientFactory mcpClientFactory;
    private final McpServerRepository mcpServerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mcpServerRepository.findAll().forEach(mcpServer -> {
            McpSyncClient client;
            if(mcpServer.getTransportType().equals(McpServer.TransportType.SSE)){
                client = mcpClientFactory.createSSEClient(mcpServer.getBaseUrl(), mcpServer.getEndpoint());
            } else {
                client = mcpClientFactory.createStreamClient(mcpServer.getBaseUrl(), mcpServer.getEndpoint());
            }
            mcpClientService.addClient(client);
        });

    }
}
