import React, {useCallback, useEffect, useState} from "react";
import CustomButton from "../../../utils/components/CustomButton";
import LabeledInput from "../../../utils/LabeledInput";
import {HOST} from "../../../utils/constants";
import {connect} from "../../../../App/Context";
import PropTypes from "prop-types";
import {Review} from "../../../utils/types";
import {putReview} from "./actions";
import moment from "moment";
import {
    initialize,
    send,
    setOnCloseDataChanel,
    setOnErrorDataChanel,
    setOnIceCandidatePeerConnection,
    setOnMessageConn,
    setOnMessageDataChanel
} from "./utils";

const MessageRTC = ({userId, eventId, setMessages, messages, eventUserId, userMessageId, putReview}) => {
    const [message, setMessage] = useState('');
    const [conn, setConn] = useState(null);
    const [peerConnection, setPeerConnection] = useState(null);
    const [dataChannel, setDataChannel] = useState(null);
    const [candidates, setCandidates] = useState([]);

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
            setOnIceCandidatePeerConnection(peerConnection, conn, userId, eventUserId, userMessageId, eventId)
        }
    }, [peerConnection, conn, eventUserId, eventId, userMessageId, userId]);

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

    const onChangeCandidates = useCallback((newCandidateId) => {
        if (userId !== newCandidateId && !candidates.filter(candidate => candidate === newCandidateId)[0]) {
            setCandidates([...candidates, newCandidateId]);
        }
    }, [candidates, setCandidates, userId]);

    useEffect(() => {
        if (peerConnection && conn) {
            setOnMessageConn(conn, peerConnection, onChangeCandidates, userId, eventUserId, userMessageId, eventId)
        }
    }, [userId, conn, peerConnection, onChangeCandidates, eventId, eventUserId, userMessageId]);

    const createOffer = () => {
        peerConnection.createOffer((offer) => {
            send({
                event: "offer",
                data: offer,
                eventId,
                userId: userId === eventUserId ? eventId : userId,
                userRecipientId: userId === eventUserId ? userMessageId : eventId
            }, conn);
            peerConnection.setLocalDescription(offer);
        }, (error) => {
            alert("Error creating an offer" + error);
        });
    };

    const sendMessage = (message, setMessage) => {
        const authorId = userId === eventUserId ? eventId : userId;
        const recipientId = userMessageId === eventUserId ? eventId : userMessageId;
        const date = moment();
        setMessage('');
        const newMessage = {
            message,
            authorId,
            recipientId,
            date
        };
        setMessages([...messages, newMessage]);
        setMessage('');
        if (dataChannel.readyState === 'open') {
            dataChannel.send(JSON.stringify(newMessage));
        }
        putMessage(newMessage);
    };

    const putMessage = (newMessage) => {
        putReview(eventId, newMessage);
    };

    return <>
        <CustomButton onClick={createOffer} text={'createOffer'}/>
        <LabeledInput onChange={setMessage} id='message' value={message}/>
        <CustomButton onClick={() => sendMessage(message, setMessage)} text={'Send message'}/>
    </>
};

MessageRTC.propTypes = {
    messages: PropTypes.arrayOf(Review),
    setMessages: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    eventUserId: PropTypes.string,
    userMessageId: PropTypes.string,
    putReview: PropTypes.func.isRequired
};

const mapStateToProps = () => state => ({
    userId: state.userId
});

export default connect(mapStateToProps, {putReview})(MessageRTC);