package io.github.pyvesb.eclipse_solargraph.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class CommandJob extends Job {

	private final List<String> command;
	private final String description;
	private volatile Process process;

	public CommandJob(List<String> command, String description) {
		super("Solargraph");
		this.command = command;
		this.description = description;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		LogHelper.info("Running command " + command);
		monitor.beginTask(description, IProgressMonitor.UNKNOWN);
		try {
			process = new ProcessBuilder(command).start();
			monitorOutput(monitor);
			CompletableFuture<String> error = consumeError();
			int exitValue = process.waitFor();
			if (exitValue == 0) {
				return Status.OK_STATUS;
			} else {
				LogHelper.error("Unexpected exit value " + exitValue + " from command " + command + System.lineSeparator()
						+ "Error details:" + System.lineSeparator() + error.get(), null);
			}
		} catch (IOException | ExecutionException e) {
			LogHelper.error("Exception whilst running command " + command, e);
		} catch (InterruptedException e) {
			LogHelper.error("Interrupted whilst waiting for completion of command " + command, e);
			Thread.currentThread().interrupt();
		}
		return Status.CANCEL_STATUS;
	}

	@Override
	protected void canceling() {
		if (process != null) {
			process.destroyForcibly();
		}
	}

	private void monitorOutput(IProgressMonitor monitor) {
		CompletableFuture.runAsync(() -> {
			try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				inputReader.lines().forEachOrdered(monitor::subTask);
			} catch (IOException e) {
				LogHelper.error("Failed to read output from command " + command, e);
			}
		});
	}

	private CompletableFuture<String> consumeError() {
		return CompletableFuture.supplyAsync(() -> {
			try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
				return inputReader.lines().collect(Collectors.joining(System.lineSeparator()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
