import {USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

import {PERSONAL_AREA_ALL_INTERESTS, PERSONAL_AREA_ALL_LANGUAGES, PERSONAL_AREA_USER_INFO} from "./constants";

export const URL_USER = USER_SERVICE_URL + "/users/";
const URL_USER_ID = URL_USER + "_id_";
const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/_mail_';

export const getUserInfo = () => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_USER_INFO,
        url: URL_USER_ID.replace('_id_', state.auth.response.userId)
    });
};

export const getAllLanguages = () => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_ALL_LANGUAGES,
        method: POST,
        url: USER_SERVICE_URL + '/find',
        data: {
            mainIdField: "language"
        }
    });
};

export const getAllInterests = () => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_ALL_INTERESTS,
        method: POST,
        url: USER_SERVICE_URL + '/find',
        data: {
            mainIdField: "interest"
        }
    });
};