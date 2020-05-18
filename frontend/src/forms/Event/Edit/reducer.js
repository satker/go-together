import {createEmptyResponse} from "App/utils/utils";
import {DEFAULT_CREATE_EVENT} from "forms/utils/constants";

import {paidThings} from "./PaidThings/reducers";
import {mainInfo} from "./MainInfo/reducers";
import {EDIT_EVENT_EVENT, EDIT_EVENT_NEW_EVENT, EDIT_EVENT_UPDATED_EVENT} from "./constants";

export const eventEdit = {
    event: createEmptyResponse(EDIT_EVENT_EVENT, {...DEFAULT_CREATE_EVENT}),
    updatedEvent: createEmptyResponse(EDIT_EVENT_UPDATED_EVENT),
    newEvent: createEmptyResponse(EDIT_EVENT_NEW_EVENT),
    paidThings,
    mainInfo
};