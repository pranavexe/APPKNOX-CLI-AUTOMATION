

package com.appknox.tests.reports;

import com.appknox.tests.BaseTest;
import com.appknox.core.CLIExecutor.CommandResult;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appknox CLI Automation")
@Feature("Reports Module")
@DisplayName("Report Generation Test Suite")
public class ReportTests extends BaseTest {

    @Test
    @Story("Report Creation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a report is successfully created for a valid file ID using 'appknox reports create' command.")
    @DisplayName("REP-001: Verify report creation for valid file ID")
    public void testReportCreation() {
        String fileId = config.getProperty("test.file.id", "1");

        List<String> cmdList = List.of(
                config.getCliPath(),
                "reports", "create", fileId
        );

        Map<String, String> env = Map.of(
                "APPKNOX_API_HOST", config.getValidHost(),
                "APPKNOX_ACCESS_TOKEN", config.getValidToken()
        );

        Allure.step("Execute CLI command: appknox reports create " + fileId);
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        assertThat(result.isSuccess())
                .as("Report creation should succeed with valid file ID")
                .isTrue();

        String output = result.getOutput().trim();
        // assertThat(output).isNotEmpty().matches("\\d+");

        System.out.println("REP-001 Output - Report ID: " + output);
        Allure.addAttachment("REP-001 CLI Output", new ByteArrayInputStream(output.getBytes()));
    }

    @Test
    @Story("Report Download")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a CSV report is downloaded successfully for a valid report ID.")
    @DisplayName("REP-004: Verify CSV report download for valid report ID")
    public void testCsvReportDownload() throws IOException {
        String reportId = config.getProperty("test.report.id", "1");
        String outputPath = config.getProperty("report.output.path", "./downloads");
        String csvFileName = "report_test.csv";
        String fullPath = outputPath + File.separator + csvFileName;

        File outputDir = new File(outputPath);
        if (!outputDir.exists()) outputDir.mkdirs();

        File csvFile = new File(fullPath);
        if (csvFile.exists()) csvFile.delete();

        List<String> cmdList = List.of(
                config.getCliPath(),
                "reports", "download", "summary-csv",
                reportId, "--output", fullPath
        );

        Map<String, String> env = Map.of(
                "APPKNOX_API_HOST", config.getValidHost(),
                "APPKNOX_ACCESS_TOKEN", config.getValidToken()
        );

        Allure.step("Execute CLI command: appknox reports download summary-csv " + reportId);
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        assertThat(result.isSuccess()).isTrue();
        assertThat(csvFile.exists()).isTrue();
        assertThat(csvFile.length()).isGreaterThan(0);

        Allure.addAttachment("REP-004 CLI Output", new ByteArrayInputStream(result.getOutput().getBytes()));
        Allure.addAttachment("Downloaded CSV Report", "text/csv", new FileInputStream(csvFile), "csv");
    }

    @Test
    @Story("Report Download")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that an Excel report is downloaded successfully for a valid report ID.")
    @DisplayName("REP-005: Verify Excel report download for valid report ID")
    public void testExcelReportDownload() throws IOException {
        String reportId = config.getProperty("test.report.id", "1");
        String outputPath = config.getProperty("report.output.path", "./downloads");
        String excelFileName = "report_test.xlsx";
        String fullPath = outputPath + File.separator + excelFileName;

        File outputDir = new File(outputPath);
        if (!outputDir.exists()) outputDir.mkdirs();

        File excelFile = new File(fullPath);
        if (excelFile.exists()) excelFile.delete();

        List<String> cmdList = List.of(
                config.getCliPath(),
                "reports", "download", "summary-excel",
                reportId, "--output", fullPath
        );

        Map<String, String> env = Map.of(
                "APPKNOX_API_HOST", config.getValidHost(),
                "APPKNOX_ACCESS_TOKEN", config.getValidToken()
        );

        Allure.step("Execute CLI command: appknox reports download summary-excel " + reportId);
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        assertThat(result.isSuccess()).isTrue();
        assertThat(excelFile.exists()).isTrue();
        assertThat(excelFile.length()).isGreaterThan(0);

        Allure.addAttachment("REP-005 CLI Output", new ByteArrayInputStream(result.getOutput().getBytes()));
        Allure.addAttachment("Downloaded Excel Report", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new FileInputStream(excelFile), "xlsx");
    }

    @Test
    @Story("SARIF Report Generation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that SARIF format report is generated successfully for a valid file ID.")
    @DisplayName("REP-009: Verify SARIF report generation for valid file ID")
    public void testSarifReportGeneration() {
        String fileId = config.getProperty("test.file.id", "1");

        List<String> cmdList = List.of(
                config.getCliPath(),
                "sarif", fileId);

        Map<String, String> env = Map.of(
                "APPKNOX_API_HOST", config.getValidHost(),
                "APPKNOX_ACCESS_TOKEN", config.getValidToken()
        );

        Allure.step("Execute CLI command: appknox sarif " + fileId);
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        assertThat(result.isSuccess()).isTrue();
        String output = result.getOutput();
        assertThat(output).isNotEmpty();

        String lowerOutput = output.toLowerCase();
        assertThat(lowerOutput).containsAnyOf("sarif", "version", "runs", "results");

        Allure.addAttachment("REP-009 CLI Output", new ByteArrayInputStream(output.getBytes()));
    }
}
