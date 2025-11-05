package com.appknox.tests.cliflags;

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
@Feature("CLI Flags Module")
@DisplayName("CLI Flags Test Suite")
public class CLIFlagsTests extends BaseTest {

    @Test
    @Story("Access Token Flag Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the access-token flag authenticates the user successfully.")
    @DisplayName("FLAG-001: Verify access-token flag authenticates user successfully")
    public void testAccessTokenFlag() {
        step("Execute command with --access-token flag", () -> {
            String token = config.getValidToken();

            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("whoami");
            cmdList.add("-a");
            cmdList.add(token);

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("Command should authenticate successfully using --access-token flag")
                    .isTrue();

            assertThat(result.getOutput())
                    .as("Output should display user details")
                    .isNotEmpty();

            Allure.addAttachment("FLAG-001 Output", result.getOutput());
        });
    }

    @Test
    @Story("Host Flag Connection")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the host flag connects the CLI to a custom Appknox server.")
    @DisplayName("FLAG-002: Verify host flag connects to custom Appknox server")
    public void testHostFlag() {
        step("Execute command with --host flag", () -> {
            String host = config.getValidHost();
            String token = config.getValidToken();

            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("whoami");
            cmdList.add("--host");
            cmdList.add(host);
            cmdList.add("-a");
            cmdList.add(token);

            Map<String, String> env = new HashMap<>();

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("Command should connect successfully using --host flag")
                    .isTrue();

            assertThat(result.getOutput())
                    .as("Output should display user details from specified host")
                    .isNotEmpty();

            Allure.addAttachment("FLAG-002 Output", result.getOutput());
        });
    }

    @Test
    @Story("Help Flag Functionality")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the --help flag displays help information correctly.")
    @DisplayName("FLAG-003: Verify --help flag displays help information")
    public void testHelpFlag() {
        step("Execute command with --help flag", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("--help");

            Map<String, String> env = new HashMap<>();

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("Help command should execute successfully")
                    .isTrue();

            String output = result.getOutput().toLowerCase();

            assertThat(output)
                    .as("Output should display help information")
                    .isNotEmpty();

            assertThat(output)
                    .as("Output should contain usage details and available commands")
                    .containsAnyOf("usage", "commands", "options", "help");

            Allure.addAttachment("FLAG-003 Output", result.getOutput());
        });
    }

    @Test
    @Story("Insecure Flag Functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the --insecure flag disables SSL checks successfully.")
    @DisplayName("FLAG-004: Verify --insecure flag disables SSL checks")
    public void testInsecureFlag() {
        step("Execute command with --insecure flag", () -> {
            String token = config.getValidToken();

            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("whoami");
            cmdList.add("-k");
            cmdList.add("-a");
            cmdList.add(token);

            Map<String, String> env = new HashMap<>();
            env.put("APPKNOX_API_HOST", config.getValidHost());

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("Command should connect successfully even with insecure flag")
                    .isTrue();

            assertThat(result.getOutput())
                    .as("Output should display user info (SSL checks disabled)")
                    .isNotEmpty();

            Allure.addAttachment("FLAG-004 Output", result.getOutput());
        });
    }

    @Test
    @Story("Version Flag Functionality")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that the --version flag displays the CLI version correctly.")
    @DisplayName("FLAG-006: Verify --version flag displays CLI version")
    public void testVersionFlag() {
        step("Execute command with --version flag", () -> {
            List<String> cmdList = new ArrayList<>();
            cmdList.add(config.getCliPath());
            cmdList.add("--version");

            Map<String, String> env = new HashMap<>();

            CommandResult result = cliExecutor.executeCommand(cmdList, env);

            assertThat(result.isSuccess())
                    .as("Version command should execute successfully")
                    .isTrue();

            String output = result.getOutput().toLowerCase();

            assertThat(output)
                    .as("Output should display CLI version number")
                    .isNotEmpty();

            assertThat(output)
                    .as("Output should contain version information")
                    .containsAnyOf("version", "appknox", "v", ".");

            Allure.addAttachment("FLAG-006 Output", result.getOutput());
        });
    }

    @Step("{stepName}")
    private void step(String stepName, Runnable runnable) {
        runnable.run();
    }
}
