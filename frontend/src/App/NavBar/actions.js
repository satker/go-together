import {CONTEXT_USER_ID, CSRF_TOKEN} from "App/Context/constants";

export const cleanUserId = () => (dispatch) => {
    dispatch({
        type: CONTEXT_USER_ID,
        value: null
    })
};

export const cleanToken = () => (dispatch) => {
    dispatch({
        type: CSRF_TOKEN,
        value: null
    })
};