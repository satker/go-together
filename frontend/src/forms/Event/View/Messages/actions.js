import {MESSAGE_SERVICE_URL} from "forms/utils/constants";
import {POST, PUT} from "App/utils/api/constants";

import {MESSAGES_MESSAGES_BY_EVENT, MESSAGES_NEW_MESSAGE} from "./constants";

export const getMessagesByEvent = (eventId, authorId) => (dispatch) => {
    dispatch({
        type: MESSAGES_MESSAGES_BY_EVENT,
        method: POST,
        url: MESSAGE_SERVICE_URL + '/find',
        data: {
            mainIdField: "messages",
            filters: {
                "messageType": {
                    filterType: 'EQUAL',
                    values: [{
                        messageType: 'TO_EVENT'
                    }]
                },
                "[recipientId&authorId]": {
                    filterType: 'EQUAL',
                    values: [{
                        recipientId: eventId,
                        authorId
                    },
                        {
                        recipientId: authorId,
                        authorId: eventId
                    }]
                },
            }
        }
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