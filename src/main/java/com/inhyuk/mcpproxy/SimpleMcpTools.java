package com.inhyuk.mcpproxy;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
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

    @McpTool(description = "Mcp Version")
    public List<Object> search_tools(@McpToolParam() String keyword) {
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

    @McpTool(description = "")
    public String excecute_tool(){
        McpSyncClient client1 = mcpClientFactory.createStreamClient("http://localhost:8090","/stream");
        McpSyncClient client2 = mcpClientFactory.createStreamClient("http://localhost:8091","/stream");
        mcpClientService.addClient(client1);
        mcpClientService.addClient(client2);
        client2.callTool(new McpSchema.CallToolRequest("dasd", Map.of()));
        return "success";
    }


}
