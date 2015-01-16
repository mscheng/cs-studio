/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.vtype.pv.pva;

import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.epics.vtype.VByte;
import org.epics.vtype.VType;
import org.epics.vtype.VTypeToString;

/** Hold/decode data of {@link PVStructure} in {@link VType}
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
class VTypeForByte extends VTypeTimeAlarmDisplayBase implements VByte
{
    final private int value;

    public VTypeForByte(final PVStructure struct)
    {
        super(struct);
        value = PVStructureHelper.convert.toByte((PVScalar) struct.getSubField("value"));
    }

    @Override
    public Byte getValue()
    {
        return (byte) value;
    }

    @Override
    public String toString()
    {
        return VTypeToString.toString(this);
    }
}
