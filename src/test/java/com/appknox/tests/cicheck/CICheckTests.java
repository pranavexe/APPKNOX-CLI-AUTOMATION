package com.appknox.tests.cicheck;

import com.appknox.tests.BaseTest;
import com.appknox.core.CLIExecutor.CommandResult;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appknox CLI Automation")
@Feature("CI Check Module")
@DisplayName("CI Check Test Suite")
public class CICheckTests extends BaseTest {

    @Test
    @Story("Low Risk Threshold CI Check")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cicheck command displays all vulnerabilities when risk threshold is set to low.")
    @DisplayName("CIC-001: Verify cicheck with low risk threshold")
    public void testCICheckWithLowRiskThreshold() {
        String fileId = config.getProperty("test.file.id", "1");

        step("Execute cicheck with low risk threshold", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("cicheck");
            cmdList.add(fileId);
            cmdList.add("--risk-threshold");
            cmdList.add("low");

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());
            env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("CI check with low threshold should execute successfully")
                    .isFalse();

            assertThat(result.getOutput())
                    .as("Output should display all vulnerabilities")
                    .isNotEmpty();

            Allure.addAttachment("CIC-001 Output", result.getOutput());
        });
    }

    @Test
    @Story("Medium Risk Threshold CI Check")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cicheck displays medium, high, and critical vulnerabilities only.")
    @DisplayName("CIC-002: Verify cicheck with medium risk threshold")
    public void testCICheckWithMediumRiskThreshold() {
        String fileId = config.getProperty("test.file.id", "1");

        step("Execute cicheck with medium risk threshold", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("cicheck");
            cmdList.add(fileId);
            cmdList.add("--risk-threshold");
            cmdList.add("medium");

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());
            env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("CI check with medium threshold should execute successfully")
                    .isFalse();

            assertThat(result.getOutput())
                    .as("Output should display medium, high, and critical vulnerabilities")
                    .isNotEmpty();

            Allure.addAttachment("CIC-002 Output", result.getOutput());
        });
    }

    @Test
    @Story("High Risk Threshold CI Check")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cicheck displays high and critical vulnerabilities only.")
    @DisplayName("CIC-003: Verify cicheck with high risk threshold")
    public void testCICheckWithHighRiskThreshold() {
        String fileId = config.getProperty("test.file.id", "1");

        step("Execute cicheck with high risk threshold", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("cicheck");
            cmdList.add(fileId);
            cmdList.add("--risk-threshold");
            cmdList.add("high");

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());
            env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("CI check with high threshold should execute successfully")
                    .isFalse();

            assertThat(result.getOutput())
                    .as("Output should display high and critical vulnerabilities")
                    .isNotEmpty();

            Allure.addAttachment("CIC-003 Output", result.getOutput());
        });
    }

    @Test
    @Story("Critical Risk Threshold CI Check")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cicheck displays only critical vulnerabilities.")
    @DisplayName("CIC-004: Verify cicheck with critical risk threshold")
    public void testCICheckWithCriticalRiskThreshold() {
        String fileId = config.getProperty("test.file.id", "304");

        step("Execute cicheck with critical risk threshold", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("cicheck");
            cmdList.add(fileId);
            cmdList.add("--risk-threshold");
            cmdList.add("critical");

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());
            env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("CI check with critical threshold should execute successfully")
                    .isFalse();

            assertThat(result.getOutput())
                    .as("Output should display only critical vulnerabilities")
                    .isNotEmpty();

            Allure.addAttachment("CIC-004 Output", result.getOutput());
        });
    }

    @Test
    @Story("Invalid Risk Threshold CI Check")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cicheck fails gracefully when an invalid risk threshold is provided.")
    @DisplayName("CIC-005: Verify error with invalid risk threshold")
    public void testCICheckWithInvalidRiskThreshold() {
        String fileId = config.getProperty("test.file.id", "1");
        String invalidThreshold = "invalid";

        step("Execute cicheck with invalid risk threshold", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("cicheck");
            cmdList.add(fileId);
            cmdList.add("--risk-threshold");
            cmdList.add(invalidThreshold);

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());
            env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("Command should fail with invalid risk threshold")
                    .isFalse();

            Allure.addAttachment("CIC-005 Output", result.getOutput());
        });
    }

    @Step("{stepName}")
    private void step(String stepName, Runnable runnable) {
        runnable.run();
    }
}