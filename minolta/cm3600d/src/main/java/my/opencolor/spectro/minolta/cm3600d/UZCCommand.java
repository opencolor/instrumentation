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
 * UZCCommand.java
 *
 * Created on March 18, 2007, 11:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package my.opencolor.spectro.minolta.cm3600d;

import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;
import java.util.StringTokenizer;

/**
 * @author Robin Lee
 */
public class UZCCommand implements SpectroCommand, CommandStruc
{

    /**
     * Creates a new instance of UZCCommand
     */
    public UZCCommand()
    {
    }

    public String construct()
    {
        return "UZC" + DELIM;
    }

    public String getName()
    {
        return "User Zero Calibration Command";
    }

    public my.opencolor.spectro.spi.SpectroEvent interpret( byte[] valuesin )
    {
        String response = new String( valuesin );
        StringTokenizer sTok = new StringTokenizer( response, "," + DELIM );

        if( sTok.countTokens() == 1 )
        {
            String returnCode = sTok.nextToken();

            CM3600dStatus status = CM3600dStatus.create( returnCode );

            return new SpectroEvent( this, status );
        }
        else
        {
            CM3600dStatus errstatus = CM3600dStatus.create( "INVALID_RETURN" );
            return new SpectroEvent( this, errstatus );
        }
    }

}
