import {USER_SERVICE_URL} from "../../../constants";

export const putNewLike = (state) => (eventId) => (fetch) => {
    fetch(USER_SERVICE_URL + '/users/' + state.userId + '/events/' + eventId, {});
};

export const postLikes = () => (eventIds) => (fetch) => {
    fetch(USER_SERVICE_URL + '/events/likes', eventIds);
};