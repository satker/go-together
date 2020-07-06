import {isEmpty} from 'lodash';

import {FORM, VALIDATION} from './constants';
import {onChange} from "forms/utils/utils";

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
    if (!isEmpty(validatedValues)) {
        dispatch({
            type: FORM + FORM_ID + VALIDATION,
            value: validatedValues
        })
    }
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