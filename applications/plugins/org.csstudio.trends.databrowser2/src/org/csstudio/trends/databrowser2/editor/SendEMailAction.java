/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.editor;

import org.csstudio.email.ui.AbstractSendEMailAction;
import org.csstudio.swt.rtplot.RTTimePlot;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/** Action for e-mailing snapshot of plot
 *  @author Kay Kasemir
 */
public class SendEMailAction extends AbstractSendEMailAction
{
    final private RTTimePlot graph;

    /** Initialize
     *  @param shell
     *  @param graph
     */
    public SendEMailAction(final Shell shell, final RTTimePlot graph)
    {
		super(shell, Preferences.getEmailDefaultSender(),
				Messages.LogentryDefaultTitle,
				Messages.LogentryDefaultBody);
        this.graph = graph;
    }

    /** {@inheritDoc} */
    @Override
    protected String getImage()
    {
        try
        {
            return new Screenshot(graph).getFilename();
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error, ex.getMessage());
            return null;
        }
    }
}
