/*
 * Created on Nov 12, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class InitializeFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        if( Xr8000Library.INSTANCE.Instrument_Initialize() )
        {
            status.addMessage( "MEASUREMENT_SUCCESS" );
        }
        else
        {
            status.addError( "MEASUREMENT_FAILED" );
        }
        return new SpectroEvent( this, status );
    }

}
