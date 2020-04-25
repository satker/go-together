import {createEmptyResponse} from "../../../App/utils/utils";
import {usersParticipation} from "./Users/reducers";
import {participationButton} from "./ParticipationButton/reducers";
import {messages} from "./Messages/reducers";

export const eventView = {
    event: createEmptyResponse(false),
    users: createEmptyResponse(false, []),
    statuses: createEmptyResponse(false, []),
    usersParticipation,
    participationButton,
    messages,

};