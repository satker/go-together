import {createEmptyResponse} from "../../App/utils/utils";
import {LOGIN_HEADERS, LOGIN_ID} from "./constants";

export const login = {
    loginId: createEmptyResponse(LOGIN_ID),
    loginToken: createEmptyResponse(LOGIN_HEADERS)
};