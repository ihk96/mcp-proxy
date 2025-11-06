package com.inhyuk.mcpproxy;

import com.inhyuk.mcpproxy.entity.McpServer;
import com.inhyuk.mcpproxy.entity.McpServerRepository;
import com.inhyuk.mcpproxy.mcp.McpClientFactory;
import com.inhyuk.mcpproxy.mcp.McpClientService;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class McpServerService {

    private final McpServerRepository mcpServerRepository;
    private final McpClientFactory mcpClientFactory;
    private final McpClientService mcpClientService;

    public McpServerService(McpServerRepository mcpServerRepository, McpClientFactory mcpClientFactory, McpClientService mcpClientService) {
        this.mcpServerRepository = mcpServerRepository;
        this.mcpClientFactory = mcpClientFactory;
        this.mcpClientService = mcpClientService;
    }

    public McpServer add(String name, String baseUrl, String endpoint, McpServer.TransportType transportType){
        McpServer newServer = McpServer.builder().name(name).
                baseUrl(baseUrl).endpoint(endpoint).transportType(transportType).build();
        mcpServerRepository.save(newServer);

        McpSyncClient client;
        if(transportType.equals(McpServer.TransportType.SSE)){
            client = mcpClientFactory.createSSEClient(newServer.getBaseUrl(), newServer.getEndpoint());
        } else {
            client = mcpClientFactory.createStreamClient(newServer.getBaseUrl(), newServer.getEndpoint());
        }
        mcpClientService.addClient(client);

        return newServer;
    }

    public List<McpServer> getAll(){
        return mcpServerRepository.findAll();
    }

    public Optional<McpServer> getById(long id){
        return mcpServerRepository.findById(id);
    }
}
