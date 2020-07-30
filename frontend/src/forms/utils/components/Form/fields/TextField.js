import React, {useEffect} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";
import ItemContainer from "../../Container/ItemContainer";

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

export default TextField;

