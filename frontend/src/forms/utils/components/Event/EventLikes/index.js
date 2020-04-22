import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {connect} from "../../../../../App/Context";
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import {postLikes, putNewLike} from "./actions";
import LoadableContent from "../../LoadableContent";

const EventLikes = ({eventId, putNewLike, newLike, postLikes, likes, userId, eventIds}) => {
    const [flag, setFlag] = useState(false);
    useEffect(() => {
        if (newLike && flag) {
            postLikes(eventIds);
            setFlag(false)
        }
    }, [newLike, postLikes, eventIds, flag, setFlag]);

    const saveLike = () => {
        putNewLike(eventId);
        setFlag(true);
    };

    const currentLikes = likes.response[eventId] || [];
    const likeType = !!currentLikes
        .map(likedUser => likedUser.id)
        .filter(userIdNew => userIdNew === userId)[0];

    return <LoadableContent loadableData={likes}>
        {likeType ? <FavoriteBorderIcon color='error' onClick={saveLike}/> :
            <FavoriteIcon color='error' onClick={saveLike}/>}
        {currentLikes.length} likes this
    </LoadableContent>;
};

EventLikes.propTypes = {
    eventId: PropTypes.string.isRequired,
    eventIds: PropTypes.arrayOf(PropTypes.string).isRequired,

};

const mapStateToProps = (FORM_ID) => (state) => ({
    newLike: state[FORM_ID]?.newLike,
    likes: state[FORM_ID]?.likes,
    userId: state.userId
});

export default connect(mapStateToProps, {putNewLike, postLikes})(EventLikes);