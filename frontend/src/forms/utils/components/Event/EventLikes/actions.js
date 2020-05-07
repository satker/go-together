import {USER_SERVICE_URL} from "../../../constants";
import {EVENT_LIKES, EVENT_LIKES_NEW_LIKE} from "./constants";

export const putNewLike = (state) => (eventId) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/' + state.userId + '/events/' + eventId, {})(EVENT_LIKES_NEW_LIKE);
};

export const postLikes = () => (eventIds) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/events/likes', eventIds)(EVENT_LIKES);
};