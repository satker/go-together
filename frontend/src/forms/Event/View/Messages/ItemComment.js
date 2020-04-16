import React from "react";
import {Review} from "../../../utils/types";

const ItemComment = ({review, style, timestampStyle}) => {
    const date = review.date.format('LLL');

    return <div className={style}>
        <div className='message-content'>
            {review.message}
        </div>
        <div className={timestampStyle}>
            {date}
        </div>
    </div>
};

ItemComment.propTypes = {
    review: Review.isRequired
};

export default ItemComment;