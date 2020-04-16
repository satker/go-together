import React from 'react';
import {Button} from "reactstrap";
import PropTypes from 'prop-types';

const CounterItem = ({value, setValue, disabled}) => {
    const onChange = value => {
        if (value <= 10 && value >= 0) {
            setValue(value);
        }
    };

    return <>
        <Button disabled={disabled} color="primary" onClick={() => onChange(value - 1)} size="sm">-</Button>
        {' '}{value}{' '}
        <Button disabled={disabled} color="primary" onClick={() => onChange(value + 1)} size="sm">+</Button>
    </>

};

CounterItem.props = {
    value: PropTypes.object.isRequired,
    setValue: PropTypes.func.isRequired,
    disabled: PropTypes.bool.isRequired
};

export default CounterItem;