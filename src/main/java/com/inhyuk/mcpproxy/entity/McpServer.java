package com.inhyuk.mcpproxy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class McpServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String baseUrl;
    private String endpoint;
    private TransportType transportType;

    public enum TransportType {
        SSE, STREAM
    }

    @Builder
    public McpServer(String name, String baseUrl, String endpoint, TransportType transportType) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.endpoint = endpoint;
        this.transportType = transportType;
    }
}

