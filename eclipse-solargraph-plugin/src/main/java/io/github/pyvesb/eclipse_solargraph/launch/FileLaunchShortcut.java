package io.github.pyvesb.eclipse_solargraph.launch;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import java.io.File;
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

import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;
import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public abstract class FileLaunchShortcut implements ILaunchShortcut {

	private String baseCommand;

	public FileLaunchShortcut(String baseCommand) {
		this.baseCommand = baseCommand;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof StructuredSelection) {
			Object firstElement = ((StructuredSelection) selection).getFirstElement();
			if (firstElement instanceof IFile) {
				launchFile((IFile) firstElement);
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			launchFile(((IFileEditorInput) editorInput).getFile());
		}
	}

	private void launchFile(IFile file) {
		Launch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		List<String> command = getFileCommand(file);
		String commandSummary = baseCommand + file.getName();
		Job.create("Running " + commandSummary, r -> {
			try {
				Process process = new ProcessBuilder(command).start();
				IProcess runtimeProcess = DebugPlugin.newProcess(launch, process, commandSummary);
				launch.addProcess(runtimeProcess);
			} catch (IOException e) {
				LogHelper.error("Exception whilst running " + command, e);
			}
		}).schedule();
	}

	private List<String> getFileCommand(IFile file) {
		StringBuilder command = new StringBuilder();
		if (!SYSTEM_RUBY.getValue()) {
			command.append(RUBY_DIR.getValue());
			if (command.length() > 0) {
				command.append(File.separator);
			}
		}
		command.append(baseCommand);
		command.append(file.getLocation().toOSString());
		return CommandHelper.getPlatformCommand(command.toString());
	}

}
