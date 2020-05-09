import React, {useCallback, useEffect, useState} from "react";
import {HOST} from "../../../../utils/constants";
import {connect} from "../../../../../App/Context";
import PropTypes from "prop-types";
import {Review} from "../../../../utils/types";
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
                                userId, eventId, setMessages, messages,
                                eventUserId, userMessageId, readOnly
                            }) => {
    const [conn, setConn] = useState(null);
    const [peerConnection, setPeerConnection] = useState(null);
    const [dataChannel, setDataChannel] = useState(null);
    const [sentUserId, setSentUserId] = useState(null);
    const [sentUserRecipientId, setSentUserRecipientId] = useState(null);

    useEffect(() => {
        setSentUserId(userId === eventUserId ? eventId : userId);
    }, [userId, eventUserId, eventId]);

    useEffect(() => {
        setSentUserRecipientId(userId === eventUserId ? userMessageId : eventId);
    }, [userId, eventUserId, userMessageId, eventId]);

    useEffect(() => {
        if (!conn) {
            const newConn = new WebSocket('ws://' + HOST + ':8064/socket');
            newConn.onopen = () => {
                initialize(conn, setPeerConnection, setDataChannel, eventId);
            };
            setConn(newConn);
        }
    }, [eventId, conn]);

    useEffect(() => {
        if (peerConnection && conn) {
            setOnIceCandidatePeerConnection(peerConnection, conn, sentUserId, sentUserRecipientId, eventId)
        }
    }, [peerConnection, conn, eventId, sentUserId, sentUserRecipientId]);

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
            setOnMessageConn(conn, peerConnection, sentUserId, sentUserRecipientId, eventId);
        }
    }, [conn, peerConnection, eventId, sentUserId, sentUserRecipientId]);

    const createOffer = useCallback(() => {
        console.log('create offer');

        peerConnection.createOffer((offer) => {
            send({
                event: "offer",
                data: offer,
                eventId,
                userId: sentUserId,
                userRecipientId: sentUserRecipientId
            }, conn);
            peerConnection.setLocalDescription(offer);
        }, (error) => {
            alert("Error creating an offer" + error);
        });
    }, [peerConnection, eventId, sentUserId, sentUserRecipientId, conn]);

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
                      eventId={eventId}
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
    userId: state.userId.value
});

export default connect(mapStateToProps, null)(WebRTCInputMessage);