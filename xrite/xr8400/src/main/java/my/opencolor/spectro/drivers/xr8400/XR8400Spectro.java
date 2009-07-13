/*
 * Created on Nov 7, 2003
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr8400;

import java.net.URI;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroException;
import my.opencolor.spectro.spi.SpectroFunction;
import my.opencolor.spectro.spi.SpectroListener;
import my.opencolor.spectro.spi.SpectroReading;
import my.opencolor.spectro.spi.SpectroSettings;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.Spectrophotometer;
import my.opencolor.spectro.spi.helpers.Fifo;
import my.opencolor.spectro.spi.helpers.FifoFullException;
import my.opencolor.spectro.drivers.xr.CalibrateFunction;
import my.opencolor.spectro.drivers.xr.MeasureFunction;
import my.opencolor.spectro.drivers.xr.SmallAperture;
import my.opencolor.spectro.drivers.xr.UVIncludedLightFilter;
import my.opencolor.spectro.drivers.xr.SmallAreaView;
import my.opencolor.spectro.drivers.xr.LargeAreaView;
import my.opencolor.spectro.drivers.xr.LargeAperture;
import my.opencolor.spectro.drivers.xr.MediumAreaView;
import my.opencolor.spectro.drivers.xr.MediumAperture;
import my.opencolor.spectro.drivers.xr.SetConfigurationFunction;
import my.opencolor.spectro.drivers.xr.GetModelFunction;
import my.opencolor.spectro.drivers.xr.GetConfigFunction;
import my.opencolor.spectro.drivers.xr.InitializeFunction;
import my.opencolor.spectro.drivers.xr.GetReflectanceFunction;
import my.opencolor.spectro.drivers.xr.XR8000Reading;
import my.opencolor.spectro.drivers.xr.GetSerialNumberFunction;
import my.opencolor.spectro.drivers.xr.CutOff420LightFilter;
import my.opencolor.spectro.drivers.xr.CutOff400LightFilter;

public class XR8400Spectro
    implements Spectrophotometer, Runnable
{
    static private Logger m_Logger = Logger.getLogger( XR8400Spectro.class.getName() );

    private SpectroSettings m_Settings;
    private String m_Serial = "";
    private String m_Model = "<XR>8400";

    protected Fifo m_Outgoing; // Queued function calls

    protected Vector m_Listeners;
    protected Thread m_Thread;

    protected boolean running;
    protected boolean stopRequest;
    protected int m_OpStatus;

    public XR8400Spectro()
    {
        running = false;
        stopRequest = false;

        m_OpStatus = OPERATIONAL_STATUS_INITIALIZING;
        notifyStatusChange( new SpectroEvent( this ) );
        initialize();

        m_OpStatus = OPERATIONAL_STATUS_IDLE;
        notifyStatusChange( new SpectroEvent( this ) );
    }

    public void measure() throws SpectroException
    {
        try
        {
            m_Outgoing.putObject( new MeasureFunction() );
            m_OpStatus = Spectrophotometer.OPERATIONAL_STATUS_SENDING;
            notifyStatusChange( new SpectroEvent( this ) );
        }
        catch( FifoFullException fifoEx )
        {
            throw new SpectroException( "MSG_SPECTRO_BUSY" );
        }
    }

    public void calibrate( int step )
        throws SpectroException
    {
        try
        {
            m_Outgoing.putObject( new CalibrateFunction() );
            m_OpStatus = OPERATIONAL_STATUS_SENDING;
            notifyStatusChange( new SpectroEvent( this ) );
        }
        catch( FifoFullException fifoEx )
        {
            throw new SpectroException( "MSG_SPECTRO_BUSY" );
        }
    }

    public SpectroSettings getSettings()
    {
        return m_Settings;
    }

    public void setSettings( SpectroSettings newSettings )
    {
        if( m_Settings == null )
        {
            m_Settings = new SpectroSettings();
            m_Settings.setAperture( new SmallAperture() );
            m_Settings.setLightFilter( new UVIncludedLightFilter() );
            m_Settings.setLensPosition( new SmallAreaView() );
            m_Settings.setSpecular( true );
        }

        if( newSettings.getAperture() != null
            && m_Settings.getAperture() != newSettings.getAperture() )
        {
            if( newSettings.getAperture() instanceof SmallAperture )
            {
                m_Settings.setAperture( newSettings.getAperture() );
                m_Settings.setLensPosition( new SmallAreaView() );
            }

            if( newSettings.getAperture() instanceof MediumAperture )
            {
                m_Settings.setAperture( newSettings.getAperture() );
                m_Settings.setLensPosition( new MediumAreaView() );
            }

            if( newSettings.getAperture() instanceof LargeAperture )
            {
                m_Settings.setAperture( newSettings.getAperture() );
                m_Settings.setLensPosition( new LargeAreaView() );
            }
        }

        if( newSettings.getLightFilter() != null
            && m_Settings.getLightFilter() != newSettings.getLightFilter() )
        {
            m_Settings.setLightFilter( newSettings.getLightFilter() );
        }

        if( newSettings.getSpecular() != m_Settings.getSpecular() )
        {
            m_Settings.setSpecular( newSettings.getSpecular() );
        }

        try
        {
            SetConfigurationFunction function = new SetConfigurationFunction( m_Settings.getAperture(),
                                                                              m_Settings.getLightFilter(),
                                                                              m_Settings.getSpecular(),
                                                                              true );
            m_Outgoing.putObject( function );
        }
        catch( FifoFullException e )
        {
            m_Logger.warning( "SPECTRO_BUSY" );
        }
        catch( SpectroException e )
        {
            m_Logger.warning( e.getMessage() );
        }
    }

    public String getSerialNo()
    {
        return m_Serial;
    }

    public void retrieveStoredSamples() throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );
    }

    public void retrieveStoredSample( int position ) throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );
    }

    public void setStandard( int position, SpectroReading reading )
        throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );
    }

    public void retrieveStandards() throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );
    }

    public void retrieveStandard( int position ) throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );
    }

    /**
     * Initializes the spectrophotomer; but does not open connection to the
     * serial port The initialization process covers the instantiation of the
     * FIFO queues but does not open the connection and start the send thread.
     */
    public void initialize()
    {
        m_Outgoing = new Fifo( 10 );

        m_Thread = new Thread( this );
        running = true;
        m_Thread.start();

        //Do nothing
        m_OpStatus = OPERATIONAL_STATUS_INITIALIZING;
        notifyStatusChange( new SpectroEvent( this ) );

        try
        {
            m_Outgoing.putObject( new InitializeFunction() );
            m_Outgoing.putObject( new GetConfigFunction() );
            m_Outgoing.putObject( new GetModelFunction() );
            //m_Outgoing.putObject( new GetSerialNumberFunction() );
        }
        catch( FifoFullException e )
        {
            m_Logger.severe( "SPECTRO_BUSY" );
        }
    }

    public void dispose()
    {
        stopRequest = true;
        running = false;
        m_Thread.interrupt();
    }

    public void removeSpectroListener( SpectroListener listener )
    {
        if( m_Listeners != null )
        {
            m_Listeners.remove( listener );
        }
    }

    public void addSpectroListener( SpectroListener listener )
    {
        if( m_Listeners == null )
        {
            m_Listeners = new Vector();
        }

        m_Listeners.add( listener );
    }

    public int getOperationalStatus()
    {
        return m_OpStatus;
    }

    public void setCalibrationDataFiles( URI[] fileURIs )
    {
        //Not supported by the X-Rite 8400
    }


    /**
     * Notifies registered listeners of a measurement event
     *
     * @param evt The event to be sent or triggerred
     */
    public void notifyMeasured( SpectroEvent evt )
    {
        if( m_Listeners != null )
        {
            m_Logger.log( Level.INFO, "Measurement received" );
            Iterator list = m_Listeners.iterator();

            while( list.hasNext() )
            {
                ( (SpectroListener) list.next() ).measured( evt );
            }
        }

    }

    /**
     * Notifies registered listeners of a calibration event
     *
     * @param evt The event to be triggerred
     */
    public void notifyCalibrated( SpectroEvent evt )
    {
        if( m_Listeners != null )
        {
            m_Logger.log( Level.INFO, "Calibration received" );
            Iterator list = m_Listeners.iterator();

            while( list.hasNext() )
            {
                ( (SpectroListener) list.next() ).calibrated( evt );
            }
        }
    }

    /**
     * Notifies registered listeners of setting changes
     *
     * @param evt The event to be triggered
     */
    public void notifySettingsChanged( SpectroEvent evt )
    {
        if( m_Listeners != null )
        {
            Iterator list = m_Listeners.iterator();

            while( list.hasNext() )
            {
                ( (SpectroListener) list.next() ).settingsChanged( evt );
            }
        }
    }

    /**
     * Notifies registered listeners of status changes and errors
     *
     * @param evt The event or error to be triggered
     */
    public void notifyStatusChange( SpectroEvent evt )
    {
        if( m_Listeners != null )
        {
            m_Logger.log( Level.INFO, "Status change " );
            Iterator list = m_Listeners.iterator();

            while( list.hasNext() )
            {
                ( (SpectroListener) list.next() ).operationalStatusChanged( evt );
            }
        }
    }


    /**
     * The thread that takes a command from the outgoing FIFO and sends it.
     * This thread is responsible for taking the constructed command, computing
     * the appropriate checksum and terminating it then send it.
     *
     * If the incoming FIFO is full, the thread would not send commands until
     * the incoming FIFO is available for more elements to be added.
     */
    public void run()
    {
        while( running )
        {
            if( stopRequest )
            {
                running = false;
                break;
            }

            //If the incoming FIFO is not full, send first command in outgoing
            // queue
            if( !m_Outgoing.isEmpty() )
            {
                m_Logger.log( Level.INFO, "Thread still running" );
                m_Logger.log( Level.INFO, "Retreiving Command from fifo" );

                //Retrieve the command
                SpectroFunction cmd = (SpectroFunction) m_Outgoing.getNextObject();

                invokeFunction( cmd );

                m_Logger.log( Level.INFO, "Retrieving function" );
                if( cmd == null )
                {
                    continue;
                }
                else
                {
                    m_Logger.log( Level.INFO, "Message posted" );
                }
            }
            else
            {
                //Incoming FIFO is full, spectro is currently busy
                //System.out.println( "SF300 Spectro : Spectro busy." );
            }

            try
            {
                Thread.sleep( 1000 );
            }
            catch( InterruptedException irEx )
            {
                if( stopRequest )
                {
                    running = false;
                    break;
                }
            }
        }

        //Indicate that the thread has stopped running
        m_Logger.log( Level.INFO, "Thread stopped." );
    }

    /**
     * @param cmd
     */
    private void invokeFunction( SpectroFunction cmd )
    {
        SpectroEvent evt = cmd.invoke();

        if( evt != null )
        {

            if( cmd instanceof GetReflectanceFunction )
            {
                XR8000Reading reading = (XR8000Reading) evt.getReading();
                reading.setSettings( m_Settings );

                notifyMeasured( evt );
            }
            else if( cmd instanceof MeasureFunction )
            {
                SpectroStatus status = evt.getStatus();

                if( status.isSuccess() )
                {
                    try
                    {
                        m_Outgoing.putObject( new GetReflectanceFunction() );
                    }
                    catch( FifoFullException ex )
                    {
                        m_Logger.info( "SPECTRO_BUSY" );
                    }
                }
                else
                {
                    m_Logger.warning( "MEASURE_FAILED" );
                }
            }
            else if( cmd instanceof CalibrateFunction )
            {
                notifyCalibrated( evt );
            }
            else if( cmd instanceof GetConfigFunction )
            {
                SpectroStatus status = evt.getStatus();
                String message = searchFor( "CONFIG:", status );
                m_Logger.info( "Config String : " + message );
                if( message != null )
                {
                    parseSettings( message.substring( message.indexOf( ':' ) + 1 ) );
                }
            }
            else if( cmd instanceof GetSerialNumberFunction )
            {
                SpectroStatus status = evt.getStatus();
                String message = searchFor( "SERIAL_NUMBER:", status );
                m_Logger.info( "Serial Number : " + message );
                if( message != null )
                {
                    m_Serial = message.substring( message.indexOf( ':' ) + 1 );
                }
            }
            else if( cmd instanceof GetModelFunction )
            {
                SpectroStatus status = evt.getStatus();
                String message = searchFor( "MODEL:", status );
                m_Logger.info( "Model : " + message );
                if( message != null )
                {
                    m_Model = message.substring( message.indexOf( ':' ) + 1 );
                }
            }

            m_Outgoing.removeNextObject();
        }
    }

    /**
     * @param string
     * @param status
     * @return
     */
    private String searchFor( String string, SpectroStatus status )
    {
        Iterator msgList = status.getMessages().iterator();
        while( msgList.hasNext() )
        {
            String msg = (String) msgList.next();
            if( msg.startsWith( string ) )
            {
                return msg;
            }
        }

        return null;
    }

    /**
     * @param string
     */
    private void parseSettings( String string )
    {
        if( string.charAt( 1 ) == 'I' )
        {
            m_Settings.setSpecular( true );
        }
        else if( string.charAt( 1 ) == 'E' )
        {
            m_Settings.setSpecular( false );
        }

        if( string.charAt( 2 ) == 'S' )
        {
            m_Settings.setAperture( new SmallAperture() );
            m_Settings.setLensPosition( new SmallAreaView() );
        }
        else if( string.charAt( 2 ) == 'M' )
        {
            m_Settings.setAperture( new MediumAperture() );
            m_Settings.setLensPosition( new MediumAreaView() );
        }
        else if( string.charAt( 2 ) == 'L' )
        {
            m_Settings.setAperture( new LargeAperture() );
            m_Settings.setLensPosition( new LargeAreaView() );
        }

        if( "OPN".equals( string.substring( 4 ) ) )
        {
            m_Settings.setLightFilter( new UVIncludedLightFilter() );
        }
        else if( "UV1".equals( string.substring( 4 ) ) )
        {
            m_Settings.setLightFilter( new CutOff400LightFilter() );
        }
        else if( "UV2".equals( string.substring( 4 ) ) )
        {
            m_Settings.setLightFilter( new CutOff420LightFilter() );
        }

        notifySettingsChanged( new SpectroEvent( this ) );
    }

    public void queryNoOfStoredSamples() throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );

    }

    public void queryNoOfStoredStandards() throws SpectroException
    {
        throw new SpectroException( "MSG_UNSUPPORTED_FUNCTION" );

    }
}
