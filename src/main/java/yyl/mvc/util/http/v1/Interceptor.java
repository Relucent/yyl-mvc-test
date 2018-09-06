package yyl.mvc.util.http.v1;

import java.io.IOException;

public interface Interceptor {

	Response intercept(Chain chain) throws IOException;

	static interface Chain {

		Request request();

		Response proceed(Request request) throws IOException;
	}
}
