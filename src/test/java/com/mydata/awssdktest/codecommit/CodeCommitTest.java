package com.mydata.awssdktest.codecommit;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codecommit.AWSCodeCommitClient;
import com.amazonaws.services.codecommit.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@SpringBootTest
public class CodeCommitTest {
    private static AWSCodeCommit codeCommit;

    @Value("${test.code-commit.repository-name}")
    private String repositoryName;
    @Value("${test.code-commit.file-path}")
    private String filePath;
    @Value("${test.code-commit.commit.commit-message}")
    private String commitMessage;
    @Value("${test.code-commit.commit.branch-name}")
    private String branchName;

    private static String commitId;

    @BeforeAll
    static void beforeEach() {
        // given
        codeCommit = AWSCodeCommitClient.builder()
                .withRegion(Regions.AP_NORTHEAST_2.getName())
                .build();
    }

    @Test
    @DisplayName("Repository 리스트 조회")
    void listRepositoriesTest() {
        // when
        ListRepositoriesResult listRepositoriesResult = codeCommit.listRepositories(new ListRepositoriesRequest());
        // then
        List<RepositoryNameIdPair> repositories = listRepositoriesResult.getRepositories();
        repositories.forEach(System.out::println);
    }

    @Test
    @DisplayName("Repository 이름으로 Repository 조회")
    void getRepositoryTest() {
        // when
        GetRepositoryResult repositoryResult = codeCommit.getRepository(new GetRepositoryRequest()
                .withRepositoryName(repositoryName));
        // then
        System.out.println(repositoryResult.getRepositoryMetadata());
    }

    @Test
    @DisplayName("파일 다운로드 -> 수정 -> 파일 업로드 테스트")
    void fileIntegrationTest() {
        Assertions.assertAll(
                this::getFile,
                this::modifyFile,
                this::putFile
        );
    }

    void getFile() throws IOException {
        // when
        GetFileResult fileResult = codeCommit.getFile(new GetFileRequest()
                .withRepositoryName(repositoryName)
                .withFilePath(filePath));
        // then
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.write(path, StandardCharsets.UTF_8.decode(fileResult.getFileContent()).toString()
                .getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        commitId = fileResult.getCommitId();
    }

    private void modifyFile() throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        allLines.add("# this is test " + System.currentTimeMillis());
        Files.write(Paths.get(filePath), allLines);
    }

    void putFile() throws IOException {
        // when
        PutFileResult putFileResult = codeCommit.putFile(new PutFileRequest()
                .withRepositoryName(repositoryName)
                .withBranchName(branchName)
                .withCommitMessage(commitMessage)
                .withParentCommitId(commitId)
                .withFilePath(filePath)
                .withFileContent(ByteBuffer.wrap(Files.readAllBytes(Paths.get(filePath))))
        );
        // then
        System.out.println(putFileResult);
    }
}