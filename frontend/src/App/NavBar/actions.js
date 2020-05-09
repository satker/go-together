import {CONTEXT_USER_ID, FETCH} from "../Context/constants";
import {fetchAndSetToken} from "../utils/api/request";

export const cleanUserId = () => (dispatch) => {
    dispatch({
        type: CONTEXT_USER_ID,
        value: ''
    })
};

export const cleanFetchWithToken = () => (dispatch) => {
    dispatch({
        type: FETCH,
        value: fetchAndSetToken(null)
    })
};