import {NOTIFICATION} from "./constants";

export const showNotification = (message) => (dispatch) => {
    dispatch({
        type: NOTIFICATION,
        value: {
            isOpen: true,
            message
        }
    })
};

export const hideNotification = () => (dispatch) => {
    dispatch({
        type: NOTIFICATION,
        value: {
            isOpen: false
        }
    })
};