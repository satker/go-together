import {USER_SERVICE_URL} from "forms/utils/constants";
import {POST, PUT} from "App/utils/api/constants";

import {EVENT_LIKES, EVENT_LIKES_NEW_LIKE} from "./constants";

export const putNewLike = (eventId) => (dispatch, state) => {
    dispatch({
        type: EVENT_LIKES_NEW_LIKE,
        url: USER_SERVICE_URL + '/users/' + state.userId.value + '/events/' + eventId,
        method: PUT,
        data: {}
    });
};

export const postLikes = (eventIds) => (dispatch) => {
    dispatch({
        type: EVENT_LIKES,
        url: USER_SERVICE_URL + '/events/likes',
        method: POST,
        data: eventIds
    });
};