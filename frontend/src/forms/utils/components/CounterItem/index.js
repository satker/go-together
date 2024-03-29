import React from 'react';
import PropTypes from 'prop-types';

import CustomButton from "forms/utils/components/CustomButton";

const CounterItem = ({value, setValue, disabled}) => {
    const onChange = value => {
        if (value <= 10 && value >= 0) {
            setValue(value);
        }
    };

    return <>
        <CustomButton text='-'
                      disabled={disabled}
                      color="primary"
                      onClick={() => onChange(value - 1)}/>
        {' '}{value}{' '}
        <CustomButton text='+'
                      disabled={disabled}
                      color="primary"
                      onClick={() => onChange(value + 1)}/>
    </>

};

CounterItem.propTypes = {
    value: PropTypes.object.isRequired,
    setValue: PropTypes.func.isRequired,
    disabled: PropTypes.bool.isRequired
};

export default CounterItem;