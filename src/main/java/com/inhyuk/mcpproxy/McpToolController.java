package com.inhyuk.mcpproxy;

import com.inhyuk.mcpproxy.entity.McpServer;
import com.inhyuk.mcpproxy.mcp.McpClientService;
import com.inhyuk.mcpproxy.models.McpTool;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mcp/tools")
@RequiredArgsConstructor
public class McpToolController {
    private final McpClientService mcpClientService;

    @GetMapping("search_tools")
    public ResponseEntity searchTools(@RequestParam String keyword){
        List<McpTool> search_result = mcpClientService.search_tool(keyword);
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
        return ResponseEntity.ok(list);
    }

    @Data
    @NoArgsConstructor
    public static class ToolExecuteRequestBody {
        private String toolName;
        private Map<String,Object> args;
    }

    @PostMapping("execute_tool")
    public ResponseEntity executeTool(@RequestBody ToolExecuteRequestBody requestBody){
        com.inhyuk.mcpproxy.models.McpTool tool = mcpClientService.getTool(requestBody.toolName);
        McpSyncClient client = tool.getClient();
        McpSchema.CallToolResult result = client.callTool(McpSchema.CallToolRequest.builder().name(tool.getTool().name()).arguments(requestBody.args).build());
        return ResponseEntity.ok(result);
    }

}
