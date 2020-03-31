import React from "react";
import {Review} from "../../../types";

const ItemComment = ({review, style, timestampStyle}) => {
    const date = review.date.format('LLL');

    return <div className={style}>
        <p className='message-content'>
            {review.message}
        </p>
        <div className={timestampStyle}>
            {date}
        </div>
    </div>
};

ItemComment.propTypes = {
    review: Review.isRequired
};

export default ItemComment;