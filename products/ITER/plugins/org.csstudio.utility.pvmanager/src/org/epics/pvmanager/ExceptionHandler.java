/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class receives all the exceptions generated by a PV.
 * {@link #handleException(java.lang.Exception) } is called on the thread that
 * generated the exception. It's up to the handler to handle thread safety
 * and notification.
 *
 * @author carcassi
 */
public class ExceptionHandler {

    private static final Logger log = Logger.getLogger(ExceptionHandler.class.getName());

    /**
     * Notifies of an exception being thrown.
     * 
     * @param ex the exception
     */
    public void handleException(Exception ex) {
        log.log(Level.INFO, "Exception for PV", ex);
    }
    
    static ExceptionHandler safeHandler(final ExceptionHandler exceptionHandler) {
        return new ExceptionHandler() {

            @Override
            public void handleException(Exception ex) {
                try {
                    exceptionHandler.handleException(ex);
                } catch(RuntimeException e) {
                    log.log(Level.INFO, "Exception handler throw an exception", e);
                }
            }
            
        };
    }
}