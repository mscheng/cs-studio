/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.widgets.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.csstudio.opibuilder.model.AbstractContainerModel;
import org.csstudio.opibuilder.model.AbstractWidgetModel;
import org.csstudio.opibuilder.properties.IWidgetPropertyChangeHandler;
import org.csstudio.opibuilder.widgets.figures.NativeTextFigure;
import org.csstudio.opibuilder.widgets.model.NativeTextModel;
import org.csstudio.opibuilder.widgets.model.TextInputModel;
import org.csstudio.swt.widgets.figures.ITextFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The editpart for native text widget.
 * 
 * @author Xihui Chen
 * 
 */
public class NativeTextEditpart extends TextInputEditpart {


	private Text text;
	
	@Override
	public NativeTextModel getWidgetModel() {
		return (NativeTextModel) getModel();
	}

	@Override
	protected IFigure doCreateFigure() {
		initFields();
		
		int style=SWT.NONE;
		NativeTextModel model = getWidgetModel();
		if(model.isShowNativeBorder())
			style |= SWT.BORDER;
		if(model.isMultilineInput()){
			style |= SWT.MULTI;
			if(model.isShowHScroll())
				style |= SWT.H_SCROLL;
			if(model.isShowVScroll())
				style |= SWT.V_SCROLL;
			if(model.isWrapWords())
				style |= SWT.WRAP;
		}else{
			style |= SWT.SINGLE;
			if(model.isPasswordInput())
				style |= SWT.PASSWORD;
		}
		if(model.isReadOnly())
			style |= SWT.READ_ONLY;
		switch (model.getHorizontalAlignment()) {
		case CENTER:
			style |= SWT.CENTER;
			break;
		case LEFT:
			style |= SWT.LEFT;
			break;
		case RIGHT:
			style |= SWT.RIGHT;
		default:
			break;
		}		
		
		NativeTextFigure figure = new NativeTextFigure(this, style);
		text = figure.getSWTWidget();

		if(!model.isReadOnly()){
			if(model.isMultilineInput()){
				text.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent keyEvent) {
						if (keyEvent.character == '\r') { // Return key	            
				            if (text != null && !text.isDisposed()
				                    && (text.getStyle() & SWT.MULTI) != 0) {
				                if ((keyEvent.stateMask & SWT.CTRL) != 0) {
				                	outputText(text.getText());
				                  keyEvent.doit=false;
				                  text.getShell().setFocus();
				                }
				            }
				            
						}
					}
				});
			}else {
				text.addListener (SWT.DefaultSelection, new Listener () {
					public void handleEvent (Event e) {
						outputText(text.getText());
		                text.getShell().setFocus();
					}
				});
			}
		}
		
		getPVWidgetEditpartDelegate().setUpdateSuppressTime(-1);
		updatePropSheet();
		return figure;
	}

	@Override
	protected void outputText(String newValue) {
		setPropertyValue(NativeTextModel.PROP_TEXT, newValue);
	}
	
	@Override
	protected void updatePropSheet() {
		super.updatePropSheet();
		boolean isMulti = getWidgetModel().isMultilineInput();
		getWidgetModel().setPropertyVisible(NativeTextModel.PROP_SHOW_H_SCROLL, isMulti);
		getWidgetModel().setPropertyVisible(NativeTextModel.PROP_SHOW_V_SCROLL, isMulti);
		getWidgetModel().setPropertyVisible(NativeTextModel.PROP_WRAP_WORDS, isMulti);
		getWidgetModel().setPropertyVisible(NativeTextModel.PROP_PASSWORD_INPUT, !isMulti);	
	}


	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
//		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, null);
	}

	
	@Override
	protected void setFigureText(String text) {
		this.text.setText(text);
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		super.registerPropertyChangeHandlers();	
		removeAllPropertyChangeHandlers(NativeTextModel.PROP_ALIGN_H);
		
		PropertyChangeListener updatePropSheetListener = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updatePropSheet();
			}
		};
		
		getWidgetModel().getProperty(NativeTextModel.PROP_MULTILINE_INPUT)
			.addPropertyChangeListener(updatePropSheetListener);
		
		IWidgetPropertyChangeHandler handler = new IWidgetPropertyChangeHandler() {
			
			@Override
			public boolean handleChange(Object oldValue, Object newValue, IFigure figure) {
				AbstractWidgetModel model = getWidgetModel();
				AbstractContainerModel parent = model.getParent();
				parent.removeChild(model);
				parent.addChild(model);
				parent.selectWidget(model, true);
				return false;
			}
		};
		setPropertyChangeHandler(NativeTextModel.PROP_SHOW_NATIVE_BORDER, handler);
		setPropertyChangeHandler(NativeTextModel.PROP_MULTILINE_INPUT, handler);
		setPropertyChangeHandler(NativeTextModel.PROP_WRAP_WORDS, handler);
		setPropertyChangeHandler(NativeTextModel.PROP_SHOW_H_SCROLL, handler);
		setPropertyChangeHandler(NativeTextModel.PROP_SHOW_V_SCROLL, handler);
		setPropertyChangeHandler(NativeTextModel.PROP_PASSWORD_INPUT, handler);
		setPropertyChangeHandler(NativeTextModel.PROP_ALIGN_H, handler);
		
	}	

	@Override
	protected String formatValue(Object newValue, String propId) {
		String text = super.formatValue(newValue, propId);
		getWidgetModel()
				.setPropertyValue(TextInputModel.PROP_TEXT, text, false);
		return text;

	}
	
	@Override
	protected void performAutoSize() {
		getWidgetModel().setSize(((NativeTextFigure)getFigure()).
				getAutoSizeDimension());
	}
	
	@Override
	public String getValue() {
		return text.getText();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if(key == ITextFigure.class)
			return getFigure();

		return super.getAdapter(key);
	}
	
	
	

}