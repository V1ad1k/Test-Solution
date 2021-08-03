package client;

import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class NonSecureClientBuilder {

    private final OkHttpClient.Builder clientBuilder;

    public NonSecureClientBuilder() throws Exception {
        OkHttpClient client = new OkHttpClient();
        clientBuilder = client.newBuilder();

        final X509TrustManager trustAllCerts = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) {
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, new TrustManager[]{trustAllCerts}, new java.security.SecureRandom());
        clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts);

        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        clientBuilder.hostnameVerifier(hostnameVerifier);
    }

    public OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }
}
