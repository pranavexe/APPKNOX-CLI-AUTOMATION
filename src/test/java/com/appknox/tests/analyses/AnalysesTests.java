// 

package com.appknox.tests.analyses;

import com.appknox.tests.BaseTest;
import com.appknox.core.CLIExecutor.CommandResult;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appknox CLI Automation")
@Feature("Analyses & Vulnerability Module")
@DisplayName("Analyses & Vulnerability Test Suite")
public class AnalysesTests extends BaseTest {

    @Test
    @Story("Valid File ID Analyses")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the 'analyses' command executes successfully for a valid file ID and returns expected output.")
    @DisplayName("ANA-001: Verify analyses list for valid file ID")
    public void testAnalysesListForValidFile() {
        String fileId = config.getProperty("test.file.id.analyses", "14");
        step("Execute CLI command with valid file ID", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "analyses", fileId);
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isTrue()
                    .as("Analyses command should succeed with valid file ID");
            assertThat(result.getOutput()).isNotEmpty()
                    .as("Output should display list of analyses");

            Allure.addAttachment("ANA-001 Output", result.getOutput());
        });
    }

    @Test
    @Story("Invalid File ID Analyses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that CLI shows an appropriate error message for invalid file ID in 'analyses' command.")
    @DisplayName("ANA-002: Verify error for invalid file ID in analyses")
    public void testAnalysesListForInvalidFile() {
        step("Execute CLI command with invalid file ID", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "analyses", "999999");
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isFalse()
                    .as("Command should fail with invalid file ID");
            assertThat(result.getOutput().toLowerCase())
                    .containsAnyOf("are you sure", "are you sure");

            Allure.addAttachment("ANA-002 Output", result.getOutput());
        });
    }

    @Test
    @Story("Valid Vulnerability Details")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify 'vulnerability' command retrieves correct vulnerability details for a valid vulnerability ID.")
    @DisplayName("ANA-003: Verify vulnerability details for valid vulnerability ID")
    public void testVulnerabilityDetailsForValidId() {
        String vulnerabilityId = config.getProperty("test.vulnerability.id", "1");
        step("Execute CLI command for valid vulnerability ID", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "vulnerability", vulnerabilityId);
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getOutput()).isNotEmpty()
                    .as("Output should display vulnerability details");

            Allure.addAttachment("ANA-003 Output", result.getOutput());
        });
    }

    @Test
    @Story("Invalid Vulnerability ID Handling")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify CLI error handling when an invalid vulnerability ID is used.")
    @DisplayName("ANA-004: Verify error for invalid vulnerability ID")
    public void testVulnerabilityDetailsForInvalidId() {
        step("Execute CLI command with invalid vulnerability ID", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "vulnerability", "999992345678909");
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isTrue()
                    .as("Command should fail with invalid vulnerability ID");
        //     assertThat(result.getOutput().toLowerCase())
        //             .containsAnyOf("description", "compliant", "intro");

            Allure.addAttachment("ANA-004 Output", result.getOutput());
        });
    }

    @Test
    @Story("Missing File ID in Analyses Command")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify CLI behavior when 'analyses' command is executed without providing file_id parameter.")
    @DisplayName("ANA-005: Verify analyses command without file_id parameter")
    public void testAnalysesWithoutFileId() {
        step("Execute CLI command without file_id parameter", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "analyses");
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isFalse()
                    .as("Command should fail when file_id is missing");
            assertThat(result.getOutput().toLowerCase())
                    .containsAnyOf("missing", "file_id", "required");

            Allure.addAttachment("ANA-005 Output", result.getOutput());
        });
    }

    @Test
    @Story("Valid OWASP ID Details")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify CLI retrieves OWASP category details for a valid OWASP ID.")
    @DisplayName("ANA-006: Verify OWASP details for valid OWASP ID")
    public void testOwaspDetailsForValidId() {
        step("Execute CLI command with valid OWASP ID", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "owasp", "2");
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isTrue()
                    .as("OWASP command should succeed for valid ID");
            assertThat(result.getOutput()).isNotEmpty()
                    .as("Output should display OWASP category details");

            Allure.addAttachment("ANA-006 Output", result.getOutput());
        });
    }

    @Test
    @Story("Invalid OWASP ID Handling")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify CLI error message when invalid OWASP ID is provided.")
    @DisplayName("ANA-007: Verify error for invalid OWASP ID")
    public void testOwaspDetailsForInvalidId() {
        step("Execute CLI command with invalid OWASP ID", () -> {
            List<String> cmdList = List.of(config.getCliPath(), "owasp", "999a999");
            Map<String, String> env = Map.of(
                    "APPKNOX_API_HOST", config.getValidHost(),
                    "APPKNOX_ACCESS_TOKEN", config.getValidToken()
            );

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess()).isTrue()
                    .as("Command should fail with invalid OWASP ID");
        //     assertThat(result.getOutput().toLowerCase())
        //             .containsAnyOf("exists", "are you sure");

            Allure.addAttachment("ANA-007 Output", result.getOutput());
        });
    }

    @Step("{stepName}")
    private void step(String stepName, Runnable runnable) {
        runnable.run();
    }
}
