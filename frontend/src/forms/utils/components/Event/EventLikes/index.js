import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';

import {connect} from "App/Context";
import LoadableContent from "forms/utils/components/LoadableContent";

import {getEventsLikes, updateEventLike} from "./actions";

const EventLikes = ({eventId, updateEventLike, newLike, getEventsLikes, likes, userId, eventIds}) => {
    const [flag, setFlag] = useState(false);

    useEffect(() => {
        if (newLike && flag) {
            getEventsLikes(eventIds);
            setFlag(false)
        }
    }, [newLike, getEventsLikes, eventIds, flag, setFlag]);

    const currentLikes = likes.response.find(eventLike => eventLike.eventId === eventId) || [];
    const currentLikeUsers = currentLikes?.users || [];
    const likeType = !!currentLikeUsers.find(user => user.id === userId);

    const saveLike = (isSave) => () => {
        let eventLikeObject;
        if (isSave) {
            eventLikeObject = {...currentLikes, users: [...currentLikes.users, {id: userId}]};
        } else {
            eventLikeObject = {
                ...currentLikes, users: currentLikes.users
                    .filter(eventLikeUser => eventLikeUser.id !== userId)
            }
        }
        updateEventLike(eventLikeObject);
        setFlag(true);
    };
    return <LoadableContent loadableData={likes}>
        {likeType ? <FavoriteBorderIcon color='error' onClick={saveLike(false)}/> :
            <FavoriteIcon color='error' onClick={saveLike(true)}/>}
        {currentLikeUsers.length} likes this
    </LoadableContent>;
};

EventLikes.propTypes = {
    eventId: PropTypes.string.isRequired,
    eventIds: PropTypes.arrayOf(PropTypes.string).isRequired,

};

const mapStateToProps = state => ({
    newLike: state.components.utils.eventLikes.newLike,
    likes: state.components.utils.eventLikes.likes,
    userId: state.userId.value
});

export default connect(mapStateToProps, {updateEventLike, getEventsLikes})(EventLikes);