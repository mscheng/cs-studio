package org.csstudio.config.ioconfig.commands;

import javax.annotation.Nonnull;

import org.apache.log4j.Logger;
import org.csstudio.config.ioconfig.editorinputs.NodeEditorInput;
import org.csstudio.config.ioconfig.editorparts.FacilityEditor;
import org.csstudio.config.ioconfig.model.FacilityDBO;
import org.csstudio.platform.logging.CentralLogger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * Caller for a Facility Editor with a new Facility. 
 * 
 * @author hrickens
 * @author $Author: $
 * @since 01.10.2010
 */
public class CallNewFacilityEditor extends AbstractHandler {
	private static final Logger LOG = CentralLogger.getInstance().getLogger(
			CallNewFacilityEditor.class);
	public static final String ID = "org.csstudio.config.ioconfig.commands.callNewFacilityEditor";

	@Override
	public Object execute(@Nonnull ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        
        FacilityDBO facilityDBO = new FacilityDBO();
        facilityDBO.setName("new Facility");
        facilityDBO.setSortIndex(0);
		NodeEditorInput input = new NodeEditorInput(facilityDBO, true);
		try {
			page.openEditor(input, FacilityEditor.ID);
		} catch (PartInitException e) {
			LOG.error("Can't open Facility Editor Error:", e);
		}		
		return null;
	}

}