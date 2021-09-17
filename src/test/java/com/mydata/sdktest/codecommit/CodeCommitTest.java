package com.mydata.sdktest.codecommit;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codecommit.AWSCodeCommitClient;
import com.amazonaws.services.codecommit.model.ListRepositoriesRequest;
import org.junit.jupiter.api.Test;

public class CodeCommitTest {
    @Test
    void test() {
        AWSCodeCommit codeCommit = AWSCodeCommitClient.builder()
                .withRegion(Regions.AP_NORTHEAST_2.getName())
                .build();
        ListRepositoriesRequest listRepositoriesRequest = new ListRepositoriesRequest();

        codeCommit.listRepositories(listRepositoriesRequest);
    }
}
