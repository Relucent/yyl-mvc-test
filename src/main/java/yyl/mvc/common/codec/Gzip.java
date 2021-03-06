package yyl.mvc.common.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public class Gzip {

	public static byte[] zip(byte[] data) {
		ByteArrayOutputStream output = null;
		GZIPOutputStream stream = null;
		ByteArrayInputStream input = null;
		try {
			IOUtils.copy(input = new ByteArrayInputStream(data), //
					stream = new GZIPOutputStream(output = new ByteArrayOutputStream()));
			stream.finish();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(stream);
			IOUtils.closeQuietly(output);
		}
		return output.toByteArray();
	}

	public static byte[] unzip(byte[] data) {
		ByteArrayOutputStream output = null;
		InputStream input = null;
		try {
			IOUtils.copy(input = new GZIPInputStream(new ByteArrayInputStream(data)), //
					output = new ByteArrayOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return output.toByteArray();
	}
}
