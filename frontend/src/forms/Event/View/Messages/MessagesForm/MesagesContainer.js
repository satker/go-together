import React from "react";
import PropTypes from "prop-types";

import {connect} from "App/Context";
import {Review} from "forms/utils/types";

import ItemComment from "./ItemComment";

const MessagesContainer = ({reviews, event, userId}) => {
    return reviews.length !== 0 ? reviews.map((review, key) =>
        <ItemComment key={key}
                     review={review}
                     style={review.authorId ===
                     (userId === event.author.id ? event.id : userId)
                         ? 'message-orange' : 'message-blue'}
                     timestampStyle={review.authorId ===
                     (userId === event.author.id ? event.id : userId)
                         ? 'message-timestamp-right' : 'message-timestamp-left'}/>
    ) : <div className='no-messages-text'>
        No messages presented
    </div>;
};

MessagesContainer.propTypes = {
    reviews: PropTypes.arrayOf(Review),
    eventUserId: PropTypes.string,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    userId: state.userId.value,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, null)(MessagesContainer);