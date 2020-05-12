import {createEmptyResponse} from "../../../../App/utils/utils";
import {PARTICIPATION_BUTTON_FROM_LIST, PARTICIPATION_BUTTON_TO_LIST} from "./constants";

export const participationButton = {
    meToList: createEmptyResponse(PARTICIPATION_BUTTON_TO_LIST),
    meFromList: createEmptyResponse(PARTICIPATION_BUTTON_FROM_LIST)
};