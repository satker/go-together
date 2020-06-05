import {createEmptyResponse} from "App/utils/utils";

import {EVENTS_FIND_EVENTS, EVENTS_INTERESTS, EVENTS_LANGUAGES} from "./constants";

export const events = {
    findEvents: createEmptyResponse(EVENTS_FIND_EVENTS, []),
    interests: createEmptyResponse(EVENTS_INTERESTS, []),
    languages: createEmptyResponse(EVENTS_LANGUAGES, [])
};