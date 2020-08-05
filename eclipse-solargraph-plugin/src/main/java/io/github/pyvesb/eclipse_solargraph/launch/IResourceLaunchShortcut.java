/*******************************************************************************
 * Copyright (c) 2019 Pierre-Yves B. and others.
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
package io.github.pyvesb.eclipse_solargraph.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

public interface IResourceLaunchShortcut extends ILaunchShortcut {

	@Override
	default void launch(ISelection selection, String mode) {
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
	default void launch(IEditorPart editor, String mode) {
		launchResource(editor.getEditorInput().getAdapter(IResource.class), mode);
	}

	void launchResource(IResource resource, String mode);

}
