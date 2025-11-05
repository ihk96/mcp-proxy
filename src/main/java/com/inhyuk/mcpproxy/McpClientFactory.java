package com.inhyuk.mcpproxy;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class McpClientFactory {

    public McpSyncClient createStreamClient(String baseUrl, String endpoint) {
        try {
            McpClientTransport transport = buildStreamTransport(baseUrl, endpoint);
            return createAndInitializeClient(transport);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect", e);
        }
    }

    public McpSyncClient createSSEClient(String baseUrl, String endpoint) {
        try {
            McpClientTransport transport = buildSseTransport(baseUrl, endpoint);
            return createAndInitializeClient(transport);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect", e);
        }
    }

    private McpClientTransport buildStreamTransport(String baseUrl, String endpoint) {
        HttpClientStreamableHttpTransport.Builder transportBuilder =
                HttpClientStreamableHttpTransport.builder(baseUrl);
        if (StringUtils.hasText(endpoint)) {
            transportBuilder.endpoint(endpoint);
        }
        return transportBuilder.build();
    }

    private McpClientTransport buildSseTransport(String baseUrl, String endpoint) {
        HttpClientSseClientTransport.Builder transportBuilder =
                HttpClientSseClientTransport.builder(baseUrl);
        if (StringUtils.hasText(endpoint)) {
            transportBuilder.sseEndpoint(endpoint);
        }
        return transportBuilder.build();
    }

    private McpSyncClient createAndInitializeClient(McpClientTransport transport) {
        McpSyncClient client = McpClient.sync(transport).build();

        McpSchema.InitializeResult result = client.initialize();
        log.info("Connected to: {}", result.serverInfo().name());

        McpSchema.ListToolsResult tools = client.listTools();
        log.info("Found {} tools", tools.tools().size());

        return client;
    }
}
