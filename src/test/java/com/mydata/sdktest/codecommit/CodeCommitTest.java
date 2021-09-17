package com.mydata.sdktest.codecommit;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codecommit.AWSCodeCommitClient;
import com.amazonaws.services.codecommit.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CodeCommitTest {
    private AWSCodeCommit codeCommit;

    @Value("${aws.code-commit.token")
    private String token;

    @Value("${test.code-commit.repository-name")
    private String repositoryName;

    @Value("${test.code-commit.commit.commit-message")
    private String commitMessage;
    @Value("${test.code-commit.commit.branch-name")
    private String branchName;
    @Value("${test.code-commit.commit.file-path")
    private String filePath;

    @Value("${test.code-commit.pull-request.title")
    private String title;
    @Value("${test.code-commit.pull-request.description")
    private String description;
    @Value("${test.code-commit.pull-request.source-reference")
    private String sourceReference;
    @Value("${test.code-commit.pull-request.destination-reference")
    private String destinationReference;

    @BeforeEach
    void beforeEach() {
        // given
        codeCommit = codeCommit == null ?
                AWSCodeCommitClient.builder()
                        .withRegion(Regions.AP_NORTHEAST_2.getName())
                        .build() :
                codeCommit;
    }

    @Test
    @DisplayName("Token으로 Repository 리스트 조회")
    void listRepositoriesTest() {
        // when
        ListRepositoriesResult listRepositoriesResult = codeCommit.listRepositories(new ListRepositoriesRequest()
                .withNextToken(token));
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
    @DisplayName("Commit")
    void commitTest() {
        // when
        CreateCommitResult commitResult = codeCommit.createCommit(new CreateCommitRequest()
                .withRepositoryName(repositoryName)
                .withBranchName(branchName)
                .withCommitMessage(commitMessage)
                .withPutFiles(new PutFileEntry()
                        .withFilePath(filePath))
        );
        // then
        System.out.println(commitResult);
    }

    @Test
    @DisplayName("PR 생성")
    void createPullRequestTest() {
        // when
        CreatePullRequestResult pullRequestResult = codeCommit.createPullRequest(new CreatePullRequestRequest()
                .withTitle(title)
                .withClientRequestToken(token)
                .withDescription(description)
                .withTargets(new Target()
                        .withRepositoryName(repositoryName)
                        .withSourceReference(sourceReference)
                        .withDestinationReference(destinationReference)));
        // then
        System.out.println(pullRequestResult);
    }
}