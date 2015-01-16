/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.propsheet;

import org.csstudio.swt.rtplot.undo.UndoableAction;
import org.csstudio.swt.rtplot.undo.UndoableActionManager;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;

/** Undo-able command to change item's line width
 *  @author Kay Kasemir
 */
public class ChangeSamplePeriodCommand extends UndoableAction
{
    final private Shell shell;
    final private PVItem item;
    final private double old_period, new_period;

    /** Register and perform the command
     *  @param shell Shell used for error dialogs
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param item Model item to configure
     *  @param new_period New value
     */
    public ChangeSamplePeriodCommand(final Shell shell,
            final UndoableActionManager operations_manager,
            final PVItem item, final double new_period)
    {
        super(Messages.ScanPeriod);
        this.shell = shell;
        this.item = item;
        this.old_period = item.getScanPeriod();
        this.new_period = new_period;
        try
        {
            item.setScanPeriod(new_period);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error,
                NLS.bind(Messages.ScanPeriodChangeErrorFmt, item.getName(), ex.getMessage()));
            // Exit before registering for undo because there's nothing to undo
            return;
        }
        operations_manager.add(this);
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        try
        {
            item.setScanPeriod(new_period);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error,
                NLS.bind(Messages.ScanPeriodChangeErrorFmt, item.getName(), ex.getMessage()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        try
        {
            item.setScanPeriod(old_period);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error,
                NLS.bind(Messages.ScanPeriodChangeErrorFmt, item.getName(), ex.getMessage()));
        }
    }
}
