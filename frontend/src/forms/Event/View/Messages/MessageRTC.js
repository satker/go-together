import React, {useCallback, useEffect, useState} from "react";
import CustomButton from "../../../utils/components/CustomButton";
import LabeledInput from "../../../utils/LabeledInput";
import {HOST} from "../../../utils/constants";
import {connect} from "../../../../App/Context";
import PropTypes from "prop-types";
import {Review} from "../../../utils/types";
import {putReview} from "./actions";
import moment from "moment";

const send = (message, conn) => {
    conn.send(JSON.stringify(message));
};

const handleOffer = (offer, conn, peerConnection, userMessageId, eventId, userId) => {
    peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

    // create and send an answer to an offer
    peerConnection.createAnswer((answer) => {
        peerConnection.setLocalDescription(answer);
        send({
            event: "answer",
            data: answer,
            userRecipientId: userMessageId,
            eventId,
            userId
        }, conn);
    }, (error) => {
        alert("Error creating an answer" + error);
    });

};

const handleAnswer = (answer, peerConnection) => {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
};

const initialize = (conn, setPeerConnection, setDataChannel, eventId) => {
    const configuration = null;

    const peerConnection = new RTCPeerConnection(configuration, {
        optional: [{
            RtpDataChannels: true
        }]
    });

    // creating data channel
    const dataChannel = peerConnection.createDataChannel(eventId, {
        reliable: true
    });

    setDataChannel(dataChannel);
    setPeerConnection(peerConnection);
};

const handleCandidate = (candidate, peerConnection) => {
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
};

const MessageRTC = ({userId, eventId, setMessages, messages, eventUserId, userMessageId, putReview}) => {
    const [message, setMessage] = useState('');
    const [conn] = useState(new WebSocket('ws://' + HOST + ':8064/socket'));
    const [peerConnection, setPeerConnection] = useState(null);
    const [dataChannel, setDataChannel] = useState(null);
    const [candidates, setCandidates] = useState([]);

    useEffect(() => {
        conn.onopen = () => {
            initialize(conn, setPeerConnection, setDataChannel, eventId);
            console.log("Connected to the signaling server");
        };
    }, [eventId, conn]);

    useEffect(() => {
        if (peerConnection) {
            const userIdCurrent = userId === eventUserId ? eventId : userId;
            const userMessageIdCurrent = userId === eventUserId ? userMessageId : eventId;
            // Setup ice handling
            peerConnection.onicecandidate = (event) => {
                if (event.candidate) {
                    send({
                        event: "candidate",
                        data: event.candidate,
                        userId: userIdCurrent,
                        eventId,
                        userRecipientId: userMessageIdCurrent
                    }, conn);
                }
            };
        }
    }, [peerConnection, conn, eventUserId, eventId, userMessageId, userId]);

    useEffect(() => {
        if (dataChannel) {
            dataChannel.onmessage = (event) => {
                const gotMessage = JSON.parse(event.data);
                gotMessage.date = moment(gotMessage.date);
                setMessages([...messages, gotMessage]);
            };
        }
    }, [setMessages, messages, dataChannel]);

    useEffect(() => {
        if (dataChannel) {
            dataChannel.onerror = (error) => {
                console.log("Error occured on datachannel:", error);
            };
        }
    }, [dataChannel]);

    useEffect(() => {
        if (dataChannel) {
            dataChannel.onclose = () => {
                console.log("data channel is closed");
            };
        }
    }, [dataChannel]);

    const onChangeCandidates = useCallback((newCandidateId) => {
        if (userId !== newCandidateId && !candidates.filter(candidate => candidate === newCandidateId)[0]) {
            setCandidates([...candidates, newCandidateId]);
        }
    }, [candidates, setCandidates, userId]);

    useEffect(() => {
        if (peerConnection) {
            const userIdCurrent = userId === eventUserId ? eventId : userId;
            const userMessageIdCurrent = userId === eventUserId ? userMessageId : eventId;
            conn.onmessage = (msg) => {
                const content = JSON.parse(msg.data);
                if (content.userId) {
                    onChangeCandidates(content.userId)
                }
                const data = content.data;
                switch (content.event) {
                    // when somebody wants to call us
                    case "offer":
                        handleOffer(data, conn, peerConnection, userMessageIdCurrent, eventId, userIdCurrent);
                        break;
                    case "answer":
                        handleAnswer(data, peerConnection);
                        break;
                    // when a remote peer sends an ice candidate to us
                    case "candidate":
                        handleCandidate(data, peerConnection);
                        break;
                    default:
                        break;
                }
            };
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