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
 * BlackTrapCommand.java
 *
 * Created on July 11, 2002, 4:19 PM
 */

package my.opencolor.spectro.gretagmacbeth.ce2180;

import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;

/**
 * @author chc
 */
public class BlackTrapCommand
    implements SpectroCommand
{
    /**
     * Creates a new instance of BlackTrapCommand
     */
    public BlackTrapCommand()
    {

    }

    public String getName()
    {
        return "Black Trap Calibration Command";
    }

    public String construct()
    {
        String command;

        command = "B";

        return command;
    }

    public SpectroEvent interpret( byte[] received )
    {
        String response = new String( received );

        SpectroStatus status = new CE2180Status();

        // Check for Black Calibration
        if( response.charAt( 1 ) == '1' )
        {
            status.addMessage( "MSG_SUCCESS_BLACK_CALIBRATION" );
        }
        else if( response.charAt( 1 ) == '9' )
        {
            status.addError( "MSG_FAILURE_BLACK_CALIBRATION" );
        }
        else if( response.charAt( 1 ) == '8' )
        {
            status.addError( "MSG_FAILURE_BLACK_CALIBRATION" );
        }
        else
        {
            status.addError( "MSG_UNKNOWN_STATUS" );
        }

        return new SpectroEvent( this, status );
    }
}
