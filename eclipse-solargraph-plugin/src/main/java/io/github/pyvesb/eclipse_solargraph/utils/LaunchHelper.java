/*******************************************************************************
 * Copyright (c) 2022-2024 Sean Champ and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Sean Champ - Initial implementation
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Various improvements
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;

public class LaunchHelper {

	public static Job createJob(ILaunch launch, String command, String workingDirectory) {
		return createJob(launch, command, new File(workingDirectory));
	}

	public static Job createJob(ILaunch launch, String command, File workingDirectory) {
		List<String> absolutePlatformCommand = CommandHelper.getAbsolutePlatformCommand(command);
		return Job.create("Running '" + command + "'", r -> {
			try {
				Process process = DebugPlugin.exec(absolutePlatformCommand.toArray(new String[0]), workingDirectory);
				if (process == null) {
					LogHelper.cancelled("Command cancelled: " + command);
					return;
				}
				CompletableFuture<Process> future = process.onExit();
				// initialize a process completion future to log any non-zero process exit
				future.defaultExecutor().execute(() -> {
					try {
						if (process.isAlive()) {
							process.waitFor();
						}
					} catch (InterruptedException e) {
						LogHelper.cancelled("Process monitor interrupted: " + command);
						return;
					}
					int exc = process.exitValue();
					if (exc != 0) {
						LogHelper.error(String.format("Process exited non-zero (%d): %s ", exc, command));
					}
				});
				DebugPlugin.newProcess(launch, process, command);
			} catch (CoreException e) {
				LogHelper.error("Exception when launching process: " + command, e);
				return;
			}
		});
	}

}
