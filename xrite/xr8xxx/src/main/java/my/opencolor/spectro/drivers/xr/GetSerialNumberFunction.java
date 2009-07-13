/*
 * Created on Nov 7, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class GetSerialNumberFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        char[] outdata = new char[1024];
        Xr8000Library.INSTANCE.Instrument_GetSerialNo( outdata );
        String serialNo = new String( outdata );
        status.addMessage( "SERIAL_NUMBER:" + serialNo );
        return new SpectroEvent( status );
    }

}
