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
import org.csstudio.trends.databrowser2.model.ModelItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;

/** Undo-able command to change item's name
 *  @author Kay Kasemir
 */
public class ChangeNameCommand extends UndoableAction
{
    final private Shell shell;
    final private ModelItem item;
    final private String old_name, new_name;

    /** Register and perform the command
     *  @param shell Shell used for error dialogs
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param item Model item to configure
     *  @param new_name New value
     */
    public ChangeNameCommand(final Shell shell,
            final UndoableActionManager operations_manager,
            final ModelItem item, final String new_name)
    {
        super(Messages.ItemName);
        this.shell = shell;
        this.item = item;
        this.old_name = item.getName();
        this.new_name = new_name;
        try
        {
            item.setName(new_name);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error,
                    NLS.bind(Messages.ChangeNameErrorFmt,
                            new Object[] { old_name, new_name, ex.getMessage()}));
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
            item.setName(new_name);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error,
                    NLS.bind(Messages.ChangeNameErrorFmt,
                            new Object[] { old_name, new_name, ex.getMessage()}));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        try
        {
            item.setName(old_name);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, Messages.Error,
                    NLS.bind(Messages.ChangeNameErrorFmt,
                            new Object[] { new_name, old_name, ex.getMessage()}));
        }
    }
}
