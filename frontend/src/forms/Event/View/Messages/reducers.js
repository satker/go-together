import {createEmptyResponse} from "App/utils/utils";

import {MESSAGES_CHATS, MESSAGES_MESSAGES_BY_EVENT, MESSAGES_NEW_MESSAGE, MESSAGES_USERS_INFO} from "./constants";

export const messages = {
    messagesByEvent: createEmptyResponse(MESSAGES_MESSAGES_BY_EVENT, []),
    chats: createEmptyResponse(MESSAGES_CHATS, []),
    usersInfo: createEmptyResponse(MESSAGES_USERS_INFO, []),
    newMessage: createEmptyResponse(MESSAGES_NEW_MESSAGE, {})
};