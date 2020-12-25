import {USER_SERVICE_URL} from "forms/utils/constants";
import {
    REGISTER_ALL_INTERESTS,
    REGISTER_ALL_LANGUAGES,
    REGISTER_CHECK_MAIL,
    REGISTER_CHECK_USERNAME
} from "./constants";
import {POST} from "App/utils/api/constants";

export const getAllLanguages = () => (dispatch) => {
    dispatch({
        type: REGISTER_ALL_LANGUAGES,
        method: POST,
        url: USER_SERVICE_URL + '/find',
        data: {
            mainIdField: "languages"
        }
    });
};

export const getAllInterests = () => (dispatch) => {
    dispatch({
        type: REGISTER_ALL_INTERESTS,
        method: POST,
        url: USER_SERVICE_URL + '/find',
        data: {
            mainIdField: "interests"
        }
    });
};

export const getCheckMail = (value) => (dispatch) => {
    dispatch({
        type: REGISTER_CHECK_MAIL,
        url: USER_SERVICE_URL + '/users/check/mail/' + value
    });
};

export const getCheckUserName = (value) => (dispatch) => {
    dispatch({
        type: REGISTER_CHECK_USERNAME,
        url: USER_SERVICE_URL + '/users/check/login/' + value
    });
};