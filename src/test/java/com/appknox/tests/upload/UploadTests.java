


package com.appknox.tests.upload;

import com.appknox.tests.BaseTest;
import com.appknox.core.CLIExecutor.CommandResult;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appknox CLI Automation")
@Feature("Upload Functionality")
@DisplayName("Upload Functionality Test Suite")
public class UploadTests extends BaseTest 
{
@Test
@Story("UPLOAD-001: Valid APK Upload")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify that a valid APK file can be successfully uploaded via the Appknox CLI and save the file ID for reuse.")
@DisplayName("UPLOAD-001: Verify successful upload of valid APK file and store file ID")
public void testValidApkUpload() {
    Allure.step("Get valid APK path from config");
    String apkPath = config.getProperty("test.apk.valid", "src/test/resources/testdata/test-files/MFVA.apk");

    Allure.step("Build CLI command for upload");
    List<String> cmdList = new ArrayList<>();
    cmdList.add(config.getCliPath());
    cmdList.add("upload");
    cmdList.add(apkPath);

    Allure.step("Set environment variables for valid host and token");
    Map<String, String> env = new HashMap<>();
    env.put("APPKNOX_API_HOST", config.getValidHost());
    env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

    Allure.step("Execute CLI command for APK upload");
    CommandResult result = cliExecutor.executeCommand(cmdList, env);

    Allure.step("Verify command execution success");
    assertThat(result.isSuccess())
            .as("Upload command should execute successfully")
            .isTrue();

    String output = result.getOutput().trim();
    Allure.addAttachment("UPLOAD-001 Command Output", new ByteArrayInputStream(output.getBytes()));

    System.out.println("UPLOAD-001 Output:\n" + output);

    //Extract last numeric line as File ID
    String fileId = null;
    for (String line : output.split("\\R")) { // \R matches all line endings
        line = line.trim();
        if (line.matches("\\d+")) {
            fileId = line; // last numeric line wins
        }
    }

    assertThat(fileId)
            .as("File ID should be extracted correctly from upload output")
            .isNotNull();

    System.out.println("Extracted File ID: " + fileId);

    //Save File ID to config.properties //overwrite existing valueee
    try {
        Path configPath = Paths.get("src/test/resources/config.properties");
        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(configPath)) {
            props.load(input);
        }
        props.setProperty("test.file.id", fileId);
        try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            props.store(outputStream, "Updated after upload test");
        }
        System.out.println("Saved File ID to config.properties: " + fileId);
    } catch (IOException e) {
        throw new RuntimeException("Failed to save file ID to config file", e);
    }
}

    @Test
    @Story("UPLOAD-003: Non-existent File Upload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the CLI throws an error when attempting to upload a file that doesn't exist.")
    @DisplayName("UPLOAD-003: Verify error when uploading non-existent file")
    public void testNonExistentFileUpload() {
        Allure.step("Set invalid non-existent file path");
        String nonExistentPath = "C:/non-existent-file.apk";

        Allure.step("Build CLI command for upload");
        List<String> cmdList = new ArrayList<>();
        cmdList.add(config.getCliPath());
        cmdList.add("upload");
        cmdList.add(nonExistentPath);

        Allure.step("Set valid environment variables");
        Map<String, String> env = new HashMap<>();
        env.put("APPKNOX_API_HOST", config.getValidHost());
        env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

        Allure.step("Execute CLI command");
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        Allure.step("Verify upload fails for non-existent file");
        assertThat(result.isSuccess())
                .as("Command should fail with non-existent file")
                .isFalse();

        Allure.step("Verify error message is shown");
        assertThat(result.getOutput().toLowerCase())
                .as("Output should contain file not found error")
                .containsAnyOf("file not found", "no such file", "does not exist", "error");

        Allure.addAttachment("UPLOAD-003 Command Output", new ByteArrayInputStream(result.getOutput().getBytes()));

        System.out.println("UPLOAD-003 Output: " + result.getOutput());
    }

   

//   @Test
// @Story("UPLOAD-006: Upload Same APK Twice")
// @Severity(SeverityLevel.CRITICAL)
// @Description("Verify that uploading the same APK twice generates unique file IDs each time.")
// @DisplayName("UPLOAD-006: Verify behavior when uploading the same APK twice")
// public void testUploadSameApkTwice() {
//     Allure.step("Read APK path and first upload file ID from config");
//     String apkPath = config.getProperty("test.apk.valid", "src/test/resources/testdata/test-files/MFVA.apk");
//     String fileId = config.getProperty("test.file.id",""); // Reuse from UPLOAD-001

//     assertThat(fileId)
//             .as("First upload file ID should be available from UPLOAD-001")
//             .isNotEmpty();

//     Allure.step("Set valid environment variables");
//     Map<String, String> env = new HashMap<>();
//     env.put("APPKNOX_API_HOST", config.getValidHost());
//     env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

