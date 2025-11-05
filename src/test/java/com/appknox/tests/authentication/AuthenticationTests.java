

package com.appknox.tests.authentication;

import com.appknox.tests.BaseTest;
import com.appknox.core.CLIExecutor.CommandResult;
import io.qameta.allure.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appknox CLI Automation")
@Feature("Authentication Module")
@DisplayName("Authentication Test Suite")
public class AuthenticationTests extends BaseTest {

    @Test
    @Story("Valid Token Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify authentication using a valid Appknox access token and a valid host configuration.")
    @DisplayName("AUTH-001: Verify authentication using a valid token")
    public void testValidTokenAuthentication() {
        String host = config.getValidHost();
        String token = config.getValidToken();

        step("Execute CLI command with valid token", () -> {
            CommandResult result = runAppknoxCommand("whoami", host, token);

            assertThat(result.isSuccess())
                    .as("Command should execute successfully with valid token")
                    .isTrue();

            assertThat(result.getOutput())
                    .as("Output should contain user details")
                    .isNotEmpty()
                    .containsAnyOf("email", "name", "username", "id");

            Allure.addAttachment("AUTH-001 Output", result.getOutput());
        });
    }

    @Test
    @Story("Invalid Token Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify CLI authentication fails with an invalid or expired token.")
    @DisplayName("AUTH-002: Verify authentication with an invalid token")
    public void testInvalidTokenAuthentication() {
        String host = config.getValidHost();
        String invalidToken = config.getInvalidToken();

        step("Execute CLI command with invalid token", () -> {
            CommandResult result = runAppknoxCommand("whoami", host, invalidToken);

            assertThat(result.isSuccess())
                    .as("Command should fail with invalid token")
                    .isFalse();

            assertThat(result.getOutput().toLowerCase())
                    .as("Output should contain error message about invalid or expired token")
                    .containsAnyOf("invalid", "expired", "token", "unauthorized", "authentication");

            Allure.addAttachment("AUTH-002 Output", result.getOutput());
        });
    }

    @Test
    @Story("Missing Token Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify CLI behavior when APPKNOX_ACCESS_TOKEN is missing.")
    @DisplayName("AUTH-003: Verify behavior when token is missing")
    public void testMissingTokenAuthentication() {
        String host = config.getValidHost();
        String token = null;

        step("Execute CLI command without token", () -> {
            CommandResult result = runAppknoxCommand("whoami", host, token);

            assertThat(result.isSuccess())
                    .as("Command should fail when token is missing")
                    .isFalse();

            assertThat(result.getOutput())
                    .as("Output should indicate missing APPKNOX_ACCESS_TOKEN")
                    .containsIgnoringCase("APPKNOX_ACCESS_TOKEN");

            Allure.addAttachment("AUTH-003 Output", result.getOutput());
        });
    }

    @Test
    @Story("Valid Host Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that CLI connects successfully with a valid host and token.")
    @DisplayName("AUTH-004: Verify valid host configuration")
    public void testValidHostConfiguration() {
        String host = config.getValidHost();
        String token = config.getValidToken();

        step("Execute CLI command with valid host", () -> {
            CommandResult result = runAppknoxCommand("whoami", host, token);

            assertThat(result.isSuccess())
                    .as("Command should connect successfully to valid host")
                    .isTrue();

            assertThat(result.getOutput())
                    .as("Output should display user details from custom host")
                    .isNotEmpty();

            Allure.addAttachment("AUTH-004 Output", result.getOutput());
        });
    }

    @Test
    @Story("Invalid Host Verification")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify CLI behavior when invalid host URL is provided.")
    @DisplayName("AUTH-005: Verify behavior with invalid host URL")
    public void testInvalidHostURL() {
        String invalidHost = config.getInvalidHost();
        String token = config.getValidToken();

        step("Execute CLI command with invalid host", () -> {
            CommandResult result = runAppknoxCommand("whoami", invalidHost, token);

            assertThat(result.isSuccess())
                    .as("Command should fail with invalid host")
                    .isFalse();

            assertThat(result.getOutput().toLowerCase())
                    .as("Output should contain host error message")
                    .containsAnyOf("unable to connect", "host", "connection", "failed", "error");

            Allure.addAttachment("AUTH-005 Output", result.getOutput());
        });
    }

    @Test
    @Story("Host URL Format Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that CLI throws an error when host URL is missing a trailing slash.")
    @DisplayName("AUTH-006: Verify error when host missing trailing slash")
    public void testHostMissingTrailingSlash() {
        String hostWithoutSlash = config.getHostWithoutSlash();
        String token = config.getValidToken();

        step("Execute CLI command with host missing trailing slash", () -> {
            CommandResult result = runAppknoxCommand("whoami", hostWithoutSlash, token);

            assertThat(result.isSuccess())
                    .as("Command should fail when host missing trailing slash")
                    .isFalse();

            assertThat(result.getOutput())
                    .as("Output should indicate BaseURL must have trailing slash")
                    .containsIgnoringCase("trailing slash");

            Allure.addAttachment("AUTH-006 Output", result.getOutput());
        });
    }

    @Step("{stepName}")
    private void step(String stepName, Runnable runnable) {
        runnable.run();
    }
}
