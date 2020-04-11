import React, {useContext} from "react";
import ItemComment from "./ItemComment";
import {Context} from "../../../Context";
import {Review} from "../../../types";
import PropTypes from "prop-types";

const MessagesContainer = ({reviews, eventUserId, eventId}) => {
    const [state] = useContext(Context);

    return reviews.length !== 0 ? reviews.map((review, key) =>
        <ItemComment key={key}
                     review={review}
                     style={review.authorId ===
                     (state.userId === eventUserId ? eventId : state.userId)
                         ? 'message-orange' : 'message-blue'}
                     timestampStyle={review.authorId ===
                     (state.userId === eventUserId ? eventId : state.userId)
                         ? 'message-timestamp-right' : 'message-timestamp-left'}/>
    ) : <div className='no-messages-text'>
        No messages presented
    </div>;
};

MessagesContainer.propTypes = {
    reviews: PropTypes.arrayOf(Review),
    eventUserId: PropTypes.string.isRequired,
    eventId: PropTypes.string.isRequired
};

export default MessagesContainer;