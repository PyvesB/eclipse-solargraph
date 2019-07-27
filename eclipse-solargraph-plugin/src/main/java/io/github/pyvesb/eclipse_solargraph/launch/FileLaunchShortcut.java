package io.github.pyvesb.eclipse_solargraph.launch;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import io.github.pyvesb.eclipse_solargraph.SolargraphPlugin;
import io.github.pyvesb.eclipse_solargraph.preferences.Preferences;
import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;
import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public class FileLaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof StructuredSelection) {
			Object firstElement = ((StructuredSelection) selection).getFirstElement();
			if (firstElement instanceof IFile) {
				launchRubyFile((IFile) firstElement);
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			launchRubyFile(((IFileEditorInput) editorInput).getFile());
		}
	}

	private void launchRubyFile(IFile rubyFile) {
		Launch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		String rubyPath = SolargraphPlugin.getPreferences().get(Preferences.RUBY_PATH, Preferences.RUBY_PATH_DEFAULT);
		List<String> command = CommandHelper.getPlatformCommand(rubyPath + " " + rubyFile.getLocation().toOSString());
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		String fileName = rubyFile.getName();
		Job.create("Running " + fileName, r -> {
			try {
				Process process = processBuilder.start();
				IProcess runtimeProcess = DebugPlugin.newProcess(launch, process, "ruby " + fileName);
				launch.addProcess(runtimeProcess);
			} catch (IOException e) {
				LogHelper.error("Exception whilst running " + fileName, e);
			}
		}).schedule();
	}

}
