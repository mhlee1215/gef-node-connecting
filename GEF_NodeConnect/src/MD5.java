import java.security.MessageDigest;


public class MD5 {
	public static String getString( byte[] bytes )
	{
	  StringBuffer sb = new StringBuffer();
	  for( int i=0; i<bytes.length; i++ )
	  {
	    byte b = bytes[ i ];
	    sb.append( ( int )( 0x00FF & b ) );
	    if( i+1 <bytes.length )
	    {
	      sb.append( "-" );
	    }
	  }
	  return sb.toString();
	}
	public static String md5( String source )
	{
	  try
	  {
	   MessageDigest md = MessageDigest.getInstance( "MD5" );
	   byte[] bytes = md.digest( source.getBytes() );
	   return getString( bytes );
	  }
	  catch( Exception e )
	  {
	   e.printStackTrace();
	   return null;
	  }
	}
	
	public static void main(String[] argv){
		System.out.println("p2: "+md5("p2").length());
		System.out.println("ii552222: "+md5("ii552222"));
		System.out.println("jw1234!@: "+md5("jw1234!@"));
		System.out.println("1111: "+md5("1111"));
	}
}
