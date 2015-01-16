/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.vtype.pv.pva;

import org.epics.pvdata.property.AlarmStatus;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVStructure;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Time;
import org.epics.vtype.VType;

/** Base {@link VType} that decodes {@link Time} and {@link Alarm}
 *
 *  <p>Inspired by  org.epics.pvmanager.pva.adapters.AlarmTimeExtractor
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
class VTypeTimeAlarmBase implements Time, Alarm
{
    final private static Timestamp NO_TIME = Timestamp.of(0, 0);
    final private Timestamp timestamp;
    final private AlarmSeverity severity;
    final private String message;

    VTypeTimeAlarmBase(final PVStructure struct)
    {
        // Decode time_t timeStamp
        final PVStructure time = struct.getStructureField("timeStamp");
        if (time != null)
        {
            final PVLong sec = time.getLongField("secondsPastEpoch");
            final PVInt nano = time.getIntField("nanoSeconds");
            if (sec == null || nano == null)
                timestamp = NO_TIME;
            else
                timestamp = Timestamp.of(sec.get(), nano.get());
        }
        else
            timestamp = NO_TIME;

        // Decode alarm_t alarm
        final PVStructure alarm = struct.getStructureField("alarm");
        if (alarm != null)
        {
            PVInt code = alarm.getIntField("severity");
            severity = code == null
                ? AlarmSeverity.UNDEFINED
                : AlarmSeverity.values()[code.get()];

            code = alarm.getIntField("status");
            message = code == null
                ? AlarmStatus.UNDEFINED.name()
                : AlarmStatus.values()[code.get()].name();
        }
        else
        {
            severity = AlarmSeverity.UNDEFINED;
            message = AlarmStatus.UNDEFINED.name();
        }
    }

    // Time
    @Override
    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    // Time
    @Override
    public Integer getTimeUserTag()
    {
        // TODO Auto-generated method stub
        return null;
    }

    // Time
    @Override
    public boolean isTimeValid()
    {
        return timestamp != NO_TIME;
    }

    // Alarm
    @Override
    public AlarmSeverity getAlarmSeverity()
    {
        return severity;
    }

    // Alarm
    @Override
    public String getAlarmName()
    {
        return message;
    }
}
