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

import my.opencolor.spectro.spi.*;
import java.util.*;

/**
 * Definition of a Spectrophotometer driver. <p>The <code>SpectroDriver</code> implementation must properly implement
 * these methods, primarily for the user client software to query for functionality and operational issues.</p>
 *
 * @author Robin Lee
 * @author Niclas Hedhman
 * @stereotype implementation
 * @type Product Requirement
 */
public class CM2500dDriver
    implements SpectroDriver
{
    static
    {
        DriverManager manager = DriverManager.getInstance();
        try
        {
            manager.registerDriver( CM2500dDriver.class );
        }
        catch( my.opencolor.spectro.spi.SpectroException ex )
        {
            System.err.println( "Unable to register CM2500d Driver" );
            ex.printStackTrace( System.err );
        }
    }

    private boolean m_RetrieveSamples;
    private boolean m_RetrieveStandards;
    private boolean m_Reflectance;
    private boolean m_Transmittance;
    private boolean m_IncSpecular;
    private boolean m_ExcSpecular;
    private boolean m_OfflineMeasurement;
    private boolean m_BlackFirst;
    private boolean m_BlackCalibration;
    private boolean m_WhiteCalibration;

    private String m_Name;
    private String m_Manufacturer;
    private Collection m_Parameters;

    private LightFilter[] m_LightFilters;
    private Aperture[] m_Apertures;
    private LensPosition[] m_LensPositions;

    private String[] m_CalibrationSteps = { "MSG_ZERO", "MSG_WHITE" };

//    private String[] m_CalibrationDataFiles = {
//        "Specular Included White Tile (whitesi.dat)", "Specular Excluded White Tile (whitese.dat)"
//    };

    private boolean m_UIApertures;
    private boolean m_UILensPositions;
    private boolean m_UISpecular;
    private boolean m_UIUVFilters;

    public CM2500dDriver()
    {
        initialize();
    }

    public boolean canRetrieveSamples()
    {
        return m_RetrieveSamples;
    }

    public boolean canRetrieveStandards()
    {
        return m_RetrieveStandards;
    }

    public boolean canMeasureReflectance()
    {
        return m_Reflectance;
    }

    public boolean canMeasureTransmittance()
    {
        return m_Transmittance;
    }

    public boolean canIncludeSpecular()
    {
        return m_IncSpecular;
    }

    public boolean canExcludeSpecular()
    {
        return m_ExcSpecular;
    }

    public LightFilter[] getFilters()
    {
        return m_LightFilters;
    }

    public boolean usesSerialPorts()
    {
        return true;
    }

    public int getStartingWavelength()
    {
        return 360;
    }

    public int getEndingWavelength()
    {
        return 740;
    }

    public int getInterval()
    {
        return 10;
    }

    public double getWhiteTilePrecision()
    {
        return 1.0;
    }

    public int getEstimatedAccuracy()
    {
        return 1;
    }

    public Aperture[] getApertures()
    {
        return m_Apertures;
    }

    public boolean canMeasureOffline()
    {
        return m_OfflineMeasurement;
    }

    public int getNoOfOfflineMeasurements()
    {
        return 0;
    }

    public int getNoOfOfflineStandards()
    {
        return 0;
    }

    public String getName()
    {
        return m_Name;
    }

    public String getManufacturer()
    {
        return m_Manufacturer;
    }

    public void initialize()
    {
        m_Parameters = new ArrayList();

        m_Manufacturer = "Minolta";
        m_Name = "CM-2500d";

        m_RetrieveSamples = true;
        m_RetrieveStandards = false;

        m_Reflectance = true;
        m_Transmittance = false;

        m_IncSpecular = true;
        m_ExcSpecular = true;

        m_OfflineMeasurement = false;

        m_BlackFirst = false;
        m_BlackCalibration = true;
        m_WhiteCalibration = true;

        m_LensPositions = new LensPosition[] { new MediumAreaView(), new SmallAreaView() };

        m_Apertures = new Aperture[] { new MediumAperture() };

        m_LightFilters = new LightFilter[] { new UVIncludedLightFilter() };

        m_UIApertures = false;
        m_UILensPositions = false;
        m_UISpecular = true;
        m_UIUVFilters = false;

    }

    public void dispose()
    {
    }

    /**
     * Creates a new instance of a Spectrophotometer. <p>The SpectroDriver implementation is responsible to initialize
     * the Spectrophotometer instance to its initialized state, so it is then ready to be called by the
     * <code>setSettings</code> method.</p>
     */
    public Spectrophotometer createSpectrophotometer()
    {
        return new CM2600dSpectro();
    }

    /**
     * Returns the additional parameters required for the Driver. <p>The key in the <code>java.util.Map</code> contains
     * the name of the Parameter, the value is a <code>SpectroParameter</code> object.</p>
     */
    public Collection getParameterDefinitions()
    {
        return m_Parameters;
    }

    public LensPosition[] getLensPositions()
    {
        return m_LensPositions;
    }

    public boolean doesBlackCalibrationFirst()
    {
        return m_BlackFirst;
    }

    public boolean canBlackCalibrate()
    {
        return m_BlackCalibration;
    }

    public boolean canWhiteCalibrate()
    {
        return m_WhiteCalibration;
    }

    public String[] getCalibrationSteps()
    {
        return m_CalibrationSteps;
    }

    public String[] getCalibrationDataFiles()
    {
        return null;
    }

    public boolean canUIApertures()
    {
        return m_UIApertures;
    }

    public boolean canUILensPositions()
    {
        return m_UILensPositions;
    }

    public boolean canUISpecular()
    {
        return m_UISpecular;
    }

    public boolean canUIUVFilters()
    {
        return m_UIUVFilters;
    }

    public String getLocalizedText( String text )
    {
        return getLocalizedText( text, Locale.getDefault() );
    }

    public String getLocalizedText( String text, Locale locale )
    {
        try
        {
            return ResourceBundle.getBundle( "my.opencolor.spectro.drivers.minolta.Bundle", locale ).getString( text );
        }
        catch( MissingResourceException e )
        {
            return text;
        }
    }

    public boolean canQuerySamples()
    {
        return true;
    }

    public boolean canQueryStandards()
    {
        return false;
    }
    /** @link dependency
     * @stereotype instantiate*/
    /*#Spectrophotometer lnkSpectrophotometer;*/
}
