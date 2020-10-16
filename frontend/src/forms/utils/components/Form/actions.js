import {FORM, SUBMIT_DATA, VALIDATION} from './constants';
import {onChange} from "forms/utils/utils";
import {POST} from "App/utils/api/constants";

export const updateValue = (FORM_ID) => (path, value) => (dispatch, state) => {
    let updatedValue = null;
    onChange(state.components.utils.input[FORM_ID].value,
        result => updatedValue = result)(path, value);
    if (updatedValue) {
        dispatch({
            type: FORM + FORM_ID,
            value: updatedValue
        })
    }
};

export const updateValidation = (FORM_ID, validation) => (fields) => (dispatch) => {
    const validatedValues = validation(fields)
    dispatch({
        type: FORM + FORM_ID + VALIDATION,
        value: validatedValues
    })
};

export const onSubmit = (FORM_ID, url, method = POST, dataConverter = (data) => data) => () => (dispatch, state) => {
    dispatch({
        type: FORM + FORM_ID + SUBMIT_DATA,
        url,
        method,
        data: dataConverter(state.components.utils.input[FORM_ID].value)
    })
};

export const updateValidationMessage = (FORM_ID) => (field, message) => (dispatch, state) => {
    if (message) {
        dispatch({
            type: FORM + FORM_ID + VALIDATION,
            value: {
                ...state.components.utils.validation[FORM_ID].value,
                [field]: message
            }
        })
    }
};