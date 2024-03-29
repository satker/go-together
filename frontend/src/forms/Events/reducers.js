import {createEmptyResponse} from "App/utils/utils";

import {DELETE_EVENT, EVENTS_FIND_EVENTS, EVENTS_INTERESTS, EVENTS_LANGUAGES, FILTER} from "./constants";
import {FORM_DTO} from "../utils/constants";

export const events = {
    filter: createEmptyResponse(FILTER, FORM_DTO("events")),
    findEvents: createEmptyResponse(EVENTS_FIND_EVENTS, []),
    interests: createEmptyResponse(EVENTS_INTERESTS, []),
    languages: createEmptyResponse(EVENTS_LANGUAGES, []),
    deletedEvent: createEmptyResponse(DELETE_EVENT)
};