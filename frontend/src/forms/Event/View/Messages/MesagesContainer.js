import React from "react";
import ItemComment from "./ItemComment";
import {connect} from "../../../../App/Context";
import {Review} from "../../../utils/types";
import PropTypes from "prop-types";
import {FORM_ID} from "../constants";

const MessagesContainer = ({reviews, eventUserId, eventId, userId}) => {
    return reviews.length !== 0 ? reviews.map((review, key) =>
        <ItemComment key={key}
                     review={review}
                     style={review.authorId ===
                     (userId === eventUserId ? eventId : userId)
                         ? 'message-orange' : 'message-blue'}
                     timestampStyle={review.authorId ===
                     (userId === eventUserId ? eventId : userId)
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

const mapStateToProps = () => state => ({
    userId: state.userId
});

export default connect(mapStateToProps, null)(MessagesContainer)(FORM_ID);