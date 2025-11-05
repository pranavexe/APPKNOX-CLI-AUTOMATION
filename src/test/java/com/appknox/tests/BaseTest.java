package com.appknox.tests;

import com.config.ConfigManager;
import com.appknox.core.CLIExecutor;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest {
    protected static ConfigManager config;
    protected static CLIExecutor cliExecutor;

    @BeforeAll
    public static void setupBase() {
        config = ConfigManager.getInstance();
        cliExecutor = new CLIExecutor(config.getCommandTimeout());
    }

    protected CLIExecutor.CommandResult runAppknoxCommand(String command,String host,String token) 
    {
        List<String> cmdList = new ArrayList<>();
        cmdList.add(config.getCliPath());
        cmdList.add(command);

        Map<String, String> env = new HashMap<>();
        if (host != null && !host.isEmpty()) {
            env.put("APPKNOX_API_HOST", host);
        }
        if (token != null && !token.isEmpty()) {
            env.put("APPKNOX_ACCESS_TOKEN", token);
        }

        return cliExecutor.executeCommand(cmdList, env);
    }

    protected CLIExecutor.CommandResult runAppknoxCommand(String command) {
        return runAppknoxCommand(command, config.getValidHost(), config.getValidToken());
    }

    protected void clearEnvironmentVariables() {
        //  unset env var if nedded
        
        //  not set iin the command
    }
}
