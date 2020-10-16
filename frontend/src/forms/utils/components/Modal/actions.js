import {SHOW_MODAL} from './constants';

export const showModal = (message, onAction = () => null) => (dispatch) => {
    dispatch({
        type: SHOW_MODAL,
        value: {
            isOpen: true,
            message,
            onAction
        }
    })
};

export const hideModal = () => (dispatch) => {
    dispatch({
        type: SHOW_MODAL,
        value: {
            isOpen: false
        }
    })
};