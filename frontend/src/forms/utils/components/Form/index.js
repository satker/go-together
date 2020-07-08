import React, {useEffect} from 'react';
import {get, isEmpty, keys} from 'lodash';

import {connect} from "App/Context";
import {createContextValue} from "App/utils/utils";
import {utils} from "forms/utils/reducers";
import Container from "forms/utils/components/Container/ContainerRow";
import CustomButton from "forms/utils/components/CustomButton";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import {updateValidation, updateValidationMessage, updateValue} from './actions';
import {FORM, VALIDATION} from './constants';

const ContextForm = ({
                         children, value, updateValue, updateValidation,
                         errors, onSubmit, onClose, updateValidationMessage, defaultValue
                     }) => {
    useEffect(() => {
        updateValidation(value);
    }, [value, updateValidation]);

    useEffect(() => {
        if (defaultValue) {
            const keysArray = [];
            const values = [];
            keys(defaultValue).forEach(key => {
                keysArray.push(key);
                values.push(defaultValue[key]);
            })
            updateValue(keysArray, values);
        }
    }, [defaultValue, updateValue])

    return <Container>
        {React.Children.map(children, child =>
            React.cloneElement(child, {
                ...child.props,
                error: errors[child.props.name],
                setError: updateValidationMessage,
                name: child.props.name,
                value: value ? get(value, child.props.name) : null,
                setValue: updateValue
            })
        )}
        <ContainerColumn>
            <CustomButton color="primary"
                          onClick={onSubmit}
                          disabled={!isEmpty(errors)}
                          text='Submit'/>
            <CustomButton color="secondary"
                          onClick={onClose}
                          text='Close'/>
        </ContainerColumn>
    </Container>;
};

const mapStateToProps = (FORM_ID) => (state) => {
    console.log(state.components.utils.input[FORM_ID].value, state.components.utils.validation[FORM_ID].value)
    return ({
        errors: state.components.utils.validation[FORM_ID].value,
        value: state.components.utils.input[FORM_ID].value
    });
}

export const createReduxForm = ({FORM_ID, validation}) => {
    utils.input = {
        ...utils.input,
        [FORM_ID]: createContextValue(FORM + FORM_ID)
    };
    utils.validation = {
        ...utils.validation,
        [FORM_ID]: createContextValue(FORM + FORM_ID + VALIDATION, {})
    };
    return connect(mapStateToProps(FORM_ID, validation), {
        updateValue: updateValue(FORM_ID),
        updateValidation: updateValidation(FORM_ID, validation),
        updateValidationMessage: updateValidationMessage(FORM_ID)
    })(ContextForm);
}