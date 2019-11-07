package io.github.pyvesb.eclipse_solargraph.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

public interface ResourceLaunchShortcut extends ILaunchShortcut {

	@Override
	public default void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			IResource resource = null;
			if (element instanceof IResource) {
				resource = (IResource) element;
			} else if (element instanceof IAdaptable) {
				resource = ((IAdaptable) element).getAdapter(IResource.class);
			}

			if (resource != null) {
				launchResource(resource, mode);
				return;
			}
		}
		Display display = Display.getDefault();
		display.asyncExec(() -> MessageDialog.openError(display.getActiveShell(), "Launch failed",
				"Unable to launch selected Ruby resource."));
	}

	@Override
	public default void launch(IEditorPart editor, String mode) {
		launchResource(editor.getEditorInput().getAdapter(IResource.class), mode);
	}

	public abstract void launchResource(IResource resource, String mode);

}
