package cloud.cdmi.internal.extensions.cdmi.restful;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.RequestEntity;

public class ImageRequestEntity implements RequestEntity
{
    public ImageRequestEntity( File file )
    {
        super();
        this.file = file;
    }

    public boolean isRepeatable()
    {
        return true;
    }

    public String getContentType()
    {
        return "text/plain; charset=UTF-8";
    }

    public void writeRequest( OutputStream out ) throws IOException
    {
        InputStream in = new FileInputStream( this.file );
        try
        {
            int l;
            byte[] buffer = new byte[ 1024 ];
            while ( (l = in.read( buffer )) != -1 )
            {
                out.write( buffer, 0, l );
            }
        }
        finally
        {
            in.close();
        }
    }

    public long getContentLength()
    {
        return file.length();
    }

    private File file = null;
}
