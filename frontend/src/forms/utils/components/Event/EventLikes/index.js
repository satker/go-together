import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {connect} from "../../../../../App/Context";
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import {FORM_ID} from "./constants";
import {getLikes, putNewLike} from "./actions";

const EventLikes = ({eventId, putNewLike, newLike, getLikes, likes, userId}) => {
    const [flag, setFlag] = useState(false);

    useEffect(() => {
        if (newLike && flag) {
            getLikes(eventId);
            setFlag(false)
        }
    }, [newLike, getLikes, eventId, flag, setFlag]);

    const saveLike = () => {
        putNewLike(eventId);
        setFlag(true);
    };

    useEffect(() => {
        getLikes(eventId);
    }, [getLikes, eventId]);

    const likeType = !!likes.response.map(likedUser => likedUser.id).filter(userIdNew => userIdNew === userId)[0];

    return <div>
        {likeType ? <FavoriteBorderIcon color='error' onClick={saveLike}/> :
            <FavoriteIcon color='error' onClick={saveLike}/>}
        {likes.response.length} likes this
    </div>;
};

EventLikes.propTypes = {
    eventId: PropTypes.string.isRequired,
};

const mapStateToProps = (FORM_ID) => (state) => ({
    newLike: state[FORM_ID]?.newLike,
    likes: state[FORM_ID]?.likes || [],
    userId: state.userId
});

export default connect(mapStateToProps, {putNewLike, getLikes})(EventLikes)(FORM_ID);