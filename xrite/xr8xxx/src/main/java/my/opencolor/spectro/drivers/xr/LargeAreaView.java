/*
 * Created on Nov 5, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.LensPosition;

public class LargeAreaView
    implements LensPosition
{
    String m_Name;
    double m_Radius;

    public LargeAreaView()
    {
        m_Name = "LAV";
        m_Radius = 19.0d;
    }

    public String getName()
    {
        return m_Name;
    }

    public String getDisplayName()
    {
        return m_Name;
    }

    public double getFocusRadius()
    {
        return m_Radius;
    }

    public String toString()
    {
        return m_Name;
    }

}
