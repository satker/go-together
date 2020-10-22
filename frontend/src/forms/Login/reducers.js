import {createEmptyResponse} from "App/utils/utils";
import {LOGIN_ID, LOGIN_TOKEN} from "./constants";

export const login = {
    loginId: createEmptyResponse(LOGIN_ID),
    loginToken: createEmptyResponse(LOGIN_TOKEN, {token: null})
};