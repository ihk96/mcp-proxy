package com.inhyuk.mcpproxy.models;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class McpTool {
    private String name;
    private McpSyncClient client;
    private McpSchema.Tool tool;
    private String description;

    public McpTool(String name, String description, McpSyncClient client, McpSchema.Tool tool) {
        this.name = name;
        this.client = client;
        this.tool = tool;
        this.description = description;
    }
}
