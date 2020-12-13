/******************************************************************************
* CREATETIME : 2016年7月24日 下午3:31:25
******************************************************************************/
package ins.platform.utils;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * URL编码解码工具类
 * @author ★LiuPing
 * @CreateTime 2016年7月24日
 */
public class URLEncodedUtils {

	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";

	/**
	 * Returns a String that is suitable for use as an <code>application/x-www-form-urlencoded</code> list of parameters in an HTTP PUT or HTTP POST.
	 * @param parameters The parameters to include.
	 * @param encoding UTF-8.
	 */
	public static String format(final Map<String,String> paramMap) {
		return format(paramMap,DEFAULT_CHARSET);
	}

	/**
	 * Returns a String that is suitable for use as an <code>application/x-www-form-urlencoded</code> list of parameters in an HTTP PUT or HTTP POST.
	 * @param parameters The parameters to include.
	 * @param encoding The encoding to use.
	 */
	public static String format(final Map<String,String> paramMap,final Charset charset) {
		final StringBuilder result = new StringBuilder();
		for(final String name:paramMap.keySet()){
			final String encodedName = encodeFormFields(name,charset);
			final String encodedValue = encodeFormFields(paramMap.get(name),charset);
			if(result.length()>0){
				result.append(PARAMETER_SEPARATOR);
			}
			result.append(encodedName);
			if(encodedValue!=null){
				result.append(NAME_VALUE_SEPARATOR);
				result.append(encodedValue);
			}
		}
		return result.toString();
	}

	/**
	 * Returns a list of {@link NameValuePair NameValuePairs} as built from the URI's query portion. For example, a URI of http://example.org/path/to/file?a=1&b=2&c=3 would return a list of three
	 * NameValuePairs, one for a=1, one for b=2, and one for c=3.
	 * <p>
	 * This is typically useful while parsing an HTTP PUT.
	 * @param uri uri to parse
	 * @param encoding encoding to use while parsing the query
	 */
	public static Map<String,String> parse(final URI uri,final Charset charset) {
		final String query = uri.getRawQuery();
		Map<String,String> result = new HashMap<String,String>();
		if(query!=null&&query.length()>0){
			Scanner scanner = new Scanner(query);
			parse(result,scanner,charset);
		}
		return result;
	}

	/**
	 * Decode/unescape www-url-form-encoded content.
	 * @param content the content to decode, will decode '+' as space
	 * @param charset the charset to use
	 * @return
	 */
	public static String decodeField(final String content) {
		if(content==null){
			return null;
		}
		return urldecode(content,DEFAULT_CHARSET,true);
	}


	public static String encodeField(final String content) {
		if(content==null){
			return null;
		}
		return urlencode(content,DEFAULT_CHARSET,URLENCODER,true);
	}

	/**
	 * Unreserved characters, i.e. alphanumeric, plus: {@code _ - ! . ~ ' ( ) *}
	 * <p>
	 * This list is the same as the {@code unreserved} list in <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
	 */
	private static final BitSet UNRESERVED = new BitSet(256);


	/**
	 * Safe characters for x-www-form-urlencoded data, as per java.net.URLEncoder and browser behaviour, i.e. alphanumeric plus {@code "-", "_", ".", "*"}
	 */
	private static final BitSet URLENCODER = new BitSet(256);

	static{
		// unreserved chars
		// alpha characters
		for(int i = 'a'; i<='z'; i++ ){
			UNRESERVED.set(i);
		}
		for(int i = 'A'; i<='Z'; i++ ){
			UNRESERVED.set(i);
		}
		// numeric characters
		for(int i = '0'; i<='9'; i++ ){
			UNRESERVED.set(i);
		}
		UNRESERVED.set('_'); // these are the charactes of the "mark" list
		UNRESERVED.set('-');
		UNRESERVED.set('.');
		UNRESERVED.set('*');
		URLENCODER.or(UNRESERVED); // skip remaining unreserved characters
		UNRESERVED.set('!');
		UNRESERVED.set('~');
		UNRESERVED.set('\'');
		UNRESERVED.set('(');
		UNRESERVED.set(')');

	}

	private static final int RADIX = 16;

