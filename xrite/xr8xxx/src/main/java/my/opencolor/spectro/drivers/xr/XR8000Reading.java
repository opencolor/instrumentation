/*
 * Created on Nov 7, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroReading;
import my.opencolor.spectro.spi.SpectroSettings;
import my.opencolor.spectro.spi.SpectroStatus;
import java.util.Map;

public class XR8000Reading implements SpectroReading
{
    SpectroStatus m_Status;
    Map m_Values;
    SpectroSettings m_Settings;

    public XR8000Reading( SpectroStatus status, SpectroSettings settings, Map values )
    {
        m_Status = status;
        m_Settings = settings;
        m_Values = values;
    }

    public SpectroStatus getStatus()
    {
        return m_Status;
    }

    public Map getValues()
    {
        return m_Values;
    }

    public SpectroSettings getSettings()
    {
        return m_Settings;
    }

    public void setSettings( SpectroSettings settings )
    {
        m_Settings = settings;
    }
}
