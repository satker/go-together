import React, {useCallback, useEffect, useState} from "react";
import moment from "moment";

import {connect} from "App/Context";

import MessagesContainer from "./MesagesContainer";
import WebRTCInputMessage from "./WebRTCInputMessage";
import {getMessagesByEvent} from "../actions";

const MessagesForm = ({event, userMessageId, userId, messagesByEvent, getMessagesByEvent}) => {
    const [parsedReviewsByEvent, setParsedReviewsByEvent] = useState([]);

    const getMessages = useCallback(() => {
        const authorId = userId === event.author.id ? userMessageId : userId;
        getMessagesByEvent(event.id, authorId);
    }, [event, userId, userMessageId, getMessagesByEvent]);

    useEffect(() => {
        const updateReviews = messagesByEvent.response.map(review => {
            review.date = moment(review.date);
            return review;
        }).sort((review1, review2) => review1.date.diff(review2.date, 'seconds'));
        setParsedReviewsByEvent(updateReviews);
    }, [messagesByEvent]);

    useEffect(() => {
        const authorId = userId === event.author.id ? userMessageId : userId;
        if (userMessageId && authorId) {
            getMessages();
        } else {
            setParsedReviewsByEvent([])
        }
    }, [getMessages, setParsedReviewsByEvent, userId, userMessageId, event]);

    return <div className='container-input-messages' style={{width: event.author.id === userId ? '70%' : '100%'}}>
        <div className='container-messages'>
            <MessagesContainer reviews={parsedReviewsByEvent}/>
        </div>
        {userMessageId && <WebRTCInputMessage messages={parsedReviewsByEvent}
                                              setMessages={setParsedReviewsByEvent}
                                              userMessageId={userMessageId}
                                              readOnly={parsedReviewsByEvent.length === 0}/>}
    </div>
};

MessagesForm.propTypes = {};

const mapStateToProps = () => state => ({
    userId: state.userId.value,
    messagesByEvent: state.components.forms.event.eventView.messages.messagesByEvent,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, {getMessagesByEvent})(MessagesForm);