/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroFunction;

public class StandaloneApplication
{
    public static void main( String args[] )
    {
        System.out.println( "Library Path = " + System.getProperty( "java.library.path" ) );
        SpectroFunction function = new StandaloneFunction();
        function.invoke();
    }
}
