import React from "react";
import {getSrcForImg} from "../../../utils/utils";
import {Review} from "../../../types";
import Rating from "@material-ui/lab/Rating";
import {Input} from "reactstrap";

const ItemComment = ({review}) =>
    <div className="container-review-item">
        <div className='flex-column fixed-width-sub-card'>
            <div className='flex margin-right-item center-items'>
                <img src={review.user.userPhoto
                    ? getSrcForImg(review.user.userPhoto)
                    : 'http://bazbiz.ru/images/default-user.jpg'}
                     width="140"
                     height="150"
                     alt={'User'}/>
            </div>
            <div className='flex margin-right-item center-items'>
                {review.user.login}
            </div>
        </div>
        <div className='flex-column'>
            <div className='flex'>
                <Rating
                    size='small'
                    value={review.rating}
                    readOnly/>
            </div>
            <div className='flex '>
                {review.dateCreation}
            </div>
            <div className='flex overflow-x-custom'>
                <Input type="textarea"
                       name="text"
                       id="description"
                       value={review.message}/>
            </div>
        </div>
    </div>;

ItemComment.propTypes = {
    review: Review.isRequired
};

export default ItemComment;