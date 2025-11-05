package com.inhyuk.mcpproxy.mcp;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SimpleMcpTools {

    private final McpClientService mcpClientService;
    private final McpClientFactory mcpClientFactory;

    @McpTool(description = "Search for available MCP tools across multiple domain-specific MCP servers using hybrid search. This tool performs both keyword matching (exact string search on tool names and descriptions) and semantic similarity search (vector embedding-based) to find the most relevant tools for your query. Returns a list of matching tools grouped by their source MCP servers, including server information and instructions.")
    public List<Object> search_tools(@McpToolParam(description = "keyword: The search query to find relevant MCP tools. Can be a tool name, functionality description, or any relevant term. The search will match both exact strings and semantically similar content.") String keyword) {
        List<com.inhyuk.mcpproxy.models.McpTool> search_result = mcpClientService.search_tool(keyword);
        List<Object> list = new ArrayList<>();
        Map<String, Object> clientMap = new HashMap<>();
        search_result.forEach(mcpTool -> {
            McpSyncClient client = mcpTool.getClient();
            McpSchema.Tool tool = mcpTool.getTool();
            Map cm;
            if(clientMap.containsKey(client.getServerInfo().name())){
                cm = (Map) clientMap.get(client.getServerInfo().name());
            } else {
                cm = new HashMap();
                cm.put("tools",new ArrayList<>());
                cm.put("serverName",client.getServerInfo().name());
                cm.put("serverInstructions", client.getServerInstructions());
                clientMap.put(client.getServerInfo().name(), cm);
                list.add(cm);
            }
            ((List)cm.get("tools")).add(tool);
        });
        return list;
    }

    @McpTool(description = "Execute a specific MCP tool from a domain-specific MCP server. This tool is used to actually invoke the tools discovered via search_tools. Use the tool information returned from search_tools to construct the proper toolName and arguments for execution.")
    public McpSchema.CallToolResult excecute_tool(
            @McpToolParam(description = "The unique identifier of the MCP tool to execute, formatted as '{serverName}_{toolName}' where serverName and toolName are joined with an underscore. Both values can be obtained from the search_tools results. Example: 'weather_server_get_forecast'") String toolName,
            @McpToolParam(description = "A map of arguments required to execute the MCP tool. The parameter names and types should match the tool's schema provided in the search_tools results. Refer to the tool's parameter specifications from search_tools to construct this correctly.") Map<String,Object> args
    ){
        com.inhyuk.mcpproxy.models.McpTool tool = mcpClientService.getTool(toolName);
        McpSyncClient client = tool.getClient();
        return client.callTool(McpSchema.CallToolRequest.builder().name(tool.getTool().name()).arguments(args).build());
    }


}
