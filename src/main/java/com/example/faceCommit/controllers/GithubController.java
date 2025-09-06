package com.example.faceCommit.controllers;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.nio.charset.StandardCharsets;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDate;

@RestController
public class GithubController {
    @Autowired
    @Value("${GITHUB_TOKEN}")
    private String GITHUB_TOKEN;
    @Value("${GITHUB_REPO_OWNER}")
    private String GITHUB_REPO_OWNER;
    private GitHub github;

    @PostConstruct
    private void init() throws IOException{
        // 2.0 版本建議在初始化時就建立 GitHub 實例
        this.github = GitHubBuilder.fromEnvironment()
            .withOAuthToken(GITHUB_TOKEN)
            .build();
    }
    @GetMapping("/getLastCheckin")
    public ResponseEntity<?> getLastCheckin() {
        if (this.GITHUB_TOKEN == null || this.GITHUB_REPO_OWNER == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "缺少必要參數(token或owner)"));
        }
        try {
            // 2.0 版本的倉庫獲取方式
            GHRepository repository = github.getRepository(GITHUB_REPO_OWNER + "/udemy_1007");
            
            // 2.0 版本改進了內容獲取的方式
            GHContent content = repository.getFileContent("README.md");

            String fileContent = new String(
                content.read().readAllBytes(),
                StandardCharsets.UTF_8
            );
            // System.out.println("檔案內容：" + fileContent);
            
            // 處理最後一行
            String[] lines = fileContent.split("\n");
            String lastLine = lines.length > 0 ? lines[lines.length - 1].trim() : "";
            String lastDate = lastLine.isEmpty() ? "" : 
                lastLine.split("\\(")[0].replace(",", "").trim();

            return ResponseEntity.ok(Map.of("lastDate", lastDate, "lastLine", lastLine));

        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "取得 README.md 內容失敗: " + e.getMessage()));
        }
        
    }

    @PostMapping("/updateFile")
    public ResponseEntity<?>  updateFile(@RequestBody Map<String, String> request) {
        if (this.GITHUB_TOKEN == null || this.GITHUB_REPO_OWNER == null) {
            return ResponseEntity.status(400)
                .body(Map.of("error", "缺少必要參數(token或owner)"));
        }

        try {
        GHRepository repository = github.getRepository(GITHUB_REPO_OWNER + "/udemy_1007");
        GHContent content = repository.getFileContent("README.md");
        
        String oldContent = new String(
            content.read().readAllBytes(),
            StandardCharsets.UTF_8
        );

        String today = LocalDate.now().toString();
        String newLine = String.format("%s(%s, %s),\n", 
            today, 
            request.getOrDefault("city", ""), 
            request.getOrDefault("country", "")
        );
        String newContent = oldContent + newLine;

        content.update(
            newContent,
            "他媽的更新@" + today,
            "main"
        );

        return ResponseEntity.ok(Map.of("message", "README.md updated: " + today));

    } catch (IOException e) {
        if (e.getMessage() != null && e.getMessage().contains("401")) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "權限不足，GitHub Token 失效"));
        }
        return ResponseEntity.status(500)
            .body(Map.of("error", "更新 README.md 失敗: " + e.getMessage()));
    }
    }


}
