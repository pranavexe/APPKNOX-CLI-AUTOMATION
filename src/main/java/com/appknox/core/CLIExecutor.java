package com.appknox.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CLIExecutor {
    private final int timeoutSeconds;

    public CLIExecutor(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public CommandResult executeCommand(List<String> command, Map<String, String> env) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);

            Map<String, String> processEnv = pb.environment();
            processEnv.put("PATH", System.getenv("PATH"));
            
            // Set env variableee
            if (env != null && !env.isEmpty()) {
                
                processEnv.putAll(env);
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                return new CommandResult(-1, "Command timeout after " + timeoutSeconds + " seconds", true);
            }

            int exitCode = process.exitValue();
            return new CommandResult(exitCode, output.toString().trim(), false);

        } catch (Exception e) {
            return new CommandResult(-1, "Exception: " + e.getMessage(), false);
        }
    }

    public static class CommandResult {
        private final int exitCode;
        private final String output;
        private final boolean timeout;

        public CommandResult(int exitCode, String output, boolean timeout) {
            this.exitCode = exitCode;
            this.output = output;
            this.timeout = timeout;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }

        public boolean isTimeout() {
            return timeout;
        }

        public boolean isSuccess() {
            return exitCode == 0;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "exitCode=" + exitCode +
                    ", output='" + output + '\'' +
                    ", timeout=" + timeout +
                    '}';
        }
    }
}