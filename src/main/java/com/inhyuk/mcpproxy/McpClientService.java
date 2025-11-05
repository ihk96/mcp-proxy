package com.inhyuk.mcpproxy;

import com.inhyuk.mcpproxy.models.McpTool;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpClientService {

    private final VectorStore vectorStore;

    private final List<McpSyncClient> clients = new ArrayList<>();
    private final Map<String, McpSyncClient> clientMap = new HashMap<>();
    private final List<McpTool> tools = new ArrayList<>();
    private final Map<String, McpTool> toolMap = new HashMap<>();

    public void addClient(McpSyncClient client) {
        clients.add(client);
        clientMap.put(client.getServerInfo().name(), client);
        client.listTools().tools().forEach(tool -> {
            String name = client.getServerInfo().name() + "_" + tool.name();
            String description = String.format("%s\n%s", StringUtils.hasText(client.getServerInstructions()) ? client.getServerInstructions() : "", StringUtils.hasText(tool.description()) ? tool.description() : "");
            McpTool toolInfo = new McpTool(name,description, client, tool);
            tools.add(toolInfo);
            toolMap.put(name, toolInfo);
            vectorStore.add(List.of(new Document(toolInfo.getDescription(),Map.of("name",toolInfo.getName()))));
            log.info("save tool: {} - {}", name,description);
        });
    }

    public List<McpSyncClient> getClients() {
        return clients;
    }

    public McpSyncClient getClient(String name) {
        return clientMap.get(name);
    }

    public McpTool getTool(String name){
        return toolMap.get(name);
    }

    public List<McpTool> search_tool(String keyword){
        List<McpTool> search_result = new ArrayList<>();
        List<McpTool> keywordSearchList = tools.stream().filter(tool->tool.getName().contains(keyword) || tool.getDescription().contains(keyword)).toList();
        keywordSearchList.forEach(mcpTool -> log.info("keywordSearch: {}", mcpTool));

        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query(keyword).similarityThreshold(0.5).build());
        List<McpTool> similarList = results.stream().map(document -> {
            String name = document.getMetadata().get("name").toString();
            log.info("similarList: {} - {}", name, document.getScore());
            return toolMap.get(name);
        }).toList();

        search_result.addAll(keywordSearchList);
        search_result.addAll(similarList);

        return search_result;
    }

}
