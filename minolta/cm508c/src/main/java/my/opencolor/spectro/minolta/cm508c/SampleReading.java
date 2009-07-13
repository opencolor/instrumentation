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
 * SampleReading.java
 *
 * Created on November 18, 2002, 11:17 AM
 */

package my.opencolor.spectro.minolta.cm508c;

import my.opencolor.spectro.spi.SpectroReading;
import my.opencolor.spectro.spi.SpectroSettings;
import my.opencolor.spectro.spi.SpectroStatus;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  devteam
 */
public class SampleReading implements SpectroReading
{
    SpectroSettings m_settings;
    SpectroStatus m_status;
    Map values;
    
    /** Creates a new instance of SampleReading */
    public SampleReading ()
    {
        m_settings = new SpectroSettings ();
        
        m_status = CM508cStatus.create ( "OK00" );
        
        // Create the sample values
        values = new HashMap ();
        
        for ( int i = 400; i <= 700; i = i + 20 )
        {
            values.put ( new Double ( i ) , new Double ( i ) );
        }
    }
    
    public SpectroSettings getSettings ()
    {
        return m_settings;
    }
    
    public SpectroStatus getStatus ()
    {
        return m_status;
    }
    
    public Map getValues ()
    {
        return values;
    }
    
}
