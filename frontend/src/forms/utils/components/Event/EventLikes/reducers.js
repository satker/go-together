import {createEmptyResponse} from "App/utils/utils";

import {EVENT_LIKES, EVENT_LIKES_NEW_LIKE} from "./constants";

export const eventLikes = {
    newLike: createEmptyResponse(EVENT_LIKES_NEW_LIKE),
    likes: createEmptyResponse(EVENT_LIKES, [])
};