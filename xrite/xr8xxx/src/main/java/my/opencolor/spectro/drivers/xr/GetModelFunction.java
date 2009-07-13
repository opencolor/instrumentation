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

public class GetModelFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        char[] outValue = new char[1024];
        Xr8000Library.INSTANCE.Instrument_GetModel( outValue );
        String model = new String( outValue );

        System.out.println( "Model : " + model );
        status.addMessage( "MODEL:" + model );
        return new SpectroEvent( this, status );
    }

}
