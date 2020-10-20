import {CSRF_TOKEN, USER_ID} from "App/Context/constants";
import {AUTH} from "../Context/constants";

export const cleanAuth = () => (dispatch) => {
    localStorage.removeItem(USER_ID);
    localStorage.removeItem(CSRF_TOKEN);
    dispatch({
        type: AUTH,
        value: {
            userId: null,
            csrfToken: null
        }
    })
};