//     Allure.step("Re-upload the same APK");
//     List<String> cmdList = new ArrayList<>();
//     cmdList.add(config.getCliPath());
//     cmdList.add("upload");
//     cmdList.add(apkPath);

//     CommandResult result2 = cliExecutor.executeCommand(cmdList, env);
//     String fileId2 = result2.getOutput().trim();
//     System.out.println("Re-upload File ID: " + fileId2);
//     Allure.addAttachment("UPLOAD-006 Re-upload Output", new ByteArrayInputStream(result2.getOutput().getBytes()));

//     assertThat(result2.isSuccess())
//             .as("Re-upload should succeed")
//             .isTrue();

//     Allure.step("Verify that the new upload generates a different file ID");
//     assertThat(fileId2)
//             .as("Each upload of the same APK should create a new unique file ID")
//             .isNotEqualTo(fileId);

//     System.out.println("Test Passed: Different file IDs created (" + fileId + " vs " + fileId2 + ")");


// }
 @Test
    @Story("UPLOAD-005: Missing File Path Parameter")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that the CLI throws an error when upload command is executed without providing a file path.")
    @DisplayName("UPLOAD-005: Verify upload without file path parameter")
    public void testUploadWithoutFilePath() {
        Allure.step("Build CLI command for upload without file path");
        List<String> cmdList = new ArrayList<>();
        cmdList.add(config.getCliPath());
        cmdList.add("upload");

        Allure.step("Set valid environment variables");
        Map<String, String> env = new HashMap<>();
        env.put("APPKNOX_API_HOST", config.getValidHost());
        env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

        Allure.step("Execute CLI command");
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        Allure.step("Verify upload fails without file path");
        assertThat(result.isSuccess())
                .as("Command should fail without file path")
                .isFalse();

        Allure.step("Verify error message mentions missing argument");
        assertThat(result.getOutput().toLowerCase())
                .as("Output should indicate missing required parameter")
                .containsAnyOf("required", "missing", "argument", "parameter", "usage");

        Allure.addAttachment("UPLOAD-005 Command Output", new ByteArrayInputStream(result.getOutput().getBytes()));

        System.out.println("UPLOAD-005 Output: " + result.getOutput());
    }
    @Test
    @Story("UPLOAD-007: Invalid File Type Upload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that uploading a non-APK file results in an appropriate validation error.")
    @DisplayName("UPLOAD-007: Verify error when uploading a non-APK file")
    public void testInvalidFileTypeUpload() {
        Allure.step("Get invalid text file path from config");
        String invalidFilePath = config.getProperty(
                "test.file.invalid",
                "src/test/resources/testdata/test-files/invalid.txt"
        );

        Allure.step("Build CLI command for upload");
        List<String> cmdList = new ArrayList<>();
        cmdList.add(config.getCliPath());
        cmdList.add("upload");
        cmdList.add(invalidFilePath);

        Allure.step("Set valid environment variables");
        Map<String, String> env = new HashMap<>();
        env.put("APPKNOX_API_HOST", config.getValidHost());
        env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

        Allure.step("Execute upload command");
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        Allure.step("Verify output contains invalid file format message");
        assertThat(result.getOutput().toLowerCase())
                .as("Output should indicate invalid file format")
                .containsAnyOf("the app you are trying to scan is not a valid mobile application");

        Allure.addAttachment("UPLOAD-007 Command Output", new ByteArrayInputStream(result.getOutput().getBytes()));

        System.out.println("UPLOAD-007 Output: " + result.getOutput());
    }

    @Test
    @Story("UPLOAD-008: Upload APK with Special Characters in Filename")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that APK files with special characters in filename can be uploaded successfully.")
    @DisplayName("UPLOAD-008: Verify upload success for APK with special characters in filename")
    public void testSpecialCharacterFileNameUpload() {
        Allure.step("Get special character APK file path");
        String specialCharFilePath = config.getProperty(
                "test.apk.special",
                "src/test/resources/testdata/test-files/MFVA@$.apk"
        );

        Allure.step("Build CLI command for upload");
        List<String> cmdList = new ArrayList<>();
        cmdList.add(config.getCliPath());
        cmdList.add("upload");
        cmdList.add(specialCharFilePath);

        Allure.step("Set valid environment variables");
        Map<String, String> env = new HashMap<>();
        env.put("APPKNOX_API_HOST", config.getValidHost());
        env.put("APPKNOX_ACCESS_TOKEN", config.getValidToken());

        Allure.step("Execute CLI upload command");
        CommandResult result = cliExecutor.executeCommand(cmdList, env);

        Allure.step("Verify upload succeeds");
        assertThat(result.isSuccess())
                .as("Upload should succeed even if file name has special characters")
                .isTrue();

        Allure.addAttachment("UPLOAD-008 Command Output", new ByteArrayInputStream(result.getOutput().getBytes()));

        System.out.println("UPLOAD-008 Output - File ID: " + result.getOutput());
    }
}
