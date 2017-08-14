package gateway.transformer;

import config.RequestHeader;
import config.property.ConfigProperties;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;

public class HeadersRequestTransformer extends ProxyRequestTransformer {

  private ConfigProperties configProperties;
  private PasswordEncoder encoder;

  public HeadersRequestTransformer(ConfigProperties configProperties, PasswordEncoder encoder) {
    this.configProperties = configProperties;
    this.encoder = encoder;
  }

  @Override
  public RequestBuilder transform(HttpServletRequest request) throws NoSuchRequestHandlingMethodException, URISyntaxException, IOException {
    RequestBuilder requestBuilder = predecessor.transform(request);

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      if (headerName.equals("x-access-token")) {
        requestBuilder.addHeader(headerName, headerValue);
      }
    }
    requestBuilder.addHeader(RequestHeader.GATEWAY_PASSKEY, encoder.encode(configProperties.getGatewayPasskey()));
    return requestBuilder;
  }

}
