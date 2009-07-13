/*
 * Created on Nov 7, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.LightFilter;

public class UVIncludedLightFilter
    implements LightFilter
{
    private String m_Name;
    private int m_CutOff1;
    private int m_CutOff2;
    private int m_Type;

    public UVIncludedLightFilter()
    {
        m_Name = "UV Inc";
        m_CutOff1 = 0;
        m_CutOff2 = m_CutOff1;
        m_Type = TYPE_LOWPASS;
    }

    public String getName()
    {
        return m_Name;
    }

    public String getDisplayName()
    {
        return m_Name;
    }

    public int getCutoffWavelength2()
    {
        return m_CutOff2;
    }

    public int getCutoffWavelength1()
    {
        return m_CutOff1;
    }

    public boolean isBandpassFilter()
    {
        return ( m_Type == TYPE_BANDPASS );
    }

    public boolean isLowpassFilter()
    {
        return ( m_Type == TYPE_LOWPASS );
    }

    public boolean isHighpassFilter()
    {
        return ( m_Type == TYPE_HIGHPASS );
    }

    public boolean isNotchFilter()
    {
        return ( m_Type == TYPE_NOTCH );
    }

    public int getType()
    {
        return m_Type;
    }
}
