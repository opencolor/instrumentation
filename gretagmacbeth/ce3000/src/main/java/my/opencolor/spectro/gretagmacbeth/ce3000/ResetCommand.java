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
 * MeasureCommand.java
 *
 * Created on July 11, 2002, 4:27 PM
 */

package my.opencolor.spectro.gretagmacbeth.ce3000;

import java.util.Comparator;
import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;

/**
 * @author hywong
 */

public class ResetCommand implements SpectroCommand
{
    /**
     * Creates a new instance of MeasureCommand
     */
    public ResetCommand()
    {
    }

    public String getName()
    {
        return "Reset Command";
    }

    public String construct()
    {
        return "r";
    }

    public SpectroEvent interpret( byte[] received )
    {
        SpectroStatus status = new CE3000Status();

        if( received.length >= 6 )
        {
            status.addMessage( "SERIAL: " + new String( received, 0, 6 ) );
        }
        else
        {
            status.addMessage( "STARTUP" );
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
         * @return Description of the Return Value
         * @throws ClassCastException Description of the Exception
         */
        public int compare( Object o1, Object o2 )
            throws ClassCastException
        {
            Double d1 = (Double) o1;
            Double d2 = (Double) o2;

            return (int) ( d1.doubleValue() - d2.doubleValue() );
        }
    }
}