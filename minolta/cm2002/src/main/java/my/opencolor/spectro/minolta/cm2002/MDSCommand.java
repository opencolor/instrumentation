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
 * MDSCommand.java
 *
 * Created on October 14, 2002, 3:29 PM
 */

package my.opencolor.spectro.minolta.cm2002;

import my.opencolor.spectro.spi.SpectroCommand;
import my.opencolor.spectro.spi.SpectroEvent;

/**
 *
 * @author  chc
 */
public class MDSCommand implements SpectroCommand
{
    private String[] m_Reflectance;
    private String m_DataID;
    private String m_Comment;
    
    /** Creates a new instance of MDSCommand */
    public MDSCommand ( String[] Reflectance, String DataID, String Comment )
    {
        m_Reflectance = Reflectance;
        m_DataID = DataID;
        m_Comment = Comment;
    }
    
    public String construct ()
    {
        StringBuffer mesg = new StringBuffer();
        
        mesg.append ( "MDS\r\n" );
        
        for ( int i = 0; i < m_Reflectance.length; i++ )
        {
            mesg.append ( m_Reflectance[i]+"\r\n" );
        }
        
        mesg.append ( m_DataID + "\r\n" );
        mesg.append ( m_Comment );
        
        return mesg.toString ();
    }
    
    public String getName ()
    {
        return "One Memory Data Set Command";
    }
    
    public my.opencolor.spectro.spi.SpectroEvent interpret (byte[] values)
    {
        return new SpectroEvent( this, CM2002Status.create( new String( values ) ) );
    }
}
