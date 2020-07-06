import React, {useEffect} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";

const TextField = ({name, value, setValue, error, type, checkValue, checked, setError, placeholder}) => {
    useEffect(() => {
        if (checked?.response === true) {
            setError(name, "* " + placeholder + " is busy.");
        }
    }, [checked, setError, name, error]);

    return <LabeledInput
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
        }}/>;
}

export default TextField;

