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
 * Created on Oct 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package my.opencolor.spectro.gretagmacbeth.ce3000;

import my.opencolor.spectro.spi.helpers.CommDriver;
import my.opencolor.spectro.spi.helpers.CommDriverException;

public class CE3000Spectro9600 extends CE3000Spectro
{
    protected void openConnection( CommDriver commDriver, String portname )
        throws CommDriverException
    {
        commDriver.openConnection( portname, 3000, 9600 );
    }

}
