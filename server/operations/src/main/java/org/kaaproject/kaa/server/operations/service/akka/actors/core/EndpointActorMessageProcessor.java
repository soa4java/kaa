/*
 * Copyright 2014 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaaproject.kaa.server.operations.service.akka.actors.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.kaaproject.kaa.common.TransportType;
import org.kaaproject.kaa.common.channels.protocols.kaatcp.messages.PingResponse;
import org.kaaproject.kaa.common.dto.EndpointProfileDto;
import org.kaaproject.kaa.common.dto.EventClassFamilyVersionStateDto;
import org.kaaproject.kaa.common.endpoint.gen.EndpointAttachResponse;
import org.kaaproject.kaa.common.endpoint.gen.EndpointDetachRequest;
import org.kaaproject.kaa.common.endpoint.gen.EndpointDetachResponse;
import org.kaaproject.kaa.common.endpoint.gen.Event;
import org.kaaproject.kaa.common.endpoint.gen.EventSyncRequest;
import org.kaaproject.kaa.common.endpoint.gen.EventSyncResponse;
import org.kaaproject.kaa.common.endpoint.gen.LogEntry;
import org.kaaproject.kaa.common.endpoint.gen.LogSyncRequest;
import org.kaaproject.kaa.common.endpoint.gen.LogSyncResponse;
import org.kaaproject.kaa.common.endpoint.gen.SyncRequest;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponse;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachNotification;
import org.kaaproject.kaa.common.endpoint.gen.UserDetachNotification;
import org.kaaproject.kaa.common.endpoint.gen.UserSyncResponse;
import org.kaaproject.kaa.common.hash.EndpointObjectHash;
import org.kaaproject.kaa.server.operations.pojo.Base64Util;
import org.kaaproject.kaa.server.operations.pojo.SyncResponseHolder;
import org.kaaproject.kaa.server.operations.pojo.exceptions.GetDeltaException;
import org.kaaproject.kaa.server.operations.service.OperationsService;
import org.kaaproject.kaa.server.operations.service.akka.actors.core.ChannelMap.ChannelMetaData;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.endpoint.EndpointStopMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.endpoint.SyncRequestMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.logs.LogEventPackMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.notification.ThriftNotificationMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.session.ActorTimeoutMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.session.ChannelTimeoutMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.session.RequestTimeoutMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.session.TimeoutMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.topic.NotificationMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.topic.TopicRegistrationRequestMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointEventDeliveryMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointEventDeliveryMessage.EventDeliveryStatus;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointEventReceiveMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointEventSendMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointUserActionMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointUserAttachMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointUserConnectMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointUserDetachMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.core.user.EndpointUserDisconnectMessage;
import org.kaaproject.kaa.server.operations.service.akka.messages.io.ChannelAware;
import org.kaaproject.kaa.server.operations.service.akka.messages.io.request.SyncStatistics;
import org.kaaproject.kaa.server.operations.service.akka.messages.io.response.NettySessionResponseMessage;
import org.kaaproject.kaa.server.operations.service.event.EventClassFamilyVersion;
import org.kaaproject.kaa.server.operations.service.http.commands.ChannelType;
import org.kaaproject.kaa.server.operations.service.logs.LogEvent;
import org.kaaproject.kaa.server.operations.service.logs.LogEventPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import akka.actor.ActorContext;
import akka.actor.ActorRef;

public class EndpointActorMessageProcessor {

    private static final int ENDPOINT_ACTOR_INACTIVITY_TIMEOUT = 60 * 1000;

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(EndpointActorMessageProcessor.class);

    /** The operations service. */
    private final OperationsService operationsService;

    /** The map of channel-request-response entities. */
    private final ChannelMap channelMap;

    /** The app token. */
    private final String appToken;

    /** The key. */
    private final EndpointObjectHash key;

    /** The actor key. */
    private final String actorKey;

    /** The actor key. */
    private final String endpointKey;

    /** The sync time. */
    private long syncTime;

    private long lastActivityTime;

    private boolean userRegistrationRequestSent;

    private String userId;

    private int processedEventSeqNum = Integer.MIN_VALUE;

    protected EndpointActorMessageProcessor(OperationsService operationsService, String appToken, EndpointObjectHash key, String actorKey) {
        super();
        this.operationsService = operationsService;
        this.appToken = appToken;
        this.key = key;
        this.actorKey = actorKey;
        this.endpointKey = Base64Util.encode(key.getData());
        this.channelMap = new ChannelMap(this.endpointKey, this.actorKey);
    }

    public void processEndpointSync(ActorContext context, SyncRequestMessage message) {
        sync(context, message);
    }

    public void processEndpointEventReceiveMessage(ActorContext context, EndpointEventReceiveMessage message) {
        EndpointEventDeliveryMessage response;
        List<ChannelMetaData> eventChannels = channelMap.getByTransportType(TransportType.EVENT);
        if (!eventChannels.isEmpty()) {
            for (ChannelMetaData eventChannel : eventChannels) {
                addEventsAndReply(context, eventChannel, message);
            }
            response = new EndpointEventDeliveryMessage(message, EventDeliveryStatus.SUCCESS);
        } else {
            LOG.debug("[{}] Message ignored due to no channel contexts registered for events", actorKey, message);
            response = new EndpointEventDeliveryMessage(message, EventDeliveryStatus.FAILURE);
            userRegistrationRequestSent = false;
        }
        tellParent(context, response);
    }

    protected void tellParent(ActorContext context, Object response) {
        context.parent().tell(response, context.self());
    }

    public void processThriftNotification(ActorContext context, ThriftNotificationMessage message) {
        Set<ChannelMetaData> channels = new HashSet<>();

        channels.addAll(channelMap.getByTransportType(TransportType.CONFIGURATION));
        channels.addAll(channelMap.getByTransportType(TransportType.NOTIFICATION));

        LOG.debug("[{}][{}] Processing thrift norification for {} channels", endpointKey, actorKey, channels.size());

        for (ChannelMetaData channel : channels) {
            SyncRequest syncRequest = channel.getRequest().getRequest();
            SyncResponse syncResponse = channel.getResponse().getResponse();
            if (syncRequest.getConfigurationSyncRequest() != null) {
                int oldSeqNumnber = syncRequest.getConfigurationSyncRequest().getAppStateSeqNumber();
                int newSeqNumnber = syncResponse.getConfigurationSyncResponse().getAppStateSeqNumber();
                LOG.debug("[{}][{}] Change original configuration request {} appSeqNumber from {} to {}", endpointKey, actorKey, syncRequest, oldSeqNumnber,
                        newSeqNumnber);
                syncRequest.getConfigurationSyncRequest().setAppStateSeqNumber(newSeqNumnber);
            }
            if (syncRequest.getNotificationSyncRequest() != null) {
                int oldSeqNumnber = syncRequest.getNotificationSyncRequest().getAppStateSeqNumber();
                int newSeqNumnber = syncResponse.getNotificationSyncResponse().getAppStateSeqNumber();
                LOG.debug("[{}][{}] Change original notification request {} appSeqNumber from {} to {}", endpointKey, actorKey, syncRequest, oldSeqNumnber,
                        newSeqNumnber);
                syncRequest.getNotificationSyncRequest().setAppStateSeqNumber(newSeqNumnber);
            }
            LOG.debug("[{}][{}] Processing request {}", endpointKey, actorKey, syncRequest);
            sync(context, channel.getRequest());
        }
    }

    public void processNotification(ActorContext context, NotificationMessage message) {
        LOG.debug("[{}][{}] Processing notification {}", endpointKey, actorKey, message);
        List<ChannelMetaData> channels = channelMap.getByTransportType(TransportType.NOTIFICATION);
        for (ChannelMetaData channel : channels) {
            LOG.debug("[{}][{}] processing channel {} and response {}", endpointKey, actorKey, channel, channel.getResponse().getResponse());
            SyncResponse syncResponse = operationsService.updateSyncResponse(channel.getResponse().getResponse(), message.getNotifications(),
                    message.getUnicastNotificationId());
            if(syncResponse != null){
                LOG.debug("[{}][{}] processed channel {} and response {}", endpointKey, actorKey, channel, syncResponse);
                sendReply(context, channel.getRequest(), syncResponse);
                if (!channel.getType().isAsync()) {
                    channelMap.removeChannel(channel);
                }
            }
        }
    }

    public void processRequestTimeoutMessage(ActorContext context, RequestTimeoutMessage message) {
        ChannelMetaData channel = channelMap.getByRequestId(message.getRequestId());
        if (channel != null) {
            SyncResponseHolder response = channel.getResponse();
            sendReply(context, channel.getRequest(), response.getResponse());
            if (!channel.getType().isAsync()) {
                channelMap.removeChannel(channel);
            }
        } else {
            LOG.debug("[{}][{}] Failed to find request by id [{}].", endpointKey, actorKey, message.getRequestId());
        }
    }

    public void processActorTimeoutMessage(ActorContext context, ActorTimeoutMessage message) {
        if (lastActivityTime <= message.getLastActivityTime()) {
            LOG.debug("[{}][{}] Request stop of endpoint actor due to inactivity timeout", endpointKey, actorKey);
            tellParent(context, new EndpointStopMessage(key, actorKey, context.self()));
        }
    }

    private void sync(ActorContext context, SyncRequestMessage requestMessage) {
        try {
            lastActivityTime = System.currentTimeMillis();
            long start = lastActivityTime;

            ChannelMetaData channel = initChannel(context, requestMessage);

            SyncRequest request = channel.getRequest().getRequest();
            ChannelType channelType = channel.getType();
            LOG.debug("[{}][{}] Processing sync request {} from channel [{}]", endpointKey, actorKey, request, requestMessage.getChannelUuid());

            SyncResponseHolder responseHolder = operationsService.sync(request);

            EndpointProfileDto endpointProfile = responseHolder.getEndpointProfile();

            if (endpointProfile != null) {
                LogSyncResponse logUploadResponse = sendLogs(context, endpointProfile, request.getLogSyncRequest());
                if (logUploadResponse != null) {
                    responseHolder.getResponse().setLogSyncResponse(logUploadResponse);
                }

                if (isValidForEvents(endpointProfile)) {
                    if (userId != null && !userId.equals(endpointProfile.getEndpointUserId())) {
                        sendDisconnectFromOldUser(context, endpointProfile);
                        userRegistrationRequestSent = false;
                    }
                    if (!userRegistrationRequestSent) {
                        userId = endpointProfile.getEndpointUserId();
                        sendConnectToNewUser(context, endpointProfile);
                        userRegistrationRequestSent = true;
                    } else {
                        LOG.trace("[{}][{}] User registration request is already sent.", endpointKey, actorKey);
                    }
                    sendEventsIfPresent(context, request.getEventSyncRequest());
                } else {
                    LOG.debug("[{}][{}] Endpoint profile is not valid for send/receive events. Either no assigned user or no event families in sdk",
                            endpointKey, actorKey);
                }

                processUserAttachDetachResults(context, request, responseHolder);
            } else {
                LOG.warn("[{}][{}] Endpoint profile is not set after request processing!", endpointKey, actorKey);
            }

            this.syncTime += System.currentTimeMillis() - start;
            LOG.debug("[{}][{}] SyncResponseHolder {}", endpointKey, actorKey, responseHolder);

            if (channelType.isAsync()) {
                LOG.debug("[{}][{}] Adding async request from channel [{}] to map ", endpointKey, actorKey, requestMessage.getChannelUuid());
                channel.updateReqResp(responseHolder);
                subscribeToTopics(context, responseHolder);
                sendReply(context, requestMessage, responseHolder.getResponse());
            } else {
                if (channelType.isLongPoll() && !responseHolder.requireImmediateReply()) {
                    LOG.debug("[{}][{}] Adding long poll request from channel [{}] to map ", endpointKey, actorKey, requestMessage.getChannelUuid());
                    channel.updateReqResp(responseHolder);
                    subscribeToTopics(context, responseHolder);
                    scheduleTimeoutMessage(context, requestMessage.getChannelUuid(), getDelay(requestMessage, start));
                } else {
                    sendReply(context, requestMessage, responseHolder.getResponse());
                    channelMap.removeChannel(channel);
                }
            }
        } catch (GetDeltaException e) {
            LOG.error("[{}][{}] processEndpointRequest", endpointKey, actorKey, e);
            sendReply(context, requestMessage, e);
        }
    }

    private void sendConnectToNewUser(ActorContext context, EndpointProfileDto endpointProfile) {
        List<EventClassFamilyVersion> ecfVersions = convertToECFVersions(endpointProfile.getEcfVersionStates());
        EndpointUserConnectMessage userRegistrationMessage = new EndpointUserConnectMessage(userId, key, ecfVersions, appToken, context.self());
        LOG.debug("[{}][{}] Sending user registration request {}", endpointKey, actorKey, userRegistrationMessage);
        context.parent().tell(userRegistrationMessage, context.self());
    }

    private void sendDisconnectFromOldUser(ActorContext context, EndpointProfileDto endpointProfile) {
        LOG.debug("[{}][{}] Detected user change from [{}] to [{}]", endpointKey, actorKey, userId, endpointProfile.getEndpointUserId());
        EndpointUserDisconnectMessage userDisconnectMessage = new EndpointUserDisconnectMessage(userId, key, appToken, context.self());
        context.parent().tell(userDisconnectMessage, context.self());
    }

    private long getDelay(SyncRequestMessage requestMessage, long start) {
        long delay = requestMessage.getRequest().getSyncRequestMetaData().getTimeout() - (System.currentTimeMillis() - start);
        return delay;
    }

    private ChannelMetaData initChannel(ActorContext context, SyncRequestMessage requestMessage) {
        ChannelMetaData channel = channelMap.getById(requestMessage.getChannelUuid());
        if (channel == null) {
            channel = new ChannelMetaData(requestMessage);

            if (!channel.getType().isAsync() && channel.getType().isLongPoll()) {
                LOG.debug("[{}][{}] Received request using long poll channel.", endpointKey, actorKey);
                // Probably old long poll channels lost connection. Sending
                // reply to them just in case
                List<ChannelMetaData> channels = channelMap.getByTransportType(TransportType.EVENT);
                for (ChannelMetaData oldChannel : channels) {
                    if (!oldChannel.getType().isAsync() && channel.getType().isLongPoll()) {
                        LOG.debug("[{}][{}] Closing old long poll channel [{}]", endpointKey, actorKey, oldChannel.getId());
                        sendReply(context, oldChannel.getRequest(), oldChannel.getResponse().getResponse());
                        channelMap.removeChannel(oldChannel);
                    }
                }
            }

            long time = System.currentTimeMillis();

            channel.setLastActivityTime(time);

            if(channel.getType().isAsync() && channel.getKeepAlive() > 0){
                scheduleKeepAliveCheck(context, channel);
            }

            channelMap.addChannel(channel);
        } else {
            if (channel.getType().isAsync()) {
                LOG.debug("[{}][{}] Updating request for async channel {}", endpointKey, actorKey, channel);
                channel.updateRequest(requestMessage);
            }
        }
        return channel;
    }

    private void scheduleKeepAliveCheck(ActorContext context, ChannelMetaData channel) {
        TimeoutMessage message = new ChannelTimeoutMessage(channel.getId(), channel.getLastActivityTime());
        LOG.debug("Scheduling channel timeout message: {} to timout in {}", message, channel.getKeepAlive() * 1000);
        scheduleTimeoutMessage(context, message, channel.getKeepAlive() * 1000);
    }

    private void processUserAttachDetachResults(ActorContext context, SyncRequest request, SyncResponseHolder responseHolder) {
        if (responseHolder.getResponse().getUserSyncResponse() != null) {
            List<EndpointAttachResponse> attachResponses = responseHolder.getResponse().getUserSyncResponse().getEndpointAttachResponses();
            if (attachResponses != null) {
                for (EndpointAttachResponse response : attachResponses) {
                    if (response.getResult() != SyncResponseResultType.SUCCESS) {
                        LOG.debug("[{}][{}] Skipped unsuccessful attach response [{}]", endpointKey, actorKey, response.getRequestId());
                        continue;
                    }
                    EndpointUserAttachMessage attachMessage = new EndpointUserAttachMessage(EndpointObjectHash.fromBytes(Base64Util.decode(response
                            .getEndpointKeyHash())), userId, endpointKey);
                    context.parent().tell(attachMessage, context.self());
                    LOG.debug("[{}][{}] Notification to attached endpoint [{}] sent", endpointKey, actorKey, response.getEndpointKeyHash());
                }
            }

            List<EndpointDetachRequest> detachRequests = request.getUserSyncRequest() == null ? null : request.getUserSyncRequest().getEndpointDetachRequests();
            if (detachRequests != null) {
                for (EndpointDetachRequest detachRequest : detachRequests) {
                    for (EndpointDetachResponse detachResponse : responseHolder.getResponse().getUserSyncResponse().getEndpointDetachResponses()) {
                        if (detachRequest.getRequestId().equals(detachResponse.getRequestId())) {
                            if (detachResponse.getResult() != SyncResponseResultType.SUCCESS) {
                                LOG.debug("[{}][{}] Skipped unsuccessful detach response [{}]", endpointKey, actorKey, detachResponse.getRequestId());
                                continue;
                            }
                            EndpointUserDetachMessage attachMessage = new EndpointUserDetachMessage(EndpointObjectHash.fromBytes(Base64Util
                                    .decode(detachRequest.getEndpointKeyHash())), userId, endpointKey);
                            context.parent().tell(attachMessage, context.self());
                            LOG.debug("[{}][{}] Notification to detached endpoint [{}] sent", endpointKey, actorKey, detachRequest.getEndpointKeyHash());
                        }
                    }
                }
            }
        }
    }

    protected LogSyncResponse sendLogs(ActorContext context, EndpointProfileDto profile, LogSyncRequest logUploadRequest) {
        if (logUploadRequest != null && logUploadRequest.getLogEntries() != null) {
            LOG.debug("[{}][{}] Processing log upload request {}", endpointKey, actorKey, logUploadRequest.getLogEntries().size());
            LogEventPack logPack = new LogEventPack();
            logPack.setDateCreated(System.currentTimeMillis());
            logPack.setEndpointKey(Base64Util.encode(key.getData()));
            List<LogEvent> logEvents = new ArrayList<>(logUploadRequest.getLogEntries().size());
            for (LogEntry logEntry : logUploadRequest.getLogEntries()) {
                LogEvent logEvent = new LogEvent();
                logEvent.setLogData(logEntry.getData().array());
                logEvents.add(logEvent);
            }
            logPack.setEvents(logEvents);
            logPack.setLogSchemaVersion(profile.getLogSchemaVersion());
            context.parent().tell(new LogEventPackMessage(logPack), context.self());
            return new LogSyncResponse(logUploadRequest.getRequestId(), SyncResponseResultType.SUCCESS);
        } else {
            return null;
        }
    }

    protected void scheduleActorTimeout(ActorContext context) {
        if (channelMap.isEmpty()) {
            scheduleTimeoutMessage(context, new ActorTimeoutMessage(lastActivityTime), ENDPOINT_ACTOR_INACTIVITY_TIMEOUT);
        }
    }

    /**
     * Subscribe to topics.
     *
     * @param response
     *            the response
     */
    private void subscribeToTopics(ActorContext context, SyncResponseHolder response) {
        for (Entry<String, Integer> entry : response.getSubscriptionStates().entrySet()) {
            TopicRegistrationRequestMessage topicSubscriptionMessage = new TopicRegistrationRequestMessage(entry.getKey(), entry.getValue(),
                    response.getSystemNfVersion(), response.getUserNfVersion(), appToken, key, context.self());
            context.parent().tell(topicSubscriptionMessage, context.self());
        }
    }

    private void scheduleTimeoutMessage(ActorContext context, UUID requestId, long delay) {
        scheduleTimeoutMessage(context, new RequestTimeoutMessage(requestId), delay);
    }

    private void scheduleTimeoutMessage(ActorContext context, TimeoutMessage message, long delay) {
        context.system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), context.self(), message, context.dispatcher(), context.self());
    }

    private void addEventsAndReply(ActorContext context, ChannelMetaData channel, EndpointEventReceiveMessage message) {
        SyncRequestMessage pendingRequest = channel.getRequest();
        SyncResponseHolder pendingResponse = channel.getResponse();

        EventSyncResponse eventResponse = pendingResponse.getResponse().getEventSyncResponse();
        if (eventResponse == null) {
            eventResponse = new EventSyncResponse();
            pendingResponse.getResponse().setEventSyncResponse(eventResponse);
        }

        eventResponse.setEvents(message.getEvents());
        sendReply(context, pendingRequest, pendingResponse.getResponse());
        if (!channel.getType().isAsync()) {
            channelMap.removeChannel(channel);
        }
    }

    private void sendReply(ActorContext context, SyncRequestMessage request, SyncResponse syncResponse) {
        sendReply(context, request, null, syncResponse);
    }

    private void sendReply(ActorContext context, SyncRequestMessage request, GetDeltaException e) {
        sendReply(context, request, e, null);
    }

    /**
     * Send reply.
     *
     * @param pendingRequest
     *            the pending request
     * @param syncResponse
     *            the sync response
     */
    private void sendReply(ActorContext context, SyncRequestMessage request, GetDeltaException e, SyncResponse syncResponse) {
        LOG.debug("[{}] response: {}", actorKey, syncResponse);

        SyncStatistics stats = request.getCommand().getSyncStatistics();
        if(stats != null){
            stats.reportSyncTime(syncTime);
        }

        NettySessionResponseMessage response = new NettySessionResponseMessage(request.getSession(), syncResponse, request.getCommand().getResponseBuilder(),
                request.getCommand().getErrorBuilder());

        tellActor(context,  request.getOriginator(), response);
        scheduleActorTimeout(context);
    }

    protected void tellActor(ActorContext context, ActorRef target, Object message){
        target.tell(message, context.self());
    }

    protected void sendEventsIfPresent(ActorContext context, EventSyncRequest request) {
        if (request != null) {
            List<Event> events = request.getEvents();
            if (userId != null && events != null && !events.isEmpty()) {
                LOG.debug("[{}][{}] Processing events {} with seq number > {}", endpointKey, actorKey, events, processedEventSeqNum);
                List<Event> eventsToSend = new ArrayList<>(events.size());
                int maxSentEventSeqNum = processedEventSeqNum;
                for (Event event : events) {
                    if (event.getSeqNum() > processedEventSeqNum) {
                        event.setSource(endpointKey);
                        eventsToSend.add(event);
                        maxSentEventSeqNum = Math.max(event.getSeqNum(), maxSentEventSeqNum);
                    } else {
                        LOG.debug("[{}][{}] Ignoring duplicate/old event {} due to seq number < {}", endpointKey, actorKey, events, processedEventSeqNum);
                    }
                }
                processedEventSeqNum = maxSentEventSeqNum;
                if (!eventsToSend.isEmpty()) {
                    EndpointEventSendMessage message = new EndpointEventSendMessage(userId, eventsToSend, key, appToken, context.self());
                    context.parent().tell(message, context.self());
                }
            }
        }
    }

    private boolean isValidForEvents(EndpointProfileDto profile) {
        return profile.getEndpointUserId() != null && !profile.getEndpointUserId().isEmpty() && profile.getEcfVersionStates() != null
                && !profile.getEcfVersionStates().isEmpty();
    }

    private List<EventClassFamilyVersion> convertToECFVersions(List<EventClassFamilyVersionStateDto> ecfVersionStates) {
        List<EventClassFamilyVersion> result = new ArrayList<>(ecfVersionStates.size());
        for (EventClassFamilyVersionStateDto dto : ecfVersionStates) {
            result.add(new EventClassFamilyVersion(dto.getEcfId(), dto.getVersion()));
        }
        return result;
    }

    public void processEndpointUserActionMessage(ActorContext context, EndpointUserActionMessage message) {
        Set<ChannelMetaData> eventChannels = new HashSet<ChannelMetaData>();
        eventChannels.addAll(channelMap.getByTransportType(TransportType.EVENT));
        eventChannels.addAll(channelMap.getByTransportType(TransportType.USER));
        LOG.debug("[{}][{}] Current Endpoint was attached/detached from user. Need to close all current event channels {}", endpointKey, actorKey,
                eventChannels.size());
        userRegistrationRequestSent = false;
        if (!eventChannels.isEmpty()) {
            for (ChannelMetaData channel : eventChannels) {
                SyncRequestMessage pendingRequest = channel.getRequest();
                SyncResponse pendingResponse = channel.getResponse().getResponse();

                UserSyncResponse userSyncResponse = pendingResponse.getUserSyncResponse();
                if (userSyncResponse != null) {
                    if (message instanceof EndpointUserAttachMessage) {
                        userSyncResponse.setUserAttachNotification(new UserAttachNotification(message.getUserId(), message.getOriginator()));
                        LOG.debug("[{}][{}] Adding user attach notification", endpointKey, actorKey);
                    } else if (message instanceof EndpointUserDetachMessage) {
                        userSyncResponse.setUserDetachNotification(new UserDetachNotification(message.getOriginator()));
                        LOG.debug("[{}][{}] Adding user detach notification", endpointKey, actorKey);
                    }
                }

                LOG.debug("[{}][{}] sending reply to [{}] channel", endpointKey, actorKey, channel.getId());
                sendReply(context, pendingRequest, pendingResponse);
                if (channel.getType().isAsync()) {
                    sync(context, channel.getRequest());
                } else {
                    channelMap.removeChannel(channel);
                }
            }
        } else {
            LOG.debug("[{}][{}] Message ignored due to no channel contexts registered for events", endpointKey, actorKey, message);
        }
    }

    public boolean processDisconnectMessage(ActorContext context, ChannelAware message) {
        LOG.debug("[{}][{}] Received disconnect message for channel [{}]", endpointKey, actorKey, message.getChannelUuid());
        ChannelMetaData channel = channelMap.getById(message.getChannelUuid());
        if(channel != null){
            channelMap.removeChannel(channel);
            return true;
        }else{
            LOG.debug("[{}][{}] Can't find channel by uuid [{}]", endpointKey, actorKey, message.getChannelUuid());
            return false;
        }
    }

    public boolean processPingMessage(ActorContext context, ChannelAware message) {
        LOG.debug("[{}][{}] Received ping message for channel [{}]", endpointKey, actorKey, message.getChannelUuid());
        ChannelMetaData channel = channelMap.getById(message.getChannelUuid());
        if(channel != null){
            long lastActivityTime = System.currentTimeMillis();
            LOG.debug("[{}][{}] Updating last activity time for channel [{}] to ", endpointKey, actorKey, message.getChannelUuid(), lastActivityTime);
            channel.setLastActivityTime(lastActivityTime);
            scheduleKeepAliveCheck(context, channel);
            channel.getContext().writeAndFlush(new PingResponse());
            return true;
        }else{
            LOG.debug("[{}][{}] Can't find channel by uuid [{}]", endpointKey, actorKey, message.getChannelUuid());
            return false;
        }
    }

    public boolean processChannelTimeoutMessage(ActorContext context, ChannelTimeoutMessage message) {
        LOG.debug("[{}][{}] Received channel timeout message for channel [{}]", endpointKey, actorKey, message.getChannelUuid());
        ChannelMetaData channel = channelMap.getById(message.getChannelUuid());
        if(channel != null){
            if(channel.getLastActivityTime() <= message.getLastActivityTime()){
                LOG.debug("[{}][{}] Timeout message accepted for channel [{}]. Last activity time {} and timeout is {} ", endpointKey, actorKey, message.getChannelUuid(), channel.getLastActivityTime(), message.getLastActivityTime());
                channelMap.removeChannel(channel);
                return true;
            }else{
                LOG.debug("[{}][{}] Timeout message ignored for channel [{}]. Last activity time {} and timeout is {} ", endpointKey, actorKey, message.getChannelUuid(), channel.getLastActivityTime(), message.getLastActivityTime());
                return false;
            }
        }else{
            LOG.debug("[{}][{}] Can't find channel by uuid [{}]", endpointKey, actorKey, message.getChannelUuid());
            return false;
        }
    }
}