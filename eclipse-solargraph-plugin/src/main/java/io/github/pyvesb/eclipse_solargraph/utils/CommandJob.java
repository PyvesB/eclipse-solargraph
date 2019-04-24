package io.github.pyvesb.eclipse_solargraph.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import io.github.pyvesb.eclipse_solargraph.SolargraphPlugin;

public class CommandJob extends Job {

	private static final ILog LOGGER = SolargraphPlugin.getDefault().getLog();

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
		logInfo("Running command " + command);
		monitor.beginTask(description, IProgressMonitor.UNKNOWN);
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectError(Redirect.INHERIT);
		try {
			process = processBuilder.start();
			return monitorAndAwaitTermination(monitor);
		} catch (IOException e) {
			logError("Exception whilst running command " + command, e);
		} catch (InterruptedException e) {
			logError("Interrupted whilst waiting for completion of command " + command, e);
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

	private IStatus monitorAndAwaitTermination(IProgressMonitor monitor) throws IOException, InterruptedException {
		try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			CompletableFuture.runAsync(() -> inputReader.lines().forEachOrdered(monitor::subTask));
			int exitValue = process.waitFor();
			if (exitValue == 0) {
				return Status.OK_STATUS;
			} else {
				logError("Unexpected exit value " + exitValue + " from command " + command, null);
				return Status.CANCEL_STATUS;
			}
		}
	}

	private void logInfo(String message) {
		LOGGER.log(new Status(IStatus.INFO, SolargraphPlugin.PLUGIN_ID, message));
	}

	private void logError(String message, Throwable exception) {
		LOGGER.log(new Status(IStatus.ERROR, SolargraphPlugin.PLUGIN_ID, message, exception));
	}

}
