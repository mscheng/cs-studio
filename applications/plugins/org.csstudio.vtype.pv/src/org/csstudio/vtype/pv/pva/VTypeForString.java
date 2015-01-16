/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.vtype.pv.pva;

import org.epics.pvdata.pv.PVStructure;
import org.epics.vtype.VString;
import org.epics.vtype.VType;
import org.epics.vtype.VTypeToString;

/** Hold/decode data of {@link PVStructure} in {@link VType}
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
class VTypeForString extends VTypeTimeAlarmBase implements VString
{
    final private String value;

    public VTypeForString(final PVStructure struct)
    {
        super(struct);
        value = struct.getStringField("value").get();
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return VTypeToString.toString(this);
    }
}
