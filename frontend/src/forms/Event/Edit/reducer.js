import {createEmptyResponse} from "App/utils/utils";
import {DEFAULT_CREATE_EVENT} from "forms/utils/constants";

import {
    EDIT_EVENT_EVENT,
    EDIT_EVENT_NEW_EVENT,
    EDIT_EVENT_TRANSPORT_TYPES,
    EDIT_EVENT_UPDATED_EVENT
} from "./constants";

export const eventEdit = {
    event: createEmptyResponse(EDIT_EVENT_EVENT, {...DEFAULT_CREATE_EVENT}),
    updatedEvent: createEmptyResponse(EDIT_EVENT_UPDATED_EVENT),
    newEvent: createEmptyResponse(EDIT_EVENT_NEW_EVENT),
    transportTypes: createEmptyResponse(EDIT_EVENT_TRANSPORT_TYPES, [])
};