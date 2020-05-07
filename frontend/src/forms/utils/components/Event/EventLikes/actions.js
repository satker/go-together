import {USER_SERVICE_URL} from "../../../constants";
import {EVENT_LIKES, EVENT_LIKES_NEW_LIKE} from "./constants";
import {POST, PUT} from "../../../../../App/utils/api/constants";

export const putNewLike = (state) => (eventId) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/' + state.userId + '/events/' + eventId, PUT, {})(EVENT_LIKES_NEW_LIKE);
};

export const postLikes = () => (eventIds) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/events/likes', POST, eventIds)(EVENT_LIKES);
};