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
 * ExecRefMeasurementCommand.java
 *
 * Created on July 19, 2002, 4:04 PM
 */

package my.opencolor.spectro.gretagmacbeth.spectrolino;

import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroStatus;

import java.util.Iterator;

/**
 *
 * @author  chc
 */
public class ExecRefMeasurementCommand implements SpectroCommand {
    
    /** Creates a new instance of ExecRefMeasurementCommand */
    public ExecRefMeasurementCommand () {
    }
    
    public String getName()
    {
        return "White Calibration Command";
    }    
    
    public String construct()
    {
        String command;
        
        command = "; 34 9 7";
        
        return command;
    }

    public SpectroEvent interpret( byte[] received )
    {
        String response = new String( received );

        if ( response == null )
        {
            return null;
        }
        
        SpectroStatus status = SpectrolinoStatus.create( response );

        if ( status.isFailure() ) 
        {
            Iterator list = status.getErrors().iterator();
            
            while ( list.hasNext() ) {
                if ( list.next().equals("MSG_UNKNOWN_STRING") )
                    return null;
            }
        }
        
        return new SpectroEvent( this, status );
    }        
}
