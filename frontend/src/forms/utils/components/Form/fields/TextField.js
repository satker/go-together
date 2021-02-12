import React, {useEffect} from 'react';
import PropTypes from 'prop-types';

import LabeledInput from "forms/utils/components/LabeledInput";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {ResponseData} from "forms/utils/types";

const TextField = ({name, value, setValue, error, type, checkValue, checked, setError, placeholder}) => {
    useEffect(() => {
        if (checked?.inProcess === false && checked?.response === true) {
            setError(name, "* " + placeholder + " is busy.");
        }
    }, [checked, setError, name]);

    return <ItemContainer style={{height: 70}}>
        <LabeledInput
            id={name}
            label={placeholder || name}
            value={value || ''}
            errorText={error}
            type={type}
            isError={!!error}
            onChange={input => {
                setValue(name, input);
                if (checkValue && input) {
                    checkValue(input);
                }
            }}/>
    </ItemContainer>;
}

TextField.propTypes = {
    name: PropTypes.string.isRequired,
    value: PropTypes.string,
    setValue: PropTypes.func,
    error: PropTypes.string,
    type: PropTypes.string,
    checkValue: PropTypes.func,
    checked: ResponseData,
    setError: PropTypes.func,
    placeholder: PropTypes.string
};

export default TextField;

