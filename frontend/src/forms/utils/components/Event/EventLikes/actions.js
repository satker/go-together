import {USER_SERVICE_URL} from "../../../constants";
import {eventLikes} from "./reducers";

export const putNewLike = (state) => (eventId) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/' + state.userId + '/events/' + eventId, {})(eventLikes.newLike);
};

export const postLikes = () => (eventIds) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/events/likes', eventIds)(eventLikes.likes);
};