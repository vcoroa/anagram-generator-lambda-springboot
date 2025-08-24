package br.com.vaniltoncoroa.greetings.config;

import br.com.vaniltoncoroa.greetings.App;
import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler {

    // Note os 4 genéricos (os 2 últimos como curingas):
    private static LambdaContainerHandler<AwsProxyRequest, AwsProxyResponse, ?, ?> handler;

    static {
        try {
            handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                    .defaultProxy()                  // API Gateway REST (v1) proxy
                    .asyncInit()                     // inicialização preguiçosa ajuda no cold start
                    .springBootApplication(App.class)
                    .buildAndInitialize();           // <- precisa try/catch
        } catch (ContainerInitializationException e) {
            // Falhe rápido se a app não subir
            throw new RuntimeException("Falha ao inicializar o Spring Boot no Lambda", e);
        }
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.proxyStream(input, output, context);
        output.flush();
    }
}


