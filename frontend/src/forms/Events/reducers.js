import {createEmptyResponse} from "App/utils/utils";

import {EVENTS_APARTMENT_TYPES, EVENTS_FIND_EVENTS, EVENTS_LANGUAGES, EVENTS_PARAMETERS} from "./constants";

export const events = {
    findEvents: createEmptyResponse(EVENTS_FIND_EVENTS, []),
    parameters: createEmptyResponse(EVENTS_PARAMETERS, []),
    apartmentTypes: createEmptyResponse(EVENTS_APARTMENT_TYPES, []),
    languages: createEmptyResponse(EVENTS_LANGUAGES, [])
};