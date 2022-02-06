package me.shukawam.fn;

import com.fnproject.fn.api.httpgateway.HTTPGatewayContext;

import java.util.logging.Logger;

/**
 * @author shukawam
 */
public class HttpGatewayFunction {
    private static final Logger LOGGER = Logger.getLogger(HttpGatewayFunction.class.getName());

    public String handleRequest(HTTPGatewayContext ctx) {
        simpleLogging(ctx);
        return "Hello, World";
    }

    private void simpleLogging(HTTPGatewayContext ctx) {
        if (ctx == null) {
            LOGGER.info("ctx is null");
        } else {
            LOGGER.info("START: HtpGatewayContext");
            // request url
            LOGGER.info("RequestURL: " + ctx.getRequestURL());
            // request method
            LOGGER.info("RequestMethod: " + ctx.getMethod());
            // request headers
            ctx.getHeaders().asMap().forEach((k, v) -> LOGGER.info(k + ": " + v));
            LOGGER.info("END: HtpGatewayContext");
            LOGGER.info("START: InvocationContext");
            LOGGER.info(ctx.getInvocationContext().getRuntimeContext().getAppID());
            LOGGER.info(ctx.getInvocationContext().getRuntimeContext().getAppName());
            LOGGER.info(ctx.getInvocationContext().getRuntimeContext().getFunctionID());
            LOGGER.info(ctx.getInvocationContext().getRuntimeContext().getFunctionName());
            ctx.getInvocationContext().getRuntimeContext().getInvokeInstance().ifPresent(i -> LOGGER.info(i.toString()));
            ctx.getInvocationContext().getRuntimeContext().getConfiguration().forEach((k, v) -> LOGGER.info(k + ": " + v));
            LOGGER.info("END: InvocationContext");
        }

    }
}
