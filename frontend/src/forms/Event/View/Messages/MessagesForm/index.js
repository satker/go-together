import React, {useCallback, useEffect, useState} from "react";
import MessagesContainer from "./MesagesContainer";
import WebRTCInputMessage from "./WebRTCInputMessage";
import {connect} from "../../../../../App/Context";
import {getMessagesByEvent} from "../actions";
import moment from "moment";

const MessagesForm = ({eventId, eventUserId, userMessageId, userId, messagesByEvent, getMessagesByEvent}) => {
    const [parsedReviewsByEvent, setParsedReviewsByEvent] = useState([]);

    const getMessages = useCallback(() => {
        const authorId = userId === eventUserId ? userMessageId : userId;
        getMessagesByEvent(eventId, authorId);
    }, [eventUserId, eventId, userId, userMessageId, getMessagesByEvent]);

    useEffect(() => {
        const updateReviews = messagesByEvent.response.map(review => {
            review.date = moment(review.date);
            return review;
        }).sort((review1, review2) => review1.date.diff(review2.date, 'seconds'));
        setParsedReviewsByEvent(updateReviews);
    }, [messagesByEvent]);

    useEffect(() => {
        const authorId = userId === eventUserId ? userMessageId : userId;
        if (userMessageId && authorId) {
            getMessages();
        } else {
            setParsedReviewsByEvent([])
        }
    }, [getMessages, setParsedReviewsByEvent, userId, userMessageId, eventUserId]);

    return <div className='container-input-messages' style={{width: eventUserId === userId ? '70%' : '100%'}}>
        <div className='container-messages'>
            <MessagesContainer eventId={eventId}
                               eventUserId={eventUserId}
                               reviews={parsedReviewsByEvent}/>
        </div>
        {userMessageId && <WebRTCInputMessage eventId={eventId}
                                              messages={parsedReviewsByEvent}
                                              setMessages={setParsedReviewsByEvent}
                                              eventUserId={eventUserId}
                                              userMessageId={userMessageId}
                                              readOnly={parsedReviewsByEvent.length === 0}/>}
    </div>
};

MessagesForm.propTypes = {};

const mapStateToProps = () => state => ({
    userId: state.userId,
    messagesByEvent: state.components.forms.event.eventView.messages.messagesByEvent
});

export default connect(mapStateToProps, {getMessagesByEvent})(MessagesForm);