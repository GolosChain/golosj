package eu.bittrade.libs.golosj.communication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import eu.bittrade.libs.golosj.base.models.SignedBlockHeader;
import eu.bittrade.libs.golosj.base.models.WithId;
import eu.bittrade.libs.golosj.base.models.deserializer.SteemJNodeFactory;
import eu.bittrade.libs.golosj.base.models.error.SteemError;
import eu.bittrade.libs.golosj.base.models.serializer.BooleanSerializer;
import eu.bittrade.libs.golosj.communication.dto.NotificationDTO;
import eu.bittrade.libs.golosj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.golosj.communication.dto.ResponseWrapperDTO;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemResponseError;
import eu.bittrade.libs.golosj.exceptions.SteemTimeoutException;
import eu.bittrade.libs.golosj.exceptions.SteemTransformationException;

/**
 * This class handles the communication to the Steem web socket API.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class CommunicationHandler extends Endpoint implements MessageHandler.Whole<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationHandler.class);

    private static ObjectMapper mapper = getObjectMapper();
    private ClientManager client;
    private Session session;
    private volatile String rawJsonResponse;
    private final Map<Long, CountDownLatch> locksMap = Collections.synchronizedMap(new HashMap<Long, CountDownLatch>());
    private final Map<Long, String> mResponcesMap = Collections.synchronizedMap(new HashMap<Long, String>());
    private final Object globalLock = new Object();

    /**
     * Initialize the Connection Handler.
     * <p>
     * If no connection to the Steem Node could be established.
     */
    public CommunicationHandler() {
        this.client = ClientManager.createClient();

        // Tyrus expects a SSL connection if the SSL_ENGINE_CONFIGURATOR
        // property is present. This leads to a "connection failed" error when
        // a non SSL secured protocol is used. Due to this we only add the
        // property when connecting to a SSL secured node.
        if (SteemJConfig.getInstance().isSslVerificationDisabled()
                && SteemJConfig.getInstance().getWebSocketEndpointURI().getScheme().equals("wss")
                || SteemJConfig.getInstance().getWebSocketEndpointURI().getScheme().equals("https")) {
            SslEngineConfigurator sslEngineConfigurator = new SslEngineConfigurator(new SslContextConfigurator());
            sslEngineConfigurator.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
        }

        client.setDefaultMaxSessionIdleTimeout(SteemJConfig.getInstance().getSocketTimeout());
        client.getProperties().put(ClientProperties.RECONNECT_HANDLER, new SteemJReconnectHandler());
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.session.addMessageHandler(this);

        LOGGER.info("Connection has been established.");
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        LOGGER.error("Connection has been closed.", closeReason);
        LOGGER.error("reason  = " + closeReason);
        if (closeReason.getCloseCode().getCode() == 1006 || closeReason.getCloseCode().getCode() == 1000)
            try {
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onError(Session session, Throwable thr) {
        LOGGER.error("Connection error.", thr);
    }

    /**
     * Perform a request to the web socket API whose response will automatically
     * get transformed into the given object.
     *
     * @param requestObject A request object that contains all needed parameters.
     * @param targetClass   The target class for the transformation.
     * @param <T>           The object that you want to map the result to.
     * @return The server response transformed into a list of given objects.
     * @throws SteemTimeoutException        If the server was not able to answer the request in the given
     *                                      time (@see
     *                                      {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                      setResponseTimeout()})
     * @throws SteemCommunicationException  If there is a connection problem.
     * @throws SteemTransformationException If the SteemJ is unable to transform the JSON response into a
     *                                      Java object.
     * @throws SteemResponseError           If the Server returned an error object.
     */
    public <T> List<T> performRequest(RequestWrapperDTO requestObject, Class<T> targetClass)
            throws SteemCommunicationException {
        if (session == null || !session.isOpen()) {
            synchronized (globalLock) {
                if (session == null || !session.isOpen()) {
                    connect();
                }
            }
        }
        try {
            System.out.println(requestObject.toString() + " target class is " + targetClass);

            sendMessageSynchronously(requestObject);

            System.out.println(rawJsonResponse);

            @SuppressWarnings("unchecked")
            ResponseWrapperDTO<T> response = mapper.readValue(mResponcesMap.get(requestObject.getId()), ResponseWrapperDTO.class);

            if (response == null || response.isEmpty() || rawJsonResponse.contains("\"name\":\"N5boost16exception_detail10clone_implINS0_19error_info")) {

                LOGGER.debug("The response was empty. The requested node may not provid the method {}.",
                        requestObject.getApiMethod());
                List<T> emptyResult = new ArrayList<>();
                emptyResult.add(null);
                return emptyResult;
            }

            if (response.getResponseId() != requestObject.getId()) {
                LOGGER.error("The request and the response id are not equal! This may cause some strange behaivior.");
            }

            // Make sure that the inner result object has the correct type.
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, targetClass);


            return mapper.convertValue(response.getResult(), type);
        } catch (JsonParseException | JsonMappingException e) {
            LOGGER.debug("Could not parse the response. Trying to transform it to an error object.", e);
            if (rawJsonResponse.contains("N5boost16exception"))
                return new ArrayList<>();// server error,
            // that returns server error instead of empty array
            try {
                // TODO: Find a better solution for errors in general.

                SteemResponseError responseError = new SteemResponseError(mapper.readValue(rawJsonResponse, SteemError.class));
                if (responseError.getError() != null &&
                        responseError.getError().getSteemErrorDetails() != null &&
                        responseError.getError().getSteemErrorDetails().getMessage() != null &&
                        responseError.getError().getSteemErrorDetails().getMessage().toLowerCase()
                                .contains("unable to acquire read lock".toLowerCase())) {
                    System.out.println("unable to acquire read lock");
                    return performRequest(requestObject, targetClass);
                } else throw responseError;

            } catch (IOException ex) {
                ex.printStackTrace();
                throw new SteemTransformationException("Could not transform the response into an object.", ex);
            } catch (IllegalArgumentException ex) {
                throw new SteemTransformationException("corrupted input", ex);
            }

        } catch (IOException | EncodeException | InterruptedException e) {
            throw new SteemCommunicationException("Could not send the message to the Steem Node.", e);
        } catch (NullPointerException e) {
            throw new SteemCommunicationException("Could convert nu;l object to response", e);
        }
    }

    /**
     * This method establishes a new connection to the web socket Server.
     *
     * @throws SteemCommunicationException If there is a connection problem.
     */
    private void connect() throws SteemCommunicationException {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }

            client.connectToServer(this, SteemJConfig.getInstance().getClientEndpointConfig(),
                    SteemJConfig.getInstance().getWebSocketEndpointURI());
        } catch (DeploymentException | IOException e) {
            throw new SteemCommunicationException("Could not connect to the server.", e);
        }
    }

    /**
     * Sends a message to the Steem Node and waits for an answer.
     *
     * @param requestObject The object to send.
     * @throws IOException           If something went wrong.
     * @throws EncodeException       If something went wrong.
     * @throws SteemTimeoutException If the node took to long to answer.
     * @throws InterruptedException  If something went wrong.
     */
    private void sendMessageSynchronously(RequestWrapperDTO requestObject)
            throws IOException, EncodeException, SteemTimeoutException, InterruptedException {
        System.out.println("send message sync from " + Thread.currentThread().getName());

        CountDownLatch latch = new CountDownLatch(1);

        locksMap.put(requestObject.getId(), latch);

        session.getBasicRemote().sendObject(requestObject);

        // Wait until we received a response from the Server.
        if (SteemJConfig.getInstance().getResponseTimeout() == 0) {
            latch.await();
        } else {
            long timeout = 25_000L;
            int trysCount = (int) (SteemJConfig.getInstance().getResponseTimeout() / timeout);
            for (int i = 0; i < trysCount; i++) {
                try {
                    if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                        session.close();
                        session.getBasicRemote().sendObject(requestObject);
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!session.isOpen()) try {
                        connect();
                    } catch (SteemCommunicationException e1) {
                        e1.printStackTrace();
                    }
                    session.getBasicRemote().sendObject(requestObject);
                    trysCount++;
                }
            }

            String errorMessage = "Timeout occured. The WebSocket server was not able to answer in "
                    + SteemJConfig.getInstance().getResponseTimeout() + " millisecond(s).";

            LOGGER.error(errorMessage);
            throw new SteemTimeoutException(errorMessage);

        }
    }

    @Override
    public void onMessage(String message) {
        // Check if we are waiting for an answer.

        if (locksMap.size() > 0) {
            this.rawJsonResponse = message;


            WithId withId = null;
            try {
                withId = mapper.readValue(message, WithId.class);
                System.out.println("id = " + withId);
                CountDownLatch latch = locksMap.get(withId.getId());
                if (latch != null) {
                    locksMap.remove(withId.getId());
                    mResponcesMap.put(withId.getId(), message);
                    latch.countDown();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            // A message has been send while we are not waiting for it - It can
            // be a callback.
            LOGGER.debug("Received callback: {}", message);

            try {
                NotificationDTO response = mapper.readValue(message, NotificationDTO.class);

                // Make sure that the inner result object is a BlockHeader.
                CallbackHub.getInstance().getCallbackByUuid(Integer.valueOf(response.getParams()[0].toString()))
                        .onNewBlock(mapper.convertValue(((ArrayList<Object>) (response.getParams()[1])).get(0),
                                SignedBlockHeader.class));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                LOGGER.error("Could not parse callback {}.", e);
            }
        }
    }

    /**
     * Get a preconfigured jackson Object Mapper instance.
     *
     * @return The object mapper.
     */
    public static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SteemJConfig.getInstance().getDateTimePattern());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(SteemJConfig.getInstance().getTimeZoneId()));

            mapper.setDateFormat(simpleDateFormat);
            mapper.setTimeZone(TimeZone.getTimeZone(SteemJConfig.getInstance().getTimeZoneId()));
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            SimpleModule simpleModule = new SimpleModule("BooleanAsString", new Version(1, 0, 0, null, null, null));
            simpleModule.addSerializer(Boolean.class, new BooleanSerializer());
            simpleModule.addSerializer(boolean.class, new BooleanSerializer());
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.registerModule(simpleModule);
            mapper.setNodeFactory(new SteemJNodeFactory());
        }

        return mapper;
    }
}
