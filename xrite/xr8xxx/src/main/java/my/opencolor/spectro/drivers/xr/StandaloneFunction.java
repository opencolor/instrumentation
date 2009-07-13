/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class StandaloneFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        Xr8000Library.INSTANCE.Instrument_Standalone();
        return new SpectroEvent( this, status );

    }
}
