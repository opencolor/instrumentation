/*
 * Created on Nov 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroFunction;

public class TestCalibrateFunction
{
    public static void main( String[] args )
        throws Exception
    {
        SpectroFunction function = new CalibrateFunction();
        function.invoke();
    }
}
