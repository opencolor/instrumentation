/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.Aperture;

public class MediumAperture
    implements Aperture
{
    String m_Name;
    double m_Radius;

    public MediumAperture()
    {
        m_Name = "MAV";
        m_Radius = 12.7d;
    }

    public String getName()
    {
        return m_Name;
    }

    public String getDisplayName()
    {
        return m_Name;
    }

    public double getDoorRadius()
    {
        return m_Radius;
    }

    public String toString()
    {
        return m_Name;
    }

}
