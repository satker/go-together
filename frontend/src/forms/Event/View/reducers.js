import {createEmptyResponse} from "App/utils/utils";

import {usersParticipation} from "./Users/reducers";
import {participationButton} from "./ParticipationButton/reducers";
import {messages} from "./Messages/reducers";
import {EVENT_VIEW_EVENT, EVENT_VIEW_STATUSES, EVENT_VIEW_USERS} from "./constants";

export const eventView = {
    event: createEmptyResponse(EVENT_VIEW_EVENT),
    users: createEmptyResponse(EVENT_VIEW_USERS, []),
    statuses: createEmptyResponse(EVENT_VIEW_STATUSES, []),
    usersParticipation,
    participationButton,
    messages,

};