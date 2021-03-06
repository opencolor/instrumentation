/*
 * Copyright 2009 Niclas Hedhman.
 *
 * Licensed  under the  Apache License, Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package my.opencolor.spectro.hunter.common;

import my.opencolor.spectro.spi.helpers.CommDriver;
import my.opencolor.spectro.spi.helpers.CommDriverEvent;
import my.opencolor.spectro.spi.helpers.CommDriverException;
import my.opencolor.spectro.spi.helpers.CommDriverListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.comm.CommPortIdentifier;
import javax.comm.CommPortOwnershipListener;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

/**
 *  Generic CommDriver for SerialPorts. <p>
 *
 *  The HunterCommDriver is a specific implementation of the CommDriver
 *  interface, available to Hunterlab Spectrophotometers who follow this simple and
 *  straight forward communication style. What differs from the generic commdriver
 *  is that this implementation does not trim the response for whitespace characters.
 *   </p>
 *
 *@author     chc
 *@created    May 30, 2002
 */
public class HunterCommDriver
implements CommDriver, SerialPortEventListener, CommPortOwnershipListener
{
    private static Logger m_Logger = Logger.getLogger( HunterCommDriver.class.getName() );
    
    private OutputStream m_os;
    private InputStream m_is;
    
    private CommPortIdentifier m_portId;
    private SerialPort m_sPort;
    
    private boolean m_open = false;
    private String m_result = "empty";
    
    // Default settings for timout
    private int m_ReceiveTO;
    private int m_SendTO;
    
    private ArrayList m_Listeners;
    
    private Timer m_timer;
    
    public HunterCommDriver()
    {
        m_ReceiveTO = 1000;
        m_SendTO = 1000;
    }
    
    /**
     *  Description of the Method
     *
     *@param  send  Description of the Parameter
     */
    public boolean send( byte[] send )
    {
        // TODO:
        try
        {
            m_os.write( send );
            fireCommDriverEventReceived( new CommDriverEvent(this , CommDriverEvent.COMM_DRIVER_SENDING ) );
            return true;
        }
        catch ( IOException e )
        {
            m_Logger.log( Level.SEVERE, e.getMessage(), e );
            return false;
        }
    }
    
    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public byte[] receive()
    {
        return m_result.getBytes();
    }
    
    
    /**
     *  Adds a feature to the CommDriverListener attribute of the HunterCommDriver
     *  object
     *
     *@param  listener  The feature to be added to the CommDriverListener attribute
     */
    public void addCommDriverListener( CommDriverListener listener )
    {
        synchronized( this )
        {
            ArrayList v;
            if( m_Listeners == null )
                v = new ArrayList();
            else
                v = (ArrayList) m_Listeners.clone();
            v.add( listener );
            m_Listeners = v;
        }
    }
    
    
    /**
     *  Description of the Method
     *
     *@param  listener  Description of the Parameter
     */
    public void removeCommDriverListener( CommDriverListener listener )
    {
        if( m_Listeners == null )
            return;
        synchronized( this )
        {
            ArrayList v = (ArrayList) m_Listeners.clone();
            v.remove( listener );
            if( v.size() == 0 )
                m_Listeners = null;
            else
                m_Listeners = v;
        }
    }
    
    public void fireCommDriverEventReceived( CommDriverEvent event )
    {
        Iterator list = m_Listeners.iterator();
        
        while ( list.hasNext() )
        {
            ( ( CommDriverListener ) list.next() ).received( event );
        }
        
    }
    
    public void fireCommDriverEventSent( CommDriverEvent event )
    {
        Iterator list = m_Listeners.iterator();
        
        while ( list.hasNext() )
        {
            ( ( CommDriverListener ) list.next() ).sent( event );
        }
        
    }
    
    /**
     *  Sets the receiveTimeout attribute of the HunterCommDriver object
     *
     *@param  timeout  The new receiveTimeout value
     */
    public void setReceiveTimeout( int timeout )
    {
        m_ReceiveTO = timeout;
    }
    
    /**
     *  Gets the receiveTimeout attribute of the HunterCommDriver object
     *
     *@return    The receiveTimeout value
     */
    public int getReceiveTimeout()
    {
        return m_ReceiveTO;
    }
    
    
    /**
     *  Sets the sendTimeout attribute of the HunterCommDriver object
     *
     *@param  timeout  The new sendTimeout value
     */
    public void setSendTimeout( int timeout )
    {
        m_SendTO = timeout;
    }
    
    
    /**
     *  Gets the sendTimeout attribute of the HunterCommDriver object
     *
     *@return    The sendTimeout value
     */
    public int getSendTimeout()
    {
        return m_SendTO;
    }
    
    
    /**
     *  Description of the Method
     *
     *@param  m_portname               Description of the Parameter
     *@param  m_timeout                Description of the Parameter
     *@param  m_baudrate               Description of the Parameter
     *@exception  CommDriverException  Description of the Exception
     */
    public void openConnection( String m_portname, int m_timeout, int m_baudrate )
    throws CommDriverException
    {
        // Obtain a CommPortIdentifier object for the port you want to open.
        try
        {
            m_portId =
            CommPortIdentifier.getPortIdentifier( m_portname );
        }
        catch ( NoSuchPortException e )
        {
            throw new CommDriverException( e.toString() );
        }
        
        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of m_tinmeout seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        try
        {
            m_sPort = ( SerialPort ) m_portId.open( "Spectro", m_timeout );
        }
        catch ( PortInUseException e )
        {
            throw new CommDriverException( e.toString() );
        }
        
        // Set connection parameters
        try
        {
            m_sPort.setSerialPortParams( m_baudrate,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE );
        }
        catch ( UnsupportedCommOperationException e )
        {
            m_sPort.close();
            throw new CommDriverException( e.toString() );
        }
        
        // Open the input and output streams for the connection. If they won't
        // open, close the port before throwing an exception.
        try
        {
            m_os = m_sPort.getOutputStream();
            m_is = m_sPort.getInputStream();
        }
        catch ( IOException e )
        {
            m_sPort.close();
            throw new CommDriverException( "Error opening i/o streams" );
        }
        
        // Add this object as an event listener for the serial port.
        try
        {
            m_sPort.addEventListener( this );
        }
        catch ( TooManyListenersException e )
        {
            m_sPort.close();
            throw new CommDriverException( "too many listeners added" );
        }
        
        // Set notifyOnDataAvailable to true to allow event driven input.
        m_sPort.notifyOnDataAvailable( true );
        m_sPort.notifyOnOutputEmpty( true );
        
        // Set receive timeout to allow breaking out of polling loop during
        // input handling.
        try
        {
            m_sPort.enableReceiveTimeout( m_ReceiveTO );
        }
        catch ( UnsupportedCommOperationException e )
        {
        }
        
        // Add ownership listener to allow ownership event handling.
        m_portId.addPortOwnershipListener( this );
        
        m_open = true;
    }
    
    
    /**
     *  Close the port connection
     *
     *@exception  CommDriverException  Description of the Exception
     */
    public void closeConnection()
    throws CommDriverException
    {
        // If port is alread closed just return.
        if ( !m_open )
        {
            return;
        }
        
        // Check to make sure sPort has reference to avoid a NPE.
        if ( m_sPort != null )
        {
            try
            {
                // close the i/o streams.
                m_os.close();
                m_is.close();
            }
            catch ( IOException e )
            {
                throw new CommDriverException( e.toString() );
            }
            
            // Close the port.
            m_sPort.close();
            
            // Remove the ownership listener.
            m_portId.removePortOwnershipListener( this );
        }
        
        m_open = false;
        
    }
    
    
    /**
     *  Reports the open status of the port.
     *
     *@return    true if port is open, false if port is closed.
     */
    public boolean isOpen()
    {
        return m_open;
    }
    
    //////// SerialPortListener interface
    
    /**
     *  Description of the Method
     *
     *@param  e  Description of the Parameter
     */
    public void serialEvent( SerialPortEvent e )
    {
        // Create a StringBuffer and int to receive input data.
        StringBuffer m_inputBuffer = new StringBuffer();
        int m_newData = 0;
        
        // Determine type of event.
        switch ( e.getEventType() )
        {
            
            // Read data until -1 is returned. If \r is received substitute
            // \n for correct newline handling.
            case SerialPortEvent.DATA_AVAILABLE:
                while ( m_newData != -1 )
                {
                    try
                    {
                        m_newData = m_is.read();
                        if ( m_newData == -1 )
                        {
                            break;
                        }
                        //if ( '\r' == ( char ) m_newData )
                        //{
                        //    m_inputBuffer.append( '\n' );
                        //}
                        //else
                        //{
                        m_inputBuffer.append( ( char ) m_newData );
                        //}
                    }
                    catch ( IOException ex )
                    {
                        //System.err.println( ex );
                        return;
                    }
                }
                
                // Append received data to messageAreaIn.
                m_result = new String( m_inputBuffer );
                fireCommDriverEventReceived( new CommDriverEvent(this , CommDriverEvent.COMM_DRIVER_RECEIVED ) );
                //System.out.println( "Result internal : " + m_result );
                break;
                
            case SerialPortEvent.BI:
                //System.out.println( "Break interrupt occurred." );
                break;
                
            case SerialPortEvent.CD:
                //Handle a carrier detect... probably never used
                //System.out.println( "Carrier Detect." );
                break;
                
            case SerialPortEvent.CTS:
                //Handle a clear to send event
                //System.out.println( "Clear to send." );
                break;
                
            case SerialPortEvent.DSR:
                //Handle a data set ready event
                //System.out.println( "Date set ready." );
                break;
                
            case SerialPortEvent.FE:
                //Handle a Framing error event
                //System.out.println( "Framing error." );
                break;
                
            case SerialPortEvent.OE:
                //Handle a overrun error event
                //System.out.println( "Overrun Error." );
                break;
                
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                //Handle an output buffer empty event
                //System.out.println( "Output buffer is empty." );
                fireCommDriverEventSent( new CommDriverEvent(this , CommDriverEvent.COMM_DRIVER_SENT ) );
                break;
                
            case SerialPortEvent.PE:
                //Handle a parity error event... possibly error recovery?
                //System.out.println( "Parity Error." );
                break;
                
            case SerialPortEvent.RI:
                //Handle a ring indicator event... probably never user.
                //System.out.println( "Ring Indicator." );
                break;
        }
        
    }
    
    /**
     *  Description of the Method
     *
     *@param  type  Description of the Parameter
     */
    public void ownershipChange( int type )
    {
        if ( type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED )
        {
            try
            {
                closeConnection();
            }
            catch ( Exception e )
            {
            }
        }
    }
    
    // Received and sent method for unknown prupose.
    /**
     *  Description of the Method
     *
     *@param  event  Description of the Parameter
     */
    public void received( CommDriverEvent event )
    {
    }
    
    
    /**
     *  Description of the Method
     *
     *@param  event  Description of the Parameter
     */
    public void sent( CommDriverEvent event )
    {
    }
    
    public void openConnection( String m_portname, int m_timeout, int m_baudrate, int m_flowcontrol )
    throws CommDriverException
    {
        // Obtain a CommPortIdentifier object for the port you want to open.
        try
        {
            m_portId =
            CommPortIdentifier.getPortIdentifier( m_portname );
        }
        catch ( NoSuchPortException e )
        {
            throw new CommDriverException( e.toString() );
        }
        
        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of m_tinmeout seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        try
        {
            m_sPort = ( SerialPort ) m_portId.open( "Spectro", m_timeout );
        }
        catch ( PortInUseException e )
        {
            throw new CommDriverException( e.toString() );
        }
        
        // Set connection parameters
        try
        {
            m_sPort.setSerialPortParams( m_baudrate,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE );
        }
        catch ( UnsupportedCommOperationException e )
        {
            m_sPort.close();
            throw new CommDriverException( e.toString() );
        }
        
        // set up the flow control
        if ( m_flowcontrol >= 0 )
        {
            try
            {
                if ( m_flowcontrol == CommDriver.FLOWCONTROL_NONE )
                    m_sPort.setFlowControlMode( SerialPort.FLOWCONTROL_NONE | SerialPort.FLOWCONTROL_NONE);
                else if ( m_flowcontrol == CommDriver.FLOWCONTROL_RTSCTS )
                    m_sPort.setFlowControlMode( SerialPort.FLOWCONTROL_RTSCTS_IN |  SerialPort.FLOWCONTROL_RTSCTS_OUT );
                else if ( m_flowcontrol == CommDriver.FLOWCONTROL_XONXOFF )
                    m_sPort.setFlowControlMode( SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT );
                else
                    throw new CommDriverException( "Invalid flow control type" );
            }
            catch ( UnsupportedCommOperationException e )
            {
                throw new CommDriverException( e.toString() );
            }
        }
        
        // Open the input and output streams for the connection. If they won't
        // open, close the port before throwing an exception.
        try
        {
            m_os = m_sPort.getOutputStream();
            m_is = m_sPort.getInputStream();
        }
        catch ( IOException e )
        {
            m_sPort.close();
            throw new CommDriverException( "Error opening i/o streams" );
        }
        
        // Add this object as an event listener for the serial port.
        try
        {
            m_sPort.addEventListener( this );
        }
        catch ( TooManyListenersException e )
        {
            m_sPort.close();
            throw new CommDriverException( "too many listeners added" );
        }
        
        // Set notifyOnDataAvailable to true to allow event driven input.
        m_sPort.notifyOnDataAvailable( true );
        m_sPort.notifyOnOutputEmpty( true );
        
        // Set receive timeout to allow breaking out of polling loop during
        // input handling.
        try
        {
            m_sPort.enableReceiveTimeout( m_ReceiveTO );
        }
        catch ( UnsupportedCommOperationException e )
        {
        }
        
        // Add ownership listener to allow ownership event handling.
        m_portId.addPortOwnershipListener( this );
        
        m_open = true;
    }    
    
    // Implementation of Timer to timeout
    public void setRespondTimeout( int timeout )
    {
        m_timer = new Timer();
        m_timer.schedule( new RespondTimeOutTask(), timeout);
    }
    
    public void cancelRespondTimeout()
    {
        if ( m_timer != null )
	   m_timer.cancel();
    }
    
    class RespondTimeOutTask extends TimerTask
    {
        public void run()
        {
            m_timer.cancel();
            fireCommDriverEventReceived( new CommDriverEvent(this , CommDriverEvent.COMM_DRIVER_TIMEOUT ) );
        }
    }
    
}
