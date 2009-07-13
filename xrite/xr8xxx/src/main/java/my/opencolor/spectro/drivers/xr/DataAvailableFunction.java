/*
 * Created on Nov 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class DataAvailableFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        if( Xr8000Library.INSTANCE.Instrument_IsDataAvailable() )
        {
            status.addMessage( "DATA_AVAILABLE" );
        }
        else
        {
            status.addMessage( "DATA_NOT_AVAILABLE" );
        }
        return new SpectroEvent( this, status );
    }

}
