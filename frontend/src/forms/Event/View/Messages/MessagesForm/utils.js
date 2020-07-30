import moment from "moment";

export const setOnIceCandidatePeerConnection = (peerConnection, conn, userId, userRecipientId, eventId) => {
    // Setup ice handling
    peerConnection.onicecandidate = (event) => {
        if (event.candidate) {
            send({
                event: "candidate",
                data: event.candidate,
                userId,
                eventId,
                userRecipientId
            }, conn);
        }
    };
};

export const setOnMessageDataChanel = (dataChannel, messages, setMessages) => {
    dataChannel.onmessage = (event) => {
        const gotMessage = JSON.parse(event.data);
        gotMessage.date = moment(gotMessage.date);
        setMessages([...messages, gotMessage]);
    };
};

export const setOnErrorDataChanel = (dataChannel) => {
    dataChannel.onerror = (error) => {
        console.log("Error occured on datachannel:", error);
    };
};

export const setOnCloseDataChanel = (dataChannel) => {
    dataChannel.onclose = () => {
        console.log("data channel is closed");
    };
};

export const setOnMessageConn = (conn, peerConnection, userId, userRecipientId, eventId) => {
    conn.onmessage = (msg) => {
        const content = JSON.parse(msg.data);
        const data = content.data;
        switch (content.event) {
            // when somebody wants to call us
            case "offer":
                handleOffer(data, conn, peerConnection, userRecipientId, eventId, userId);
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
};

export const send = (message, conn) => {
    conn.send(JSON.stringify(message));
};

export const handleOffer = (offer, conn, peerConnection, userMessageId, eventId, userId) => {
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

export const handleAnswer = (answer, peerConnection) => {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
};

export const initialize = (conn, setPeerConnection, setDataChannel, eventId) => {
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

export const handleCandidate = (candidate, peerConnection) => {
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
};