package com.appknox.tests.information;

import com.appknox.tests.BaseTest;
import com.appknox.core.CLIExecutor.CommandResult;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.Description;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Information Commands")
@DisplayName("Information Commands Test Suite")
public class InformationTests extends BaseTest {

    @Test
    @Story("INFO-001: List all organizations")
    @Description("Validate that the CLI command 'appknox organizations' lists all available organizations for the authenticated user.")
    @DisplayName("INFO-001: Verify organizations list is displayed")
    public void testOrganizationsList() {
        CommandResult result = runAppknoxCommand("organizations");

        assertThat(result.isSuccess())
                .as("Organizations command should execute successfully")
                .isTrue();

        String output = result.getOutput();
        assertThat(output)
                .as("Output should contain organization details")
                .isNotEmpty();

        System.out.println("INFO-001 Output:");
        System.out.println(output);

        Allure.addAttachment("INFO-001 CLI Output", new ByteArrayInputStream(output.getBytes()));
    }

@Test
@Story("INFO-003: Retrieve files for a dynamically fetched project ID")
@Description("Fetches the first available project from 'appknox projects' and validates 'appknox files <project_id>' command output.")
@DisplayName("INFO-003: Verify files list using dynamically fetched project ID")
public void testFilesListForDynamicProject() {
    //Get the projects list
    CommandResult projectsResult = runAppknoxCommand("projects");
    assertThat(projectsResult.isSuccess())
            .as("Projects command should execute successfully")
            .isTrue();

    String projectsOutput = projectsResult.getOutput();
    assertThat(projectsOutput)
            .as("Output should contain project list")
            .isNotEmpty();

    // Extract the first project ID 
    String firstProjectId = Arrays.stream(projectsOutput.split("\\r?\\n"))
            .filter(line -> line.trim().matches("^\\d+\\s+.*")) // line starts with ID
            .map(line -> line.trim().split("\\s+")[0]) // take the first column
            .findFirst()
            .orElseThrow(() -> new AssertionError("No valid project ID found in output!"));

    System.out.println("Using Project ID: " + firstProjectId);

   
    List<String> cmdList = new ArrayList<>();
    cmdList.add(config.getCliPath()); 
    cmdList.add("files");
    cmdList.add(firstProjectId);

    Map<String, String> env = new HashMap<>();
    env.put("APPKNOX_API_HOST", config.getValidHost());
    env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

    
    CommandResult filesResult = cliExecutor.executeCommand(cmdList, env);

    assertThat(filesResult.isSuccess())
            .as("Files command should execute successfully")
            .isTrue();

    String filesOutput = filesResult.getOutput();
    assertThat(filesOutput)
            .as("Output should contain file list")
            .isNotEmpty();

    assertThat(filesOutput.toLowerCase())
            .as("Output should contain file details like ID, name, or version")
            .containsAnyOf("id", "name", "version");

   
    Allure.addAttachment("Projects CLI Output", new ByteArrayInputStream(projectsOutput.getBytes()));
    Allure.addAttachment("Files CLI Output", new ByteArrayInputStream(filesOutput.getBytes()));

    System.out.println("Files output:\n" + filesOutput);
}


    @Test
    @Story("INFO-004: Handle invalid project ID")
    @Description("Validate that the CLI displays an appropriate error message when an invalid project ID is provided in the 'files' command.")
    @DisplayName("INFO-004: Verify error for invalid project ID")
    public void testFilesListForInvalidProject() {
        String invalidProjectId = "999999999";

        List<String> cmdList = new ArrayList<>();
        cmdList.add(config.getCliPath());
        cmdList.add("files");
        cmdList.add(invalidProjectId);

        Map<String, String> env = new HashMap<>();
        env.put("APPKNOX_API_HOST", config.getValidHost());
        env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        assertThat(result.isSuccess())
                .as("Command should fail with invalid project ID")
                .isFalse();

        String output = result.getOutput();
        System.out.println("INFO-004 Output:");
        System.out.println(output);

        Allure.addAttachment("INFO-004 CLI Output", new ByteArrayInputStream(output.getBytes()));
    }

    @Test
    @Story("INFO-005: Handle missing project ID")
    @Description("Validate that the CLI displays an error message when the 'files' command is executed without providing a project ID.")
    @DisplayName("INFO-005: Verify error for empty project ID")
    public void testFilesListWithoutProjectId() {
        CommandResult result = runAppknoxCommand("files");

        assertThat(result.isSuccess())
                .as("Command should fail when project ID is not provided")
                .isFalse();

        String output = result.getOutput();
        System.out.println("INFO-005 Output:");
        System.out.println(output);

        Allure.addAttachment("INFO-005 CLI Output", new ByteArrayInputStream(output.getBytes()));
    }
}
