import React, {useCallback, useEffect, useState} from "react";
import moment from "moment";

import {connect} from "App/Context";

import MessagesContainer from "./MesagesContainer";
import {getMessagesByEvent} from "../actions";
import InputMessage from "./InputMessage";
import {clearTimer, setTimer} from "App/TemporaryTimer/actions";

const MessagesForm = ({event, userMessageId, userId, messagesByEvent, getMessagesByEvent, setTimer, clearTimer}) => {
    const [parsedReviewsByEvent, setParsedReviewsByEvent] = useState([]);
    const [currentTimer, setCurrentTimer] = useState(null);

    const getAuthorId = useCallback(() => userId === event.author.id ? userMessageId : userId,
        [userId, event, userMessageId]);

    const getMessages = useCallback(() => {
        getMessagesByEvent(event.id, getAuthorId());
    }, [event, getAuthorId, getMessagesByEvent]);

    useEffect(() => {
        const messages = messagesByEvent.response.result || messagesByEvent.response;
        const updateReviews = messages.map(review => {
            review.date = moment(review.date);
            return review;
        }).sort((review1, review2) => review1.date.diff(review2.date, 'seconds'));
        setParsedReviewsByEvent(updateReviews);
    }, [messagesByEvent]);

    useEffect(() => {
        if (userMessageId && getAuthorId()) {
            if (currentTimer) {
                clearTimer(currentTimer);
            }
            getMessages();
            const intervalId = setInterval(getMessages, 3000);
            setCurrentTimer(intervalId);
            setTimer(intervalId);
        } else {
            setParsedReviewsByEvent([])
        }
    }, [getMessages, setParsedReviewsByEvent, userMessageId]);

    return <div className='container-input-messages' style={{width: event.author.id === userId ? '70%' : '100%'}}>
        <div className='container-messages'>
            <MessagesContainer reviews={parsedReviewsByEvent}/>
        </div>
        {userMessageId && <InputMessage messages={parsedReviewsByEvent}
                                        setMessages={setParsedReviewsByEvent}
                                        userMessageId={userMessageId}
                                        readOnly={parsedReviewsByEvent.length === 0}/>}
    </div>
};

MessagesForm.propTypes = {};

const mapStateToProps = state => ({
    userId: state.auth.response.userId,
    messagesByEvent: state.components.forms.event.eventView.messages.messagesByEvent,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, {getMessagesByEvent, setTimer, clearTimer})(MessagesForm);