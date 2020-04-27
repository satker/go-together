import {createEmptyResponse} from "../../../../App/utils/utils";

export const messages = {
    review: createEmptyResponse(false),
    reviewsByEvent: createEmptyResponse(false, []),
    messages: createEmptyResponse(false, []),
    usersInfo: createEmptyResponse(false, [])
};