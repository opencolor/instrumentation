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

public class GetConfigFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        char[] outValue = new char[1024];
        Xr8000Library.INSTANCE.Instrument_GetConfigString( outValue );
        String configStr = new String( outValue );
        System.out.println( "Config String : " + configStr );
        status.addMessage( "CONFIG:" + configStr );
        return new SpectroEvent( this, status );
    }
}
