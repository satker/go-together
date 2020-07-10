import {USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

import {EVENT_LIKES, EVENT_LIKES_NEW_LIKE} from "./constants";

export const updateEventLike = (eventLike) => (dispatch) => {
    dispatch({
        type: EVENT_LIKES_NEW_LIKE,
        url: USER_SERVICE_URL + '/users/likes',
        method: POST,
        data: eventLike
    });
};

export const getEventsLikes = (eventIds) => (dispatch) => {
    dispatch({
        type: EVENT_LIKES,
        url: USER_SERVICE_URL + '/events/likes',
        method: POST,
        data: eventIds
    });
};