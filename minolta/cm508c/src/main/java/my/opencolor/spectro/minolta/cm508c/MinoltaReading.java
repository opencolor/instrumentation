
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

package my.opencolor.spectro.minolta.cm508c;

import my.opencolor.spectro.spi.SpectroReading;
import my.opencolor.spectro.spi.SpectroSettings;
import my.opencolor.spectro.spi.SpectroStatus;

import java.util.Map;

/**
 * @stereotype container 
 */
public class MinoltaReading implements SpectroReading
{
    
    private SpectroStatus m_Status;
    private SpectroSettings m_Settings;
    private Map m_Values;
    
    public MinoltaReading (SpectroStatus status, SpectroSettings settings, Map values)
    {
        m_Status = status;
        m_Settings = settings;
        m_Values = values;
    }
    
    public SpectroStatus getStatus()    
    {
        return m_Status;
    }

    public Map getValues()
    {
        return m_Values;
    }

    public SpectroSettings getSettings()
    {
        return m_Settings;
    }
    
    public void setSettings( SpectroSettings settings )
    {
        m_Settings = settings;
    }
    
}