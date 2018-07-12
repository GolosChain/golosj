package eu.bittrade.libs.golosj.communication;

import javax.websocket.CloseReason;

import org.glassfish.tyrus.client.ClientManager.ReconnectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.bittrade.libs.golosj.configuration.SteemJConfig;

/**
 * This class handles connection issues.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemJReconnectHandler extends ReconnectHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationHandler.class);

    @Override
    public boolean onDisconnect(CloseReason closeReason) {
        if (SteemJConfig.getInstance().getSocketTimeout() <= 0) {
            LOGGER.info(
                    "The connection has been closed, but SteemJ is configured to never close the conenction. Initiating reconnect.");
            return true;
        }

        return false;
    }

    @Override
    public boolean onConnectFailure(Exception exception) {
        // Always try to reconnect.
        // TODO: Switch the node before reconnecting.
        return true;
    }

    @Override
    public long getDelay() {
        return 0;
    }

}
