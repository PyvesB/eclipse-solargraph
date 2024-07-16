/*******************************************************************************
 * Copyright (c) 2019-2024 Pierre-Yves B. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Initial implementation
 *******************************************************************************/
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

	public CommandJob(String gemName, List<String> command, String description) {
		super(gemName);
		this.command = command;
		this.description = description;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		String commandString = "'" + String.join(" ", command) + "'";
		LogHelper.info("Running command " + commandString);
		monitor.beginTask(description, IProgressMonitor.UNKNOWN);
		try {
			process = new ProcessBuilder(command).start();
			monitorOutput(monitor, commandString);
			CompletableFuture<String> error = consumeError();
			int exitValue = process.waitFor();
			if (exitValue == 0) {
				return Status.OK_STATUS;
			} else {
				LogHelper.error("Unexpected exit value " + exitValue + " from command " + commandString
						+ System.lineSeparator() + "Error details:" + System.lineSeparator() + error.get());
			}
		} catch (IOException | ExecutionException e) {
			LogHelper.error("Exception whilst running command " + commandString, e);
		} catch (InterruptedException e) {
			LogHelper.error("Interrupted whilst waiting for completion of command " + commandString, e);
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

	private void monitorOutput(IProgressMonitor monitor, String commandString) {
		CompletableFuture.runAsync(() -> {
			try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				inputReader.lines().forEachOrdered(monitor::subTask);
			} catch (IOException e) {
				LogHelper.error("Failed to read output from command '" + commandString + "'", e);
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
