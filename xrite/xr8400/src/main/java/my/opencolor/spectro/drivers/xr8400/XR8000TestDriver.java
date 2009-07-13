/*
 * Created on Nov 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package my.opencolor.spectro.drivers.xr8400;

import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroException;
import my.opencolor.spectro.spi.SpectroFunction;
import my.opencolor.spectro.spi.SpectroListener;
import my.opencolor.spectro.drivers.xr.InitializeFunction;
import my.opencolor.spectro.drivers.xr.SetConfigurationFunction;
import my.opencolor.spectro.drivers.xr.SmallAperture;
import my.opencolor.spectro.drivers.xr.UVIncludedLightFilter;
import my.opencolor.spectro.drivers.xr.MeasureFunction;
import my.opencolor.spectro.drivers.xr.CalibrateFunction;
import my.opencolor.spectro.drivers.xr.DataAvailableFunction;
import my.opencolor.spectro.drivers.xr.GetReflectanceFunction;

public class XR8000TestDriver
    implements SpectroListener
{
    public XR8400Spectro m_Spectro;

    public XR8000TestDriver()
    {
        m_Spectro = new XR8400Spectro();
        m_Spectro.addSpectroListener( this );
        m_Spectro.initialize();
    }

    public void measured( SpectroEvent event )
    {
        System.out.println( "Measured" );
    }

    public void calibrated( SpectroEvent event )
    {
        System.out.println( "Calibrated" );
    }

    public void settingsChanged( SpectroEvent event )
    {
        System.out.println( "Settings Changed" );
    }

    public void operationalStatusChanged( SpectroEvent event )
    {
        System.out.println( "Operational Status Changed" );
    }

    public static void main( String[] args ) throws SpectroException
    {
        SpectroFunction function = new InitializeFunction();
        function.invoke();

        function = new SetConfigurationFunction( new SmallAperture(), new UVIncludedLightFilter(), false, true );

        function.invoke();

        function = new CalibrateFunction();
        function.invoke();

        function = new MeasureFunction();
        function.invoke();

        function = new DataAvailableFunction();
        function.invoke();

        function = new GetReflectanceFunction();
        function.invoke();

        function = new DataAvailableFunction();
        function.invoke();

    }

    public void retrievedStandard( SpectroEvent event )
    {
        // TODO Auto-generated method stub

    }

    public void retrievedSample( SpectroEvent event )
    {
        // TODO Auto-generated method stub

    }

    public void numberStandardsFound( int[] indices )
    {
        // TODO Auto-generated method stub

    }

    public void numberSamplesFound( int[] indices )
    {
        // TODO Auto-generated method stub

    }
}
