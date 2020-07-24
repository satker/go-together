import {USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

import {
    FORM_ID,
    PERSONAL_AREA_ALL_INTERESTS,
    PERSONAL_AREA_ALL_LANGUAGES,
    PERSONAL_AREA_CHECK_MAIL,
    PERSONAL_AREA_UPDATED_USER,
    PERSONAL_AREA_USER_INFO
} from "./constants";

const URL_USER = USER_SERVICE_URL + "/users/";
const URL_USER_ID = URL_USER + "_id_";
const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/_mail_';
const URL_USERS_LANGUAGES = USER_SERVICE_URL + "/languages";
const URL_USERS_INTERESTS = USER_SERVICE_URL + "/interests";

export const getUserInfo = () => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_USER_INFO,
        url: URL_USER_ID.replace('_id_', state.userId.value)
    });
};

export const updateUser = () => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_UPDATED_USER,
        url: URL_USER,
        method: POST,
        data: state.components.utils.input[FORM_ID].value
    });
};

export const getCheckMail = (value) => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_CHECK_MAIL,
        url: URLtoCheck.replace("_mail_", value)
    });
};

export const getAllLanguages = () => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_ALL_LANGUAGES,
        url: URL_USERS_LANGUAGES
    });
};

export const getAllInterests = () => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_ALL_INTERESTS,
        url: URL_USERS_INTERESTS
    });
};