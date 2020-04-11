import React, {useContext, useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {Context} from "../../../../Context";
import {USER_SERVICE_URL} from "../../../constants";
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';

const EventLikes = ({eventId}) => {
    const [userLikes, setUserLikes] = useState([]);
    const [newLike, setNewLike] = useState(null);
    const [state] = useContext(Context);

    const saveLike = () => {
        state.fetchWithToken(USER_SERVICE_URL + '/users/' + state.userId + '/events/' + eventId, result => {
            setNewLike(result);
            state.fetchWithToken(USER_SERVICE_URL + '/events/' + eventId + '/likes', setUserLikes);
        }, 'PUT', {});
    };

    useEffect(() => {
        state.fetchWithToken(USER_SERVICE_URL + '/events/' + eventId + '/likes', (result) => {
            setUserLikes(result);
            setNewLike(!!result.map(likedUser => likedUser.id).filter(userId => userId === state.userId)[0])
        });
    }, [eventId, state]);

    return <div>
        {newLike ? <FavoriteBorderIcon color='error' onClick={saveLike}/> :
            <FavoriteIcon color='error' onClick={saveLike}/>}
        {userLikes.length} likes this
    </div>;
};

EventLikes.propTypes = {
    eventId: PropTypes.string.isRequired,
};

export default EventLikes;