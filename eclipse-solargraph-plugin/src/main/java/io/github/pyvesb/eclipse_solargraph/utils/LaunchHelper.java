package io.github.pyvesb.eclipse_solargraph.utils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;

public class LaunchHelper {

	public static Job createJob(ILaunch launch, String command, String workingDirectory) {
		return createJob(launch, command, new File(workingDirectory));
	}

	public static Job createJob(ILaunch launch, String command, File workingDirectory) {
		String[] absolutePlatformCommand = CommandHelper.getAbsolutePlatformCommand(command);
		return Job.create("Running '" + command + "'", r -> {
			try {
				Process process = DebugPlugin.exec(absolutePlatformCommand, workingDirectory);
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
						String msg = String.format("Process exited non-zero (%d): %s ", exc, command);
						LogHelper.error(msg);
					}
				});
				DebugPlugin.newProcess(launch, process, command);
			} catch (CoreException e) {
				// CoreException from exec
				LogHelper.log(IStatus.ERROR, "Exception when launching process: " + command, e);
				return;
			}
		});
	}

}
