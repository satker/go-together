import {createEmptyResponse} from "../../../App/utils/utils";
import {paidThings} from "./PaidThings/reducers";
import {mainInfo} from "./MainInfo/reducers";

export const eventEdit = {
    event: createEmptyResponse(false),
    updatedEvent: createEmptyResponse(false),
    newEvent: createEmptyResponse(false),
    paidThings,
    mainInfo
};