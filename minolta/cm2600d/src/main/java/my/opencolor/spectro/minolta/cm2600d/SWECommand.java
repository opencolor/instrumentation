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

package my.opencolor.spectro.minolta.cm2600d;

import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;
import java.util.StringTokenizer;

/** Enables the Switch on the spectrophotometer to be used for making the actual
 * measurement.
 * 
 * @author Niclas Hedhman
 */
public class SWECommand
    implements SpectroCommand, CommandStruc
{
    public String getName()
    {
        return "Switch Enable";
    }

    public String construct()
    {
        return "SWE" + DELIM;
    }

    public SpectroEvent interpret( byte[] response )
    {
        StringTokenizer sTok = new StringTokenizer( new String( response ), "," + DELIM );
        CM2600dStatus status = CM2600dStatus.create( sTok.nextToken() );
        return new SpectroEvent( this, status );
    }
}
