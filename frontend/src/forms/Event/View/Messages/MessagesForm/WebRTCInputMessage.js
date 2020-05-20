import React, {useCallback, useEffect, useState} from "react";
import PropTypes from "prop-types";

import {HOST} from "forms/utils/constants";
import {connect} from "App/Context";
import {Review} from "forms/utils/types";

import {
    initialize,
    send,
    setOnCloseDataChanel,
    setOnErrorDataChanel,
    setOnIceCandidatePeerConnection,
    setOnMessageConn,
    setOnMessageDataChanel
} from "./utils";
import InputMessage from "./InputMessage";

const WebRTCInputMessage = ({
                                userId, setMessages, messages,
                                userMessageId, readOnly, event
                            }) => {
    const [conn, setConn] = useState(null);
    const [peerConnection, setPeerConnection] = useState(null);
    const [dataChannel, setDataChannel] = useState(null);
    const [sentUserId, setSentUserId] = useState(null);
    const [sentUserRecipientId, setSentUserRecipientId] = useState(null);

    useEffect(() => {
        setSentUserId(userId === event.author.id ? event.id : userId);
    }, [userId, event]);

    useEffect(() => {
        setSentUserRecipientId(userId === event.author.id ? userMessageId : event.id);
    }, [userId, event, userMessageId]);

    useEffect(() => {
        if (!conn) {
            const newConn = new WebSocket('ws://' + HOST + ':8064/socket');
            newConn.onopen = () => {
                initialize(conn, setPeerConnection, setDataChannel, event.id);
            };
            setConn(newConn);
        }
    }, [event, conn]);

    useEffect(() => {
        if (peerConnection && conn) {
            setOnIceCandidatePeerConnection(peerConnection, conn, sentUserId, sentUserRecipientId, event.id)
        }
    }, [peerConnection, conn, event, sentUserId, sentUserRecipientId]);

    useEffect(() => {
        if (dataChannel) {
            setOnMessageDataChanel(dataChannel, messages, setMessages)
        }
    }, [setMessages, messages, dataChannel]);

    useEffect(() => {
        if (dataChannel) {
            setOnErrorDataChanel(dataChannel)
        }
    }, [dataChannel]);

    useEffect(() => {
        if (dataChannel) {
            setOnCloseDataChanel(dataChannel)
        }
    }, [dataChannel]);

    useEffect(() => {
        if (peerConnection && conn) {
            setOnMessageConn(conn, peerConnection, sentUserId, sentUserRecipientId, event.id);
        }
    }, [conn, peerConnection, event, sentUserId, sentUserRecipientId]);

    const createOffer = useCallback(() => {
        console.log('create offer');

        peerConnection.createOffer((offer) => {
            send({
                event: "offer",
                data: offer,
                eventId: event.id,
                userId: sentUserId,
                userRecipientId: sentUserRecipientId
            }, conn);
            peerConnection.setLocalDescription(offer);
        }, (error) => {
            alert("Error creating an offer" + error);
        });
    }, [peerConnection, event, sentUserId, sentUserRecipientId, conn]);

    const sentRtcMessage = (newMessage) => {
        if (dataChannel.readyState === 'open') {
            dataChannel.send(JSON.stringify(newMessage));
        }
    };
    console.log('rerender', {...peerConnection});

    console.log(sentUserId, sentUserRecipientId, peerConnection);
    return sentUserId && sentUserRecipientId && peerConnection &&
        <InputMessage readOnly={readOnly}
                      userId={sentUserId}
                      userRecipientId={sentUserRecipientId}
                      messages={messages}
                      setMessages={setMessages}
                      createOffer={createOffer}
                      sentRtcMessage={sentRtcMessage}
        />
};

WebRTCInputMessage.propTypes = {
    messages: PropTypes.arrayOf(Review),
    setMessages: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    eventUserId: PropTypes.string,
    userMessageId: PropTypes.string,
    readOnly: PropTypes.bool
};

const mapStateToProps = () => state => ({
    userId: state.userId.value,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, null)(WebRTCInputMessage);