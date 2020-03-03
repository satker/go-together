import React, {useContext, useEffect, useState} from "react";
import PropTypes from 'prop-types';
import {MESSAGE_SERVICE_URL} from '../../../utils/constants'
import {Context} from "../../../Context";
import ItemComment from "./ItemComment";
import InputComment from "./InputComment";

const URL_EVENT_REVIEWS = MESSAGE_SERVICE_URL + "/events/_id_/messages";

const Reviews = ({eventId}) => {
    const [reviewsByEvent, setReviewsByEvent] = useState([]);
    const [state] = useContext(Context);

    useEffect(() => {
        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId), setReviewsByEvent);
    }, [eventId, setReviewsByEvent, state]);

    return <>
        {state.userId ? <InputComment setReviewsByEvent={setReviewsByEvent}
                                      eventId={eventId}
        /> : null}
        <h3>Reviews</h3>
        {reviewsByEvent instanceof Array && reviewsByEvent.map((review, key) =>
            <ItemComment key={key} review={review}/>
        )}
    </>
};

Reviews.props = {
    eventId: PropTypes.string.isRequired
};

export default Reviews;