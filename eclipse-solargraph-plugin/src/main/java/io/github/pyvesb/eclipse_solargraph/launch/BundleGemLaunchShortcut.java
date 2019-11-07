package io.github.pyvesb.eclipse_solargraph.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;

import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;

public class BundleGemLaunchShortcut implements ResourceLaunchShortcut {

	@Override
	public void launchResource(IResource resource, String mode) {
		Launch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		String command = getBaseCommand(resource) + resource.getLocation().toOSString();
		String[] absolutePlatformCommand = CommandHelper.getAbsolutePlatformCommand(command);
		Job.create("Running " + command, r -> {
			Process process = DebugPlugin.exec(absolutePlatformCommand, null);
			DebugPlugin.newProcess(launch, process, command);
		}).schedule();
	}

	private String getBaseCommand(IResource resource) {
		return "Gemfile".equals(resource.getName()) ? "bundle install --gemfile=" : "gem build ";
	}

}
