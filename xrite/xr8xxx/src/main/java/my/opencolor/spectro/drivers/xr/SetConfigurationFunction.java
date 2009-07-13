/*
 * Created on Nov 7, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.Aperture;
import my.opencolor.spectro.spi.LightFilter;
import my.opencolor.spectro.spi.SpectroEvent;
import my.opencolor.spectro.spi.SpectroException;
import my.opencolor.spectro.spi.SpectroStatus;
import my.opencolor.spectro.spi.SpectroFunction;

public class SetConfigurationFunction
    implements SpectroFunction
{
    public static int SMALL_APERTURE = 0;
    public static int MEDIUM_APERTURE = 1;
    public static int LARGE_APERTURE = 2;

    public static int UV_INCLUDED = 0;
    public static int CUTOFF_400 = 3;
    public static int CUTOFF_420 = 4;

    public static int SPEC_INCLUDED = 0;
    public static int SPEC_EXCLUDED = 1;
    public static int SPEC_BOTH = 2;

    public static int REFLECTANCE = 1;
    public static int TRANSMITTANCE = 0;

    private Aperture m_Aperture;
    private LightFilter m_Filter;
    private boolean m_Specular;
    private boolean m_Reflectance;

    public SetConfigurationFunction( Aperture aperture, LightFilter filter, boolean specular, boolean reflectance )
        throws SpectroException
    {
        m_Aperture = aperture;
        m_Filter = filter;
        m_Specular = specular;
        m_Reflectance = reflectance;
    }

    public SpectroEvent invoke()
    {
        SpectroStatus status = new XR8000Status();

        int apertureSetting = 0;
        if( m_Aperture instanceof SmallAperture )
        {
            apertureSetting = SMALL_APERTURE;
        }
        else if( m_Aperture instanceof MediumAperture )
        {
            apertureSetting = MEDIUM_APERTURE;
        }
        else if( m_Aperture instanceof LargeAperture )
        {
            apertureSetting = LARGE_APERTURE;
        }

        int filterSetting = 0;
        if( m_Filter instanceof UVIncludedLightFilter )
        {
            filterSetting = UV_INCLUDED;
        }
        else if( m_Filter instanceof CutOff400LightFilter )
        {
            filterSetting = CUTOFF_400;
        }
        else if( m_Filter instanceof CutOff420LightFilter )
        {
            filterSetting = CUTOFF_420;
        }

        int reflectanceSetting;
        if( m_Reflectance )
        {
            reflectanceSetting = REFLECTANCE;
        }
        else
        {
            reflectanceSetting = TRANSMITTANCE;
        }

        int specularSetting;
        if( m_Specular )
        {
            specularSetting = SPEC_INCLUDED;
        }
        else
        {
            specularSetting = SPEC_EXCLUDED;
        }

        Xr8000Library.INSTANCE.Instrument_SetConfiguration( apertureSetting, filterSetting, 0.0f,
                                                            reflectanceSetting, specularSetting );
        return new SpectroEvent( this, status );
    }
}
