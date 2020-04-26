import {MESSAGE_SERVICE_URL} from "../../../utils/constants";
import {messages} from "./reducers";

export const getReviewsByEvent = () => (eventId, authorId) => (dispatch) => {
    dispatch(MESSAGE_SERVICE_URL + "/events/_id_/messages/_author_id_".replace("_id_", eventId)
        .replace('_author_id_', authorId))
    (messages.reviewsByEvent);
};

export const putReview = () => (eventId, body, setRefreshChats, refresh) => (dispatch) => {
    dispatch(MESSAGE_SERVICE_URL + "/events/_id_/messages".replace("_id_", eventId), body)
    (messages.review);
};