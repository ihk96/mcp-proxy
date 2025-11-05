package com.inhyuk.mcpproxy.models;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.Data;

@Data
public class McpToolInfo {
    private McpSyncClient client;
    private McpSchema.Tool tool;
    private String description;

    public McpToolInfo(McpSyncClient client, McpSchema.Tool tool, String description) {
        this.client = client;
        this.tool = tool;
        this.description = description;
    }
}
