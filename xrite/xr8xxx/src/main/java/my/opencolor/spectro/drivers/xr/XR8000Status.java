/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package my.opencolor.spectro.drivers.xr;

import my.opencolor.spectro.spi.SpectroStatus;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Administrator
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XR8000Status implements SpectroStatus
{
    ArrayList m_Messages;
    ArrayList m_Warnings;
    ArrayList m_Errors;

    public XR8000Status()
    {
        m_Messages = new ArrayList();
        m_Warnings = new ArrayList();
        m_Errors = new ArrayList();
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#isSuccess()
      */
    public boolean isSuccess()
    {
        if( m_Errors.size() == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#isFailure()
      */
    public boolean isFailure()
    {
        if( m_Errors.size() > 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#getWarnings()
      */
    public Collection getWarnings()
    {
        return (Collection) m_Warnings.clone();
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#addWarning(java.lang.String)
      */
    public void addWarning( String warning )
    {
        m_Warnings.add( warning );
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#getErrors()
      */
    public Collection getErrors()
    {
        return (Collection) m_Errors.clone();
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#addError(java.lang.String)
      */
    public void addError( String error )
    {
        m_Errors.add( error );
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#getMessages()
      */
    public Collection getMessages()
    {
        return (Collection) m_Messages.clone();
    }

    /* (non-Javadoc)
      * @see my.opencolor.spectro.spi.SpectroStatus#addMessage(java.lang.String)
      */
    public void addMessage( String message )
    {
        m_Messages.add( message );
    }

}
