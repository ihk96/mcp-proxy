package com.inhyuk.mcpproxy;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class McpClientTests {

    @InjectMocks
    McpClientFactory mcpClientFactory;

    @InjectMocks
    McpClientService mcpClientService;

    @Test
    public void testCreateStreamClient(){
        McpSyncClient client1 = mcpClientFactory.createStreamClient("http://localhost:8090","/stream");
        McpSyncClient client2 = mcpClientFactory.createStreamClient("http://localhost:8091","/stream");
        mcpClientService.addClient(client1);
        mcpClientService.addClient(client2);
        mcpClientService.getClients().forEach(mcpSyncClient -> {
            System.out.println(mcpSyncClient.getServerInfo());
            System.out.println(mcpSyncClient.getClientInfo());
            System.out.println(mcpSyncClient.listTools());
        });
    }

}
