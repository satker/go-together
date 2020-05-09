import {MESSAGE_SERVICE_URL} from "../../../utils/constants";
import {MESSAGES_MESSAGES_BY_EVENT, MESSAGES_NEW_MESSAGE} from "./constants";
import {PUT} from "../../../../App/utils/api/constants";

export const getMessagesByEvent = (eventId, authorId) => (dispatch) => {
    dispatch({
        type: MESSAGES_MESSAGES_BY_EVENT,
        url: MESSAGE_SERVICE_URL + "/events/_id_/messages/_author_id_".replace("_id_", eventId)
            .replace('_author_id_', authorId)
    });
};

export const putNewMessage = (eventId, body) => (dispatch) => {
    dispatch({
        type: MESSAGES_NEW_MESSAGE,
        url: MESSAGE_SERVICE_URL + "/events/_id_/messages".replace("_id_", eventId),
        method: PUT,
        data: body
    });
};