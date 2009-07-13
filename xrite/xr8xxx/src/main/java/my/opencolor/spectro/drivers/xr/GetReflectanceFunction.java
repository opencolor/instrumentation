/*
 * Created on Nov 5, 2003
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import java.util.Map;
import java.util.TreeMap;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class GetReflectanceFunction
    implements SpectroFunction
{
    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();
        Map<Double, Double> values = new TreeMap<Double, Double>();
        float[] outdata = new float[39];
        Xr8000Library.INSTANCE.VB_Instrument_GetReflectances( outdata );
        double currentWavelength = 360.0d;
        double interval = 10.0d;
        for( float aResultArray : outdata )
        {
            System.out.println( currentWavelength + "," + aResultArray );
            values.put( currentWavelength, (double) aResultArray / 100 );
            currentWavelength += interval;
        }
        return new SpectroEvent( this, new XR8000Reading( status, null, values ) );
    }

}
