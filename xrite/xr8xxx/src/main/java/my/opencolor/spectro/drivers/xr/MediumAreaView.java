/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.LensPosition;

/**
 * @author Administrator
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MediumAreaView
    implements LensPosition
{
    String m_Name;
    double m_Radius;

    public MediumAreaView()
    {
        m_Name = "MAV";
        m_Radius = 8.0d;
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.LensPosition#getName()
      */
    public String getName()
    {
        return m_Name;
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.LensPosition#getDisplayName()
      */
    public String getDisplayName()
    {
        return m_Name;
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.LensPosition#getFocusRadius()
      */
    public double getFocusRadius()
    {
        return m_Radius;
    }

    public String toString()
    {
        return m_Name;
    }

}