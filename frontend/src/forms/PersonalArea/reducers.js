import {createEmptyResponse} from "App/utils/utils";

import {PERSONAL_AREA_CHECK_MAIL, PERSONAL_AREA_UPDATED_USER, PERSONAL_AREA_USER_INFO} from "./constants";

export const personalArea = {
    userInfo: createEmptyResponse(PERSONAL_AREA_USER_INFO),
    updatedUser: createEmptyResponse(PERSONAL_AREA_UPDATED_USER),
    checkMail: createEmptyResponse(PERSONAL_AREA_CHECK_MAIL),
};