	/**
	 * Adds all parameters within the Scanner to the list of <code>parameters</code>, as encoded by <code>encoding</code>. For example, a scanner containing the string <code>a=1&b=2&c=3</code> would
	 * add the {@link NameValuePair NameValuePairs} a=1, b=2, and c=3 to the list of parameters.
	 * @param parameters List to add parameters to.
	 * @param scanner Input that contains the parameters to parse.
	 * @param charset Encoding to use when decoding the parameters.
	 */
	private static void parse(final Map<String,String> parameters,final Scanner scanner,final Charset charset) {
		scanner.useDelimiter(PARAMETER_SEPARATOR);
		while(scanner.hasNext()){
			String name = null;
			String value = null;
			String token = scanner.next();
			int i = token.indexOf(NAME_VALUE_SEPARATOR);
			if(i!= -1){
				name = decodeFormFields(token.substring(0,i).trim(),charset);
				value = decodeFormFields(token.substring(i+1).trim(),charset);
			}else{
				name = decodeFormFields(token.trim(),charset);
			}
			parameters.put(name,value);
		}
	}

	/**
	 * Emcode/escape a portion of a URL, to use with the query part ensure {@code plusAsBlank} is true.
	 * @param content the portion to decode
	 * @param charset the charset to use
	 * @param blankAsPlus if {@code true}, then convert space to '+' (e.g. for www-url-form-encoded content), otherwise leave as is.
	 * @return
	 */
	private static String urlencode(final String content,final Charset charset,final BitSet safechars,final boolean blankAsPlus) {
		if(content==null){
			return null;
		}
		StringBuilder buf = new StringBuilder();
		ByteBuffer bb = charset.encode(content);
		while(bb.hasRemaining()){
			int b = bb.get()&0xff;
			if(safechars.get(b)){
				buf.append((char)b);
			}else if(blankAsPlus&&b==' '){
				buf.append('+');
			}else{
				buf.append("%");
				char hex1 = Character.toUpperCase(Character.forDigit(( b>>4 )&0xF,RADIX));
				char hex2 = Character.toUpperCase(Character.forDigit(b&0xF,RADIX));
				buf.append(hex1);
				buf.append(hex2);
			}
		}
		return buf.toString();
	}

	/**
	 * Decode/unescape a portion of a URL, to use with the query part ensure {@code plusAsBlank} is true.
	 * @param content the portion to decode
	 * @param charset the charset to use
	 * @param plusAsBlank if {@code true}, then convert '+' to space (e.g. for www-url-form-encoded content), otherwise leave as is.
	 * @return
	 */
	private static String urldecode(final String content,final Charset charset,final boolean plusAsBlank) {
		if(content==null){
			return null;
		}
		ByteBuffer bb = ByteBuffer.allocate(content.length());
		CharBuffer cb = CharBuffer.wrap(content);
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c=='%'&&cb.remaining()>=2){
				char uc = cb.get();
				char lc = cb.get();
				int u = Character.digit(uc,16);
				int l = Character.digit(lc,16);
				if(u!= -1&&l!= -1){
					bb.put((byte)( ( u<<4 )+l ));
				}else{
					bb.put((byte)'%');
					bb.put((byte)uc);
					bb.put((byte)lc);
				}
			}else if(plusAsBlank&&c=='+'){
				bb.put((byte)' ');
			}else{
				bb.put((byte)c);
			}
		}
		bb.flip();
		return charset.decode(bb).toString();
	}

	/**
	 * Decode/unescape www-url-form-encoded content.
	 * @param content the content to decode, will decode '+' as space
	 * @param charset the charset to use
	 * @return
	 */
	private static String decodeFormFields(final String content,final Charset charset) {
		if(content==null){
			return null;
		}
		return urldecode(content,charset!=null ? charset : DEFAULT_CHARSET,true);
	}

	/**
	 * Encode/escape www-url-form-encoded content.
	 * <p>
	 * Uses the {@link #URLENCODER} set of characters, rather than the {@link #UNRSERVED} set; this is for compatibilty with previous releases, URLEncoder.encode() and most browsers.
	 * @param content the content to encode, will convert space to '+'
	 * @param charset the charset to use
	 * @return
	 */
	private static String encodeFormFields(final String content,final Charset charset) {
		if(content==null){
			return null;
		}
		return urlencode(content,charset!=null ? charset : DEFAULT_CHARSET,URLENCODER,true);
	}

}
