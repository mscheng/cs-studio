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
import org.csstudio.trends.databrowser2.model.ArchiveDataSource;
import org.csstudio.trends.databrowser2.model.PVItem;

/** Undo-able command to delete archive data sources from a PVItem
 *  @author Kay Kasemir
 */
public class DeleteArchiveCommand extends UndoableAction
{
    final private PVItem pv;
    final private ArchiveDataSource archives[];
    final private ArchiveDataSource original[];

    /** Register and perform the command
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param pv PV from which to delete archives
     *  @param archives Archive data sources to remove
     */
    public DeleteArchiveCommand(final UndoableActionManager operations_manager,
            final PVItem pv, final ArchiveDataSource archives[])
    {
        super(Messages.DeleteArchive);
        this.pv = pv;
        this.archives = archives;
        original = pv.getArchiveDataSources();
        operations_manager.execute(this);
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        pv.removeArchiveDataSource(archives);
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        pv.setArchiveDataSource(original);
    }
}
