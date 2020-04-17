import {MESSAGE_SERVICE_URL} from "../../../utils/constants";

export const getReviewsByEvent = () => (eventId, authorId) => (fetch) => {
    fetch(MESSAGE_SERVICE_URL + "/events/_id_/messages/_author_id_".replace("_id_", eventId)
        .replace('_author_id_', authorId));
};

export const putReview = () => (eventId, body, setRefreshChats, refresh) => (fetch) => {
    fetch(MESSAGE_SERVICE_URL + "/events/_id_/messages".replace("_id_", eventId), body);
};