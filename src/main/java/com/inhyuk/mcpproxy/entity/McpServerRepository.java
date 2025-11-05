package com.inhyuk.mcpproxy.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface McpServerRepository extends JpaRepository<McpServer, Long> {
}
