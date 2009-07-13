/*
 * Created on Nov 5, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import javax.swing.JOptionPane;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class MeasureFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        if( Xr8000Library.INSTANCE.Instrument_Measure() )
        {
            status.addMessage( "MEASUREMENT_SUCCESS" );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "MEASUREMENT_FAILED" );
            status.addError( "MEAUSREMENT_FAILED" );
        }
        return new SpectroEvent( this, status );
    }
}
