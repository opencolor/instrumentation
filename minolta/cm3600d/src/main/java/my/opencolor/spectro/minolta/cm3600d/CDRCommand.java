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

/*
 * CDRCommand.java
 *
 * Created on March 18, 2007, 9:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package my.opencolor.spectro.minolta.cm3600d;

import my.opencolor.spectro.spi.LensPosition;
import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;
import java.util.*;

/**
 * @author Robin Lee
 */
public class CDRCommand
    implements SpectroCommand, CommandStruc
{
    private LensPosition m_MeasureArea;
    private boolean m_Specular;

    /**
     * Creates a new instance of CDRCommand
     */
    public CDRCommand( LensPosition measureArea, Boolean specular )
    {
        m_MeasureArea = measureArea;
        m_Specular = specular.booleanValue();
    }

    public String construct()
    {
        StringBuffer command = new StringBuffer( "CDR" );

        if( m_Specular )
        {
            command.append( ",0" );
        }
        else
        {
            command.append( ",1" );
        }

        if( m_MeasureArea instanceof LargeAreaView )
        {
            command.append( ",0" );
        }
        else if( m_MeasureArea instanceof MediumAreaView )
        {
            command.append( ",1" );
        }
        else if( m_MeasureArea instanceof SmallAreaView )
        {
            command.append( ",2" );
        }

        command.append( DELIM );
        String cmd = command.toString();
        command = null;

        return cmd;
    }

    public String getName()
    {
        return "White Calibration Data Request Command";
    }

    public SpectroEvent interpret( byte[] valuesin )
    {
        String response = new String( valuesin );
        StringTokenizer sTok = new StringTokenizer( response, "," + DELIM );

        String statusString = sTok.nextToken();    // Error_Check_Code
        CM3600dStatus status = CM3600dStatus.create( statusString );

        // TODO pass this somewhere??
        String whiteCalibPlateSN = sTok.nextToken();    // ID

        try
        {

            if( sTok.countTokens() != 40 )
            {
                status.addError( "MSG_INVALID_RETURN" );
                return new SpectroEvent( this, status );
            }
            else
            {

                if( status.isSuccess() )
                {
                    Map values = new TreeMap( new DoubleCompare() );

                    int wavelength = 360;

                    while( sTok.hasMoreTokens() )
                    {
                        String data = sTok.nextToken();

                        try
                        {
                            Double doubleData = new Double( data );

                            values.put( new Integer( wavelength ), doubleData );
                            wavelength += 10;
                        }
                        catch( NumberFormatException numEx )
                        {
                            status.addError( "MSG_DATA_NUMBER_FORMAT_ERROR" );
                        }

                        MinoltaReading reading = new MinoltaReading( status, null, values );

                        return new SpectroEvent( this, reading );
                    }
                }
            }

        }
        catch( NoSuchElementException exception )
        {
            CM3600dStatus errstatus = CM3600dStatus.create( "INVALID_RETURN" );
            return new SpectroEvent( this, errstatus );
        }

        return new SpectroEvent( this, status );
    }

    class DoubleCompare implements Comparator
    {
        /**
         * Description of the Method
         *
         * @param o1 Description of the Parameter
         * @param o2 Description of the Parameter
         *
         * @return Description of the Return Value
         *
         * @throws ClassCastException Description of the Exception
         */
        public int compare( Object o1, Object o2 )
        {
            Double d1 = (Double) o1;
            Double d2 = (Double) o2;

            return (int) ( d1.doubleValue() - d2.doubleValue() );
        }
    }

}
