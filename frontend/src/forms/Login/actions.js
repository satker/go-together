import {LOGIN_URL} from "forms/utils/constants";
import {AUTH} from "App/Context/constants";
import {POST} from "App/utils/api/constants";

export const postLogin = (username, password) => (dispatch) => {
    dispatch({
        type: AUTH,
        url: LOGIN_URL,
        method: POST,
        data: {username, password}
    });
};