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
 * ATECommand.java
 *
 * Created on October 14, 2002, 3:27 PM
 */

package my.opencolor.spectro.minolta.cm2002;

import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;

/**
 *
 * @author  chc
 */
public class ATECommand implements SpectroCommand
{
    
    /** Creates a new instance of ATECommand */
    public ATECommand ()
    {
    }
    
    public String construct ()
    {
        return "ATE";
    }
    
    public String getName ()
    {
        return "Auto Target Enable Command";
    }
    
    public SpectroEvent interpret (byte[] values)
    {
        return new SpectroEvent( this, CM2002Status.create( new String( values ) ) );
    }
    
}
