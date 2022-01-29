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

        try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                try (GZIPOutputStream stream = new GZIPOutputStream(output)) {
                    IOUtils.copy(input, stream);
                    stream.finish();
                }
                return output.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] unzip(byte[] data) {
        try (InputStream input = new GZIPInputStream(new ByteArrayInputStream(data))) {
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                IOUtils.copy(input, output);
                return output.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
