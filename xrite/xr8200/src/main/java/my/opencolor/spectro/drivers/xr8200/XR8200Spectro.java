package my.opencolor.spectro.drivers.xr8200;

import my.opencolor.spectro.spi.NotSupportedException;
import my.opencolor.spectro.spi.SpectroException;
import my.opencolor.spectro.spi.SpectroListener;
import my.opencolor.spectro.spi.SpectroReading;
import my.opencolor.spectro.spi.SpectroSettings;
import my.opencolor.spectro.spi.Spectrophotometer;
import java.net.URI;

public class XR8200Spectro
    implements Spectrophotometer
{
    SpectroSettings m_Settings;

    public void measure()
        throws SpectroException
    {
        // TODO

    }

    public void calibrate( int step )
        throws SpectroException
    {
        // TODO

    }

    public SpectroSettings getSettings()
    {
        // TODO
        return null;
    }

    public void setSettings( SpectroSettings newSettings )
    {
        // TODO

    }

    public String getSerialNo()
    {
        // TODO
        return null;
    }

    public void retrieveStoredSamples()
        throws SpectroException
    {
        // TODO

    }

    public void retrieveStoredSample( int position )
        throws SpectroException
    {
        // TODO

    }

    public void setStandard( int position, SpectroReading reading )
        throws SpectroException
    {
        // TODO

    }

    public void retrieveStandards()
        throws SpectroException
    {
        // TODO

    }

    public void retrieveStandard( int position )
        throws SpectroException
    {
        // TODO

    }

    public void initialize()
    {
        // TODO

    }

    public void dispose()
    {
        // TODO

    }

    public void removeSpectroListener( SpectroListener listener )
    {
        // TODO

    }

    public void addSpectroListener( SpectroListener listener )
    {
        // TODO

    }

    public int getOperationalStatus()
    {
        return 0;
    }

    public void setCalibrationDataFiles( URI[] fileURIs )
    {
        //TODO: Mar 9, 2004 - found not done
    }

    public void queryNoOfStoredSamples() throws SpectroException
    {
        throw new NotSupportedException( "MSG_OFFLINE_UNSUPPORTED" );
    }

    public void queryNoOfStoredStandards() throws SpectroException
    {
        throw new NotSupportedException( "MSG_OFFLINE_UNSUPPORTED" );
    }
